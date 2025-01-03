package com.example.foodgram.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.foodgram.FoodGramApplication
import com.example.foodgram.models.CommentPostResponse
import com.example.foodgram.models.GetCommentByPostResponse
import com.example.foodgram.models.GetPostListResponse
import com.example.foodgram.models.GetPostWithTagsResponse
import com.example.foodgram.models.LikePostResponse
import com.example.foodgram.models.RatingPostResponse
import com.example.foodgram.repositories.IPostRepository
import com.example.foodgram.uiStates.CommentPostStatusUIState
import com.example.foodgram.uiStates.GetCommentListUIState
import com.example.foodgram.uiStates.GetPostStatusUIState
import com.example.foodgram.uiStates.LikeStatusUIState
import com.example.foodgram.uiStates.RateStatusUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException


class HomeViewModel(
    private val postRepository : IPostRepository
) : ViewModel() {
    // State to store comments for each post by postId
    private val _comments = MutableStateFlow<Map<Int, String>>(emptyMap())
    val comments: StateFlow<Map<Int, String>> = _comments

    var getPostListStatus: GetPostStatusUIState by mutableStateOf(GetPostStatusUIState.Start)
        private set

    var commentPostListStatus: CommentPostStatusUIState by mutableStateOf(CommentPostStatusUIState.Start)
        private set

    var likeStatusUIState: LikeStatusUIState by mutableStateOf(LikeStatusUIState.Start)
        private set

    var ratingStatusUIState: RateStatusUIState by mutableStateOf(RateStatusUIState.Start)
        private set

    var getCommentUIState: GetCommentListUIState by mutableStateOf(GetCommentListUIState.Start)
        private set

    fun getPostList() {
        viewModelScope.launch {
            getPostListStatus = GetPostStatusUIState.Loading

            try {
                val call = postRepository.getPosts()
                call.enqueue(object : Callback<GetPostListResponse> {
                    override fun onResponse(call: Call<GetPostListResponse>, res: Response<GetPostListResponse>) {
                        if (res.isSuccessful) {
                            getPostListStatus = GetPostStatusUIState.Success(res.body()!!.data)

                            Log.d("data-result", "TODO LIST DATA: ${getPostListStatus}")
                        } else {
                            getPostListStatus = GetPostStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<GetPostListResponse>, t: Throwable) {
                        getPostListStatus = GetPostStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                getPostListStatus = GetPostStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun getPostListByTag(tagId: Int) {
        viewModelScope.launch {
            getPostListStatus = GetPostStatusUIState.Loading

            try {
                val call = postRepository.getPostByTags(tagId)
                call.enqueue(object : Callback<GetPostWithTagsResponse> {
                    override fun onResponse(call: Call<GetPostWithTagsResponse>, res: Response<GetPostWithTagsResponse>) {
                        if (res.isSuccessful) {
                            getPostListStatus = GetPostStatusUIState.Success(res.body()!!.data)
                        } else {
                            getPostListStatus = GetPostStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<GetPostWithTagsResponse>, t: Throwable) {
                        getPostListStatus = GetPostStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                getPostListStatus = GetPostStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun commentPost(postId: Int, comment: String){
        viewModelScope.launch {
            commentPostListStatus = CommentPostStatusUIState.Loading

            try {
                val call = postRepository.commentPost(postId = postId, comment = comment)
                call.enqueue(object : Callback<CommentPostResponse> {
                    override fun onResponse(call: Call<CommentPostResponse>, res: Response<CommentPostResponse>) {
                        if (res.isSuccessful) {
                            commentPostListStatus = CommentPostStatusUIState.Success
                        } else {
                            commentPostListStatus = CommentPostStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<CommentPostResponse>, t: Throwable) {
                        commentPostListStatus = CommentPostStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                commentPostListStatus = CommentPostStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    // Function to update the comment for a specific post
    fun updateComment(postId: Int, comment: String) {
        _comments.value = _comments.value.toMutableMap().apply {
            this[postId] = comment
        }
    }

    fun getCommentValue(postId: Int) : String{
        return _comments.value[postId] ?: ""
    }

    // Function to get a comment for a specific post
    fun getComment(postId: Int) {
        viewModelScope.launch {
            getCommentUIState = GetCommentListUIState.Loading

            try {
                val call = postRepository.getCommentsByPost(postId)
                call.enqueue(object : Callback<GetCommentByPostResponse> {
                    override fun onResponse(call: Call<GetCommentByPostResponse>, res: Response<GetCommentByPostResponse>) {
                        if (res.isSuccessful) {
                            getCommentUIState = GetCommentListUIState.Success(res.body()!!.data)
                        } else {
                            getCommentUIState = GetCommentListUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<GetCommentByPostResponse>, t: Throwable) {
                        getCommentUIState = GetCommentListUIState.Failed(t.localizedMessage)
                    }
                })
            } catch (error: IOException) {
                getCommentUIState = GetCommentListUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun toogleLikePost(postId: Int, isLiked: Boolean){
        if(isLiked){
            unlikePost(postId)
        } else {
            likePost(postId)
        }
    }

    fun likePost(postId: Int){
        viewModelScope.launch {
            likeStatusUIState = LikeStatusUIState.Loading

            try {
                val call = postRepository.likePost(postId)
                call.enqueue(object : Callback<LikePostResponse> {
                    override fun onResponse(call: Call<LikePostResponse>, res: Response<LikePostResponse>) {
                        if (res.isSuccessful) {
                            likeStatusUIState = LikeStatusUIState.Success

                            Log.d("data-result", "LIKE POST DATA: ${getPostListStatus}")
                        } else {
                            likeStatusUIState = LikeStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<LikePostResponse>, t: Throwable) {
                        likeStatusUIState = LikeStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                likeStatusUIState = LikeStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun unlikePost(postId: Int){
        viewModelScope.launch {
            likeStatusUIState = LikeStatusUIState.Loading

            try {
                val call = postRepository.unlikePost(postId)
                call.enqueue(object : Callback<LikePostResponse> {
                    override fun onResponse(call: Call<LikePostResponse>, res: Response<LikePostResponse>) {
                        if (res.isSuccessful) {
                            likeStatusUIState = LikeStatusUIState.Success
                        } else {
                            likeStatusUIState = LikeStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<LikePostResponse>, t: Throwable) {
                        likeStatusUIState = LikeStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                likeStatusUIState = LikeStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun toogleRatePost(postId: Int, rateValue: Int?, isRated: Boolean){
        if(isRated){
            unratingPost(postId)
        } else {
            if (rateValue != null) {
                ratingPost(postId, rateValue)
            }
        }
    }

    fun ratingPost(postId: Int, rateValue: Int){
        viewModelScope.launch {
            ratingStatusUIState = RateStatusUIState.Loading
            try {
                val call = postRepository.ratingPost(postId, rating = rateValue)
                call.enqueue(object : Callback<RatingPostResponse> {
                    override fun onResponse(call: Call<RatingPostResponse>, res: Response<RatingPostResponse>) {
                        if (res.isSuccessful) {
                            ratingStatusUIState = RateStatusUIState.Success
                        } else {
                            ratingStatusUIState = RateStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<RatingPostResponse>, t: Throwable) {
                        ratingStatusUIState = RateStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                ratingStatusUIState = RateStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun unratingPost(postId: Int){
        viewModelScope.launch {
            ratingStatusUIState = RateStatusUIState.Loading
            try {
                val call = postRepository.unratePost(postId)
                call.enqueue(object : Callback<LikePostResponse> {
                    override fun onResponse(call: Call<LikePostResponse>, res: Response<LikePostResponse>) {
                        if (res.isSuccessful) {
                            ratingStatusUIState = RateStatusUIState.Success
                        } else {
                            ratingStatusUIState = RateStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<LikePostResponse>, t: Throwable) {
                        ratingStatusUIState = RateStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                ratingStatusUIState = RateStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun clearGetPostListErrorMessage(){
        getPostListStatus = GetPostStatusUIState.Start
    }

    fun clearGetCommentListErrorMessage(){
        getCommentUIState = GetCommentListUIState.Start
    }

    fun clearCommentErrorMessage(){
        commentPostListStatus = CommentPostStatusUIState.Start
    }

    fun clearLikeErrorMessage(){
        likeStatusUIState = LikeStatusUIState.Start
    }

    fun clearRateErrorMessage(){
        ratingStatusUIState = RateStatusUIState.Start
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FoodGramApplication)
                val postRepository = application.container.postRepository
                HomeViewModel(postRepository = postRepository)
            }
        }
    }
}