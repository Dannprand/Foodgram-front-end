package com.example.foodgram.uiStates

import com.example.foodgram.models.UserPost

sealed interface UserPostDataStatusUIState {
    data class Success(val data: List<UserPost>): UserPostDataStatusUIState
    object Start: UserPostDataStatusUIState
    object Loading: UserPostDataStatusUIState
    data class Failed(val errorMessage: String): UserPostDataStatusUIState
}