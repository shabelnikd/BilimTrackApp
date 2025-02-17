package com.shabelnikd.bilimtrack.ui.fragments.profile

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shabelnikd.bilimtrack.model.models.MeResponse
import com.shabelnikd.bilimtrack.model.models.SubjectsMeResponse
import com.shabelnikd.bilimtrack.model.repositories.BilimTrackRepository
import com.shabelnikd.bilimtrack.utils.PreferenceHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(private val apiRepository: BilimTrackRepository) :
    ViewModel() {

    private val _profileResult = MutableSharedFlow<ProfileResult>()
    val profileResult: SharedFlow<ProfileResult> = _profileResult.asSharedFlow()

    private val _subjectsResult = MutableSharedFlow<SubjectsResult>()
    val subjectsResult: SharedFlow<SubjectsResult> = _subjectsResult.asSharedFlow()


    fun getUserMeData(sharedPreferences: PreferenceHelper) {
        viewModelScope.launch {
            val response = apiRepository.getUserMeInfo()

            response.onSuccess { response ->
                if (response.isSuccessful) {
                    val responseProfile = response.body()
                    if (responseProfile != null) {
                        _profileResult.emit(ProfileResult.Success(responseProfile))
                    } else {
                        _profileResult.emit(ProfileResult.Error("Ошибка получения данных"))

                    }
                } else {
                    if (response.code() == 401) {
                        val refresh = sharedPreferences.refreshToken
                        refresh?.let {
                            val refreshResponse = apiRepository.userRefresh(it)
                            refreshResponse.onSuccess {
                                sharedPreferences.accessToken = it.body()?.access
                                getUserMeData(sharedPreferences)
                            }.onFailure {
                                sharedPreferences.accessToken = null
                                return@launch _profileResult.emit(ProfileResult.Error("401"))
                            }
                        }
                    } else {
                        val errorBody = response.errorBody()?.string()
                        _profileResult.emit(ProfileResult.Error(errorBody ?: "Ошибка получения данных"))
                    }

                    }
            }.onFailure { t ->
                _profileResult.emit(ProfileResult.Error(t.message ?: "Ошибка получения данных"))
            }
        }
    }


    fun getSubjectsMeData() {
        viewModelScope.launch {
            val response = apiRepository.getUserSubjects()
            response.onSuccess { response ->
                if (response.isSuccessful) {
                    val responseSubjects = response.body()
                    if (responseSubjects != null) {
                        _subjectsResult.emit(SubjectsResult.Success(responseSubjects))
                    } else {
                        _subjectsResult.emit(SubjectsResult.Error("Ошибка получения данных"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _subjectsResult.emit(SubjectsResult.Error(errorBody ?: "Ошибка получения данных"))
                }
            }.onFailure { t ->
                _subjectsResult.emit(SubjectsResult.Error(t.message ?: "Ошибка получения данных"))
            }

        }
    }

    sealed class ProfileResult {
        data class Success(val me: MeResponse) : ProfileResult()
        data class Error(val errorMessage: String) : ProfileResult()
    }

    sealed class SubjectsResult {
        data class Success(val me: List<SubjectsMeResponse>) : SubjectsResult()
        data class Error(val errorMessage: String) : SubjectsResult()
    }
}