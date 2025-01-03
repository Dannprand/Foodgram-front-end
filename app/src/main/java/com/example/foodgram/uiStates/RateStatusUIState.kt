package com.example.foodgram.uiStates

sealed interface RateStatusUIState {
    object Success: RateStatusUIState
    object Start: RateStatusUIState
    object Loading: RateStatusUIState
    data class Failed(val errorMessage:String): RateStatusUIState
}