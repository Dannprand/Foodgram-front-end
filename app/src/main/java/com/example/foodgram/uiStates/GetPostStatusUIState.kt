package com.example.foodgram.uiStates

import com.example.foodgram.models.PostModel

sealed interface GetPostStatusUIState {
    data class Success(val data: List<PostModel>): GetPostStatusUIState
    object Start: GetPostStatusUIState
    object Loading: GetPostStatusUIState
    data class Failed(val errorMessage:String): GetPostStatusUIState
}