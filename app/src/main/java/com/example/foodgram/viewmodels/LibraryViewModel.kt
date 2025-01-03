package com.example.foodgram.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.foodgram.FoodGramApplication
import com.example.foodgram.models.GetPostWithTagsResponse
import com.example.foodgram.repositories.IPostRepository
import com.example.foodgram.uiStates.LibraryStatusUIState
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class LibraryViewModel(
    private val postRepository: IPostRepository,
) : ViewModel() {
    var libraryStatus: LibraryStatusUIState by mutableStateOf(LibraryStatusUIState.Start)
        private set

    var selectedTagID by mutableIntStateOf(0)
        private set

    fun changeSelectedTagID(tagID: Int) {
        selectedTagID = tagID
    }

    fun getLibraryPosts(token: String) {
        viewModelScope.launch {
            Log.d("token-create-post", "TOKEN UPLOAD POST: ${token}")

            libraryStatus = LibraryStatusUIState.Loading

            try {
                val call = postRepository.getPostByTags(selectedTagID)
                call.enqueue(object : Callback<GetPostWithTagsResponse> {
                    override fun onResponse(call: Call<GetPostWithTagsResponse>, res: Response<GetPostWithTagsResponse>) {
                        if (res.isSuccessful) {
                            libraryStatus = LibraryStatusUIState.Success(
                                res.body()!!.data.filter { it.user.username == "foodgram" }
                            )

                            Log.d("data-result", "LIBRARY POST DATA: ${libraryStatus}")
                        } else {
                            libraryStatus = LibraryStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<GetPostWithTagsResponse>, t: Throwable) {
                        libraryStatus = LibraryStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                libraryStatus = LibraryStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FoodGramApplication)
                val postRepository = application.container.postRepository
                LibraryViewModel(postRepository = postRepository)
            }
        }
    }
}