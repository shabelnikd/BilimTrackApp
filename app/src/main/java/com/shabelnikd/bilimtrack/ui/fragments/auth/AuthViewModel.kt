package com.shabelnikd.bilimtrack.ui.fragments.auth

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
class AuthViewModel @Inject constructor(
    private val apiRepository: BilimTrackRepository
) : ViewModel() {

    private val _loginResult = MutableSharedFlow<LoginResult>()
    val loginResult: SharedFlow<LoginResult> = _loginResult.asSharedFlow()

    fun userLogIn(username: String, password: String) {
        viewModelScope.launch {
            val response = apiRepository.userLogin(username, password)

            response.onSuccess { response ->
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        _loginResult.emit(
                            LoginResult.Success(
                                authResponse.access,
                                authResponse.refresh
                            )
                        )
                    } else {
                        _loginResult.emit(LoginResult.Error("Ошибка получения данных"))
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    _loginResult.emit(LoginResult.Error(errorBody ?: "Неверный логин или пароль"))
                }
            }.onFailure { t ->
                _loginResult.emit(LoginResult.Error(t.message ?: "Ошибка входа"))
            }
        }
    }


    sealed class LoginResult {
        data class Success(val accessToken: String, val refreshToken: String) : LoginResult()
        data class Error(val errorMessage: String) : LoginResult()
    }

}