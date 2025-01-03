package com.example.foodgram.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.example.foodgram.FoodGramApplication
import com.example.foodgram.models.CreatePostResponse
import com.example.foodgram.models.GetPostDetailResponse
import com.example.foodgram.repositories.IPostRepository
import com.example.foodgram.uiStates.FormUIState
import com.example.foodgram.uiStates.CreatePostStatusUIState
import com.example.foodgram.uiStates.PostDetailDataStatusUI
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class PostViewModel(
    private val postRepository: IPostRepository,
) : ViewModel() {
    private val _formUIState = MutableStateFlow(FormUIState())

    val formUIState: StateFlow<FormUIState>
        get() {
            return _formUIState.asStateFlow()
        }

    private val _selectedPost = MutableStateFlow<String>("0")

    val selectedPost: MutableStateFlow<String> get() = _selectedPost

    fun setSelectedPost(postID: String) {
        _selectedPost.value = postID
    }

    fun getSelectedPost(): String {
        return _selectedPost.value
    }

//    var createPostStatus: CreatePostStatusUIState by mutableStateOf(CreatePostStatusUIState.Start)
//        private set
//
//    var postDetailStatus: PostDetailDataStatusUI by mutableStateOf(PostDetailDataStatusUI.Start)
//        private set

    var titleInput by mutableStateOf("")
        private set

    var selectedTags by mutableStateOf<Set<String>>(emptySet())
        private set

    var captionInput by mutableStateOf("")
        private set

    private val _selectedImageFile = MutableStateFlow<File?>(null)

    val selectedImageFile: StateFlow<File?> = _selectedImageFile

    fun saveSelectedImage(file: File) {
        _selectedImageFile.value = file
    }

    fun changeTitleInput(titleInput: String) {
        this.titleInput = titleInput
    }

    fun changeCaptionInput(captionInput: String) {
        this.captionInput = captionInput
    }

    fun selectOrDeselectTags(id: Int) {
        selectedTags = if (selectedTags.contains(id.toString())) {
            selectedTags - id.toString()
        } else {
            selectedTags + id.toString()
        }
    }

    fun checkCreatePostForm() {
        if (titleInput.trim().isNotEmpty() && captionInput.trim().isNotEmpty() && selectedTags.isNotEmpty() && selectedImageFile.value != null) {
            _formUIState.update { currentState ->
                currentState.copy(
                    buttonEnabled = true
                )
            }
        } else {
            _formUIState.update { currentState ->
                currentState.copy(
                    buttonEnabled = false
                )
            }
        }
    }

    fun checkPostButtonEnabled(isEnabled: Boolean): Color {
        if (isEnabled) {
            return Color.Black
        }

        return Color.DarkGray.copy(alpha = 0.5f)
    }

    fun checkCreatePostFormValidation(): String {
        if (titleInput.count() < 3) {
            return "Title must contain atleast 3 characters"
        }

        if (captionInput.trim().count() < 3) {
            return "Caption must contain atleast 3 characters"
        }

        return ""
    }

    fun createPost(token: String, navController: NavHostController) {
        viewModelScope.launch {
            Log.d("token-create-post", "TOKEN UPLOAD POST: ${token}")

            createPostStatus = CreatePostStatusUIState.Loading

            try {
                val call = postRepository.createPost(
                    title = titleInput,
                    image = selectedImageFile.value!!,
                    caption = captionInput,
                    tags = selectedTags.toList()
                )
                call.enqueue(object : Callback<CreatePostResponse> {
                    override fun onResponse(call: Call<CreatePostResponse>, res: Response<CreatePostResponse>) {
                        if (res.isSuccessful) {
                            createPostStatus = CreatePostStatusUIState.Success(res.body()!!.data)

                            navController.popBackStack()

                            resetViewModel()

                            Log.d("data-result", "CREATE POST DATA: ${createPostStatus}")
                        } else {
                            createPostStatus = CreatePostStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<CreatePostResponse>, t: Throwable) {
                        createPostStatus = CreatePostStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                createPostStatus = CreatePostStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun getPostDetail(navController: NavHostController) {
        viewModelScope.launch {
            postDetailStatus = PostDetailDataStatusUI.Loading

            try {
                val call = postRepository.getPostDetail(selectedPost.value.toInt())
                call.enqueue(object : Callback<GetPostDetailResponse> {
                    override fun onResponse(call: Call<GetPostDetailResponse>, res: Response<GetPostDetailResponse>) {
                        if (res.isSuccessful) {
                            postDetailStatus = PostDetailDataStatusUI.Success(res.body()!!.data)

                            Log.d("data-result", "DETAIL POST DATA: ${postDetailStatus}")
                        } else {
                            postDetailStatus = PostDetailDataStatusUI.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<GetPostDetailResponse>, t: Throwable) {
                        postDetailStatus = PostDetailDataStatusUI.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                postDetailStatus = PostDetailDataStatusUI.Failed(error.localizedMessage)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FoodGramApplication)
                val postRepository = application.container.postRepository
                PostViewModel(postRepository)
            }
        }
    }

    fun resetViewModel() {
        changeTitleInput("")
        changeCaptionInput("")
        selectedTags = emptySet()
        _selectedImageFile.value = null
        createPostStatus = CreatePostStatusUIState.Start
    }

    fun clearDataErrorMessage() {
        createPostStatus = CreatePostStatusUIState.Start
    }
}