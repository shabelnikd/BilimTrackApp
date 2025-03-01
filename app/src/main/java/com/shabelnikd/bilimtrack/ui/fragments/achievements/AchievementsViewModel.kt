package com.shabelnikd.bilimtrack.ui.fragments.achievements

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shabelnikd.bilimtrack.model.models.AchievementsResponse
import com.shabelnikd.bilimtrack.model.repositories.BilimTrackRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

class AchievementsViewModel : ViewModel(), KoinComponent {

    private val apiRepository: BilimTrackRepository by inject()

    private val _achieveResult = MutableSharedFlow<AchieveResult>()
    val achieveResult: SharedFlow<AchieveResult> get() = _achieveResult.asSharedFlow()


    fun getUserMeAchievementsData() {
        viewModelScope.launch {
            val response = apiRepository.getUserMeAchievements()
            when {
                response.isSuccess && response.getOrNull()?.isSuccessful == true -> {
                    response.getOrNull()?.body()?.let { body ->
                        _achieveResult.emit(AchieveResult.Success(body))
                    } ?: run {
                        _achieveResult.emit(AchieveResult.Error("Ошибка получения достижений"))
                    }
                }

                else -> _achieveResult.emit(AchieveResult.Error("Ошибка запроса достижений"))
            }
        }
    }


    @Serializable
    sealed class AchieveResult {
        data class Success(val achievements: List<AchievementsResponse>) : AchieveResult()
        data class Error(val errorMessage: String) : AchieveResult()
    }


}