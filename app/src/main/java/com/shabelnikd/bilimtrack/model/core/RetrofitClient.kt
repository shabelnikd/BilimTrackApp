package com.shabelnikd.bilimtrack.model.core

import android.content.Context
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.shabelnikd.bilimtrack.model.service.Authenticated
import com.shabelnikd.bilimtrack.model.service.BilimTrackApiService
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Invocation
import retrofit2.Retrofit
import java.io.File

class RetrofitClient private constructor(context: Context) {
    companion object {
        private const val BASE_URL = "https://api.bilim-track.makalabox.com/api/"

        const val CACHE_SIZE = 10 * 1024 * 1024L // 10MB

        private var INSTANCE: RetrofitClient? = null

        fun getInstance(context: Context): RetrofitClient {
            return INSTANCE ?: synchronized(this) {
                val instance = RetrofitClient(context)
                INSTANCE = instance
                instance
            }
        }

    }


    val httpCacheDirectory = File(context.cacheDir, "responses")
    val cache = Cache(httpCacheDirectory, CACHE_SIZE)

    val sharedPreferences = PreferenceHelper()

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val apiKeyInterceptor = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()

            val method = originalRequest.tag(Invocation::class.java)?.method()
            val isAuthRequired = method?.getAnnotation(Authenticated::class.java) != null

            sharedPreferences.initialize(context)
            val accessToken = sharedPreferences.accessToken

            val newRequest =
                if (isAuthRequired && accessToken != null)
                    originalRequest.newBuilder()
                        .addHeader("Authorization", "Bearer $accessToken")
                        .build()
                else originalRequest

            return chain.proceed(newRequest)
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
}