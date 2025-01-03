package com.example.foodgram.uiStates

import com.example.foodgram.models.GetUserModel

sealed interface UserDataStatusUIState {
    data class Success(val data: GetUserModel): UserDataStatusUIState
    object Start: UserDataStatusUIState
    object Loading: UserDataStatusUIState
    data class Failed(val errorMessage: String): UserDataStatusUIState
}