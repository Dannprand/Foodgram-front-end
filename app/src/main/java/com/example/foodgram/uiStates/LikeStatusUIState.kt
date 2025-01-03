package com.example.foodgram.uiStates

sealed interface LikeStatusUIState {
    object Success: LikeStatusUIState
    object Start: LikeStatusUIState
    object Loading: LikeStatusUIState
    data class Failed(val errorMessage:String): LikeStatusUIState
}