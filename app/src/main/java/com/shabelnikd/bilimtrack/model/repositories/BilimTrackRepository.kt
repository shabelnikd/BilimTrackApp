package com.shabelnikd.bilimtrack.model.repositories

import com.shabelnikd.bilimtrack.model.models.MeResponse
import com.shabelnikd.bilimtrack.model.models.RatingGroupsResponse
import com.shabelnikd.bilimtrack.model.models.RatingUsersResponse
import com.shabelnikd.bilimtrack.model.models.SubjectsMeResponse
import com.shabelnikd.bilimtrack.model.models.TokenCreateResponse
import com.shabelnikd.bilimtrack.model.models.TokenRefreshResponse
import com.shabelnikd.bilimtrack.model.service.BilimTrackApiService
import com.shabelnikd.bilimtrack.utils.safeApiCall
import retrofit2.Response
import javax.inject.Inject

class BilimTrackRepository @Inject constructor(private val apiService: BilimTrackApiService) {
    suspend fun userLogin(
        userName: String,
        password: String
    ): Result<Response<TokenCreateResponse>> =
        safeApiCall({ apiService.userLogin(userName, password) }, "Error fetching user login")

    suspend fun userRefresh(refresh: String): Result<Response<TokenRefreshResponse>> =
        safeApiCall({ apiService.userRefresh(refresh) }, "Error fetching refresh token")

    suspend fun getUserSubjects(): Result<Response<List<SubjectsMeResponse>>> =
        safeApiCall({ apiService.getUserSubjects() }, "Error fetching user subjects")

    suspend fun getUserMeInfo(): Result<Response<MeResponse>> =
        safeApiCall({ apiService.getUserMeInfo() }, "Error fetching user me info")

    suspend fun getRatingUsers(): Result<Response<List<RatingUsersResponse>>> =
        safeApiCall({ apiService.getRatingUsers() }, "Error fetching users rating")

    suspend fun getRatingGroups(): Result<Response<List<RatingGroupsResponse>>> =
        safeApiCall({ apiService.getRatingGroups()}, "Error fetching groups rating")

}