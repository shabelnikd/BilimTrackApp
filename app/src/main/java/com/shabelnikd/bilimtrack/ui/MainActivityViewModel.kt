package com.shabelnikd.bilimtrack.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shabelnikd.bilimtrack.model.repositories.BilimTrackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(private val apiRepository: BilimTrackRepository) : ViewModel() {


    private val _refreshResult = MutableSharedFlow<RefreshResult>()
    val refreshResult: SharedFlow<RefreshResult> = _refreshResult.asSharedFlow()

    fun userRefresh(refresh: String) {
        viewModelScope.launch {
            val response = apiRepository.userRefresh(refresh)

            response.onSuccess { response ->
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        _refreshResult.emit(
                            RefreshResult.Success(
                                authResponse.access
                            )
                        )
                    } else {
                        _refreshResult.emit(RefreshResult.Error("Ошибка получения данных"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _refreshResult.emit(RefreshResult.Error(errorBody ?: "Неверный логин или пароль"))
                }
            }.onFailure { t ->
                _refreshResult.emit(RefreshResult.Error(t.message ?: "Ошибка входа"))
            }
        }
    }


    sealed class RefreshResult {
        data class Success(val accessToken: String) : RefreshResult()
        data class Error(val errorMessage: String) : RefreshResult()
    }


}