package com.litgo.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.litgo.data.models.Login
import com.litgo.data.repositories.UserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
//    private val _uiState = MutableStateFlow(UserUiState())
//    val uiState: StateFlow<UserUiState> = _uiState.asStateFlow()

    private val _userState = MutableLiveData<UserUiState>()
    val userState: LiveData<UserUiState> = _userState

    private var fetchJob: Job? = null

    fun login(login: Login) {
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            // Attempt to log in the user
            val loginResult = userRepository.loginUser(login)
            // TODO: Figure out the return type of loginResult
//            if (loginResult == true) {
//                // Save result of attempted login to ui state for update
//                _userState.value =
//                    UserUiState(loggedIn = true) // TODO: update the user ui state with user info (fetched from result?)
//            }
        }
    }
}