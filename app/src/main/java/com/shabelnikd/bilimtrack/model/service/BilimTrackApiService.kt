package com.shabelnikd.bilimtrack.model.service

import com.shabelnikd.bilimtrack.model.models.MeResponse
import com.shabelnikd.bilimtrack.model.models.RatingGroupsResponse
import com.shabelnikd.bilimtrack.model.models.RatingUsersResponse
import com.shabelnikd.bilimtrack.model.models.SubjectsMeResponse
import com.shabelnikd.bilimtrack.model.models.TokenCreateResponse
import com.shabelnikd.bilimtrack.model.models.TokenRefreshResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface BilimTrackApiService {
    @FormUrlEncoded
    @POST("jwt/create/")
    suspend fun userLogin(
        @Field("username") userName: String,
        @Field("password") password: String
    ): Response<TokenCreateResponse>

    @FormUrlEncoded
    @POST("jwt/refresh/")
    suspend fun userRefresh(
        @Field("refresh") refresh: String,
    ): Response<TokenRefreshResponse>

    @GET("subjects/me/")
    @Authenticated
    @Headers("Cache-Control: public, max-age=600")
    suspend fun getUserSubjects(): Response<List<SubjectsMeResponse>>

    @GET("users/me/")
    @Authenticated
    @Headers("Cache-Control: public, max-age=600")
    suspend fun getUserMeInfo(): Response<MeResponse>

    @GET("rating/users/")
    @Headers("Cache-Control: public, max-age=600")
    suspend fun getRatingUsers(): Response<List<RatingUsersResponse>>

    @GET("rating/groups/")
    @Headers("Cache-Control: public, max-age=600")
    suspend fun getRatingGroups(): Response<List<RatingGroupsResponse>>


}

