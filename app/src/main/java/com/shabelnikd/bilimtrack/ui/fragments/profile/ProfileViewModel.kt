package com.shabelnikd.bilimtrack.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shabelnikd.bilimtrack.model.models.MeResponse
import com.shabelnikd.bilimtrack.model.models.SubjectsMeResponse
import com.shabelnikd.bilimtrack.model.repositories.BilimTrackRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ProfileViewModel() : ViewModel(), KoinComponent {

    private val apiRepository: BilimTrackRepository by inject()

    private val _profileResult = MutableSharedFlow<ProfileResult>()
    val profileResult: SharedFlow<ProfileResult> get() = _profileResult.asSharedFlow()

    private val _subjectsResult = MutableSharedFlow<SubjectsResult>()
    val subjectsResult: SharedFlow<SubjectsResult> get() = _subjectsResult.asSharedFlow()


    fun getUserMeData() {
        viewModelScope.launch {
            val response = apiRepository.getUserMeInfo()
            when {
                response.isSuccess && response.getOrNull()?.isSuccessful == true -> {
                    response.getOrNull()?.body()?.let { body ->
                        _profileResult.emit(ProfileResult.Success(body))
                    } ?: run {
                        _profileResult.emit(ProfileResult.Error("Ошибка получения профия"))
                    }
                }
                else -> _profileResult.emit(ProfileResult.Error("Ошибка запроса профия"))
            }
        }
    }


    fun getSubjectsMeData() {
        viewModelScope.launch {
            val response = apiRepository.getUserSubjects()
            when {
                response.isSuccess && response.getOrNull()?.isSuccessful == true -> {
                    response.getOrNull()?.body()?.let { body ->
                        _subjectsResult.emit(SubjectsResult.Success(body))
                    } ?: run {
                        _subjectsResult.emit(SubjectsResult.Error("Ошибка получения курсов"))
                    }
                }

                else -> _subjectsResult.emit(SubjectsResult.Error("Ошибка запроса курсов"))

            }
        }
    }

    @Serializable
    sealed class ProfileResult {
        data class Success(val me: MeResponse) : ProfileResult()
        data class Error(val errorMessage: String) : ProfileResult()
    }

    sealed class SubjectsResult {
        data class Success(val me: List<SubjectsMeResponse>) : SubjectsResult()
        data class Error(val errorMessage: String) : SubjectsResult()
    }
}