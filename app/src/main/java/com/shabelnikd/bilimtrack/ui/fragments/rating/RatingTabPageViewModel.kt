package com.shabelnikd.bilimtrack.ui.fragments.rating

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.shabelnikd.bilimtrack.model.models.RatingGroupsResponse
import com.shabelnikd.bilimtrack.model.models.RatingUsersResponse
import com.shabelnikd.bilimtrack.model.repositories.BilimTrackRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


class RatingTabPageViewModel() : ViewModel(), KoinComponent {

    private val apiRepository: BilimTrackRepository by inject()

    private val _usersRatingResponse = MutableLiveData<List<RatingUsersResponse>>()
    val usersRatingResponse: LiveData<List<RatingUsersResponse>> = _usersRatingResponse

    private val _groupsRatingResponse = MutableLiveData<List<RatingGroupsResponse>>()
    val groupsRatingResponse: LiveData<List<RatingGroupsResponse>> = _groupsRatingResponse

    suspend fun getUsersRating() {
        val response = apiRepository.getRatingUsers()
        response.onSuccess { response ->
            if (response.isSuccessful) {
                _usersRatingResponse.postValue(response.body())
            }
        }
    }

    suspend fun getGroupsRating() {
        val response = apiRepository.getRatingGroups()
        response.onSuccess { response ->
            if (response.isSuccessful) {
                _groupsRatingResponse.postValue(response.body())
            }
        }
    }


}