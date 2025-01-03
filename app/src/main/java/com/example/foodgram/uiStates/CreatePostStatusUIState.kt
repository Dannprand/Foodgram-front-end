package com.example.foodgram.uiStates

import com.example.foodgram.models.CreatePostModel

sealed interface CreatePostStatusUIState {
    data class Success(val data: CreatePostModel): CreatePostStatusUIState
    object Start: CreatePostStatusUIState
    object Loading: CreatePostStatusUIState
    data class Failed(val errorMessage:String): CreatePostStatusUIState
}