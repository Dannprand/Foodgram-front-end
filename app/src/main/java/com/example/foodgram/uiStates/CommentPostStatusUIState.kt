package com.example.foodgram.uiStates

sealed interface CommentPostStatusUIState {
    object Success: CommentPostStatusUIState
    object Start: CommentPostStatusUIState
    object Loading: CommentPostStatusUIState
    data class Failed(val errorMessage:String): CommentPostStatusUIState
}