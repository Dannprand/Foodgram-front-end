package com.example.foodgram.uiStates

import com.example.foodgram.models.PostModel

sealed interface PostDetailDataStatusUI {
    data class Success(val data: PostModel): PostDetailDataStatusUI
    object Start: PostDetailDataStatusUI
    object Loading: PostDetailDataStatusUI
    data class Failed(val errorMessage: String): PostDetailDataStatusUI
}