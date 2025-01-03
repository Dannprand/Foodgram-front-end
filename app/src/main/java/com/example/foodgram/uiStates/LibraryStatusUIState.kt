package com.example.foodgram.uiStates

import com.example.foodgram.models.PostModel

sealed interface LibraryStatusUIState {
    data class Success(val data: List<PostModel>): LibraryStatusUIState
    object Start: LibraryStatusUIState
    object Loading: LibraryStatusUIState
    data class Failed(val errorMessage:String): LibraryStatusUIState
}