package com.shabelnikd.bilimtrack.model.core

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.shabelnikd.bilimtrack.model.models.TokenRefreshResponse
import com.shabelnikd.bilimtrack.model.service.Authenticated
import com.shabelnikd.bilimtrack.model.service.BilimTrackApiService
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import com.shabelnikd.bilimtrack.utils.safeApiCall
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import retrofit2.Invocation
import retrofit2.Retrofit
import java.io.File

class RetrofitClient(context: Context): KoinComponent {

    private val preferenceHelper: PreferenceHelper by inject()

    val httpCacheDirectory = File(context.cacheDir, "responses")
    val cache = Cache(httpCacheDirectory, CACHE_SIZE)

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val apiKeyInterceptor = object: Interceptor {
        private fun newRequest(originalRequest: Request, accessToken: String?) =
            accessToken?.let {
                originalRequest.newBuilder()
                    .header("Authorization", "Bearer $it")
                    .build()
            }?: originalRequest

        private suspend fun refreshAccessToken(refreshToken: String): Result<retrofit2.Response<TokenRefreshResponse>> {
            return safeApiCall(
                { retrofitService.userRefresh(refreshToken) },
                "Error fetching refresh token"
            )
        }

        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val method = originalRequest.tag(Invocation::class.java)?.method()
            val isAuthRequired = method?.getAnnotation(Authenticated::class.java)!= null

            if (isAuthRequired) {
                val accessToken = preferenceHelper.accessToken
                var response = chain.proceed(newRequest(originalRequest, accessToken))

                if (response.code == 401) {
                    Log.e("AuthOK", "Required auth response is 401 processing...")
                    val refreshToken = preferenceHelper.refreshToken
                    response.close()

                    refreshToken?.let {
                        val refreshResult = runBlocking { refreshAccessToken(it) }
                        Log.e("AuthOK", "RefreshToken is find too processing to get new access..")

                        if (refreshResult.isSuccess && refreshResult.getOrNull()?.isSuccessful == true) {
                            Log.e("AuthOK", "Refresh if successful processing to OriginalRequest")
                            val newAccessToken = refreshResult.getOrNull()?.body()?.access
                            preferenceHelper.accessToken = newAccessToken
                            response = chain.proceed(newRequest(originalRequest, newAccessToken))
                            if (response.isSuccessful) {
                                Log.e("AuthOK", "Original Request is Successful")
                            }
                        } else {
                            Log.e("AuthFail", "Failed to refresh access token: ${refreshResult.exceptionOrNull()?.message}")
                        }
                    }?: run {
                        Log.e("AuthFail", "Refresh token is null")
                    }
                }
                return response
            } else {
                return chain.proceed(originalRequest)
            }
        }
    }


    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(apiKeyInterceptor)
            .cache(cache)
            .build()

    }

    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        allowStructuredMapKeys = true
    }

    @OptIn(ExperimentalSerializationApi::class)
    val retrofitService: BilimTrackApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(BilimTrackApiService::class.java)
    }


    companion object {
        const val BASE_URL = "https://api.bilim-track.makalabox.com/api/"
        const val DOMAIN = "https://api.bilim-track.makalabox.com"

        const val CACHE_SIZE = 10 * 1024 * 1024L // 10MB
    }
}