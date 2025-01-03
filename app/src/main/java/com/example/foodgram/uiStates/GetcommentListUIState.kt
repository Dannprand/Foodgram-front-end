package com.example.foodgram.uiStates

import com.example.foodgram.models.CommentModelPost

sealed interface GetCommentListUIState {
    data class Success(val data: List<CommentModelPost>): GetCommentListUIState
    object Start: GetCommentListUIState
    object Loading: GetCommentListUIState
    data class Failed(val errorMessage:String): GetCommentListUIState
}