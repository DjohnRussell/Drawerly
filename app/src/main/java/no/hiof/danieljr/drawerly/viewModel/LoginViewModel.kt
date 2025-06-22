package no.hiof.danieljr.drawerly.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import no.hiof.danieljr.drawerly.data.repository.AuthRepository
import no.hiof.danieljr.drawerly.data.model.User
import javax.inject.Inject

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val user: User?) : LoginState()
    data class Error(val error: String) : LoginState()
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = authRepository.login(email, password)
            _loginState.value = if (result.isSuccess) {
                LoginState.Success(result.getOrNull())
            } else {
                LoginState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
            }
        }
    }

    fun register(email: String, password: String, name: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val result = authRepository.register(email, password, name)
            _loginState.value = if (result.isSuccess) {
                LoginState.Success(result.getOrNull())
            } else {
                LoginState.Error(result.exceptionOrNull()?.message ?: "Registration failed")
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
            _loginState.value = LoginState.Idle
        }
    }
}
