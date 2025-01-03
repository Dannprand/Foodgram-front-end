package com.example.foodgram.uiStates

import com.example.foodgram.models.UserModel

sealed interface AuthenticationStatusUIState {
    data class Success(val userModelData: UserModel): AuthenticationStatusUIState
    object Loading: AuthenticationStatusUIState
    object Start: AuthenticationStatusUIState
    data class Failed(val errorMessage: String): AuthenticationStatusUIState
}