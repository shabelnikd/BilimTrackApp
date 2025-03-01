package com.shabelnikd.bilimtrack.model.repositories

import com.shabelnikd.bilimtrack.model.models.AchievementsResponse
import com.shabelnikd.bilimtrack.model.models.RatingGroupsResponse
import com.shabelnikd.bilimtrack.model.models.RatingUsersResponse
import com.shabelnikd.bilimtrack.model.models.SubjectsMeResponse
import com.shabelnikd.bilimtrack.model.models.TokenCreateResponse
import com.shabelnikd.bilimtrack.model.models.TokenRefreshResponse
import com.shabelnikd.bilimtrack.model.models.UserResponse
import com.shabelnikd.bilimtrack.model.service.BilimTrackApiService
import com.shabelnikd.bilimtrack.utils.safeApiCall
import org.koin.core.component.KoinComponent
import retrofit2.Response

class BilimTrackRepository(private val apiService: BilimTrackApiService) : KoinComponent {
    suspend fun userLogin(
        userName: String,
        password: String
    ): Result<Response<TokenCreateResponse>> =
        safeApiCall({ apiService.userLogin(userName, password) }, "Error fetching user login")

    suspend fun userRefresh(refresh: String): Result<Response<TokenRefreshResponse>> =
        safeApiCall({ apiService.userRefresh(refresh) }, "Error fetching refresh token")

    suspend fun getUserSubjects(): Result<Response<List<SubjectsMeResponse>>> =
        safeApiCall({ apiService.getUserSubjects() }, "Error fetching user subjects")

    suspend fun getUserMeInfo(username: String): Result<Response<UserResponse>> =
        safeApiCall({ apiService.getUserMeInfo(username) }, "Error fetching user me info")

    suspend fun getUserMeAchievements(): Result<Response<List<AchievementsResponse>>> =
        safeApiCall(
            { apiService.getUserMeAchievements() },
            "Error fetching user achievements me info"
        )

    suspend fun getRatingUsers(): Result<Response<List<RatingUsersResponse>>> =
        safeApiCall({ apiService.getRatingUsers() }, "Error fetching users rating")

    suspend fun getRatingGroups(): Result<Response<List<RatingGroupsResponse>>> =
        safeApiCall({ apiService.getRatingGroups() }, "Error fetching groups rating")

}