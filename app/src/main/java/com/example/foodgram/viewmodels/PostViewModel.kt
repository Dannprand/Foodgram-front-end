package com.example.foodgram.viewmodels

import androidx.lifecycle.ViewModel
import com.example.foodgram.repositories.IPostRepository
import com.example.foodgram.uiStates.FormUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PostViewModel(
    private val postRepository: IPostRepository,
) : ViewModel(){

    private val _formUIState = MutableStateFlow(FormUIState())
    val formUIState: StateFlow<FormUIState>

        get() {
            return _formUIState.asStateFlow()
        }


}