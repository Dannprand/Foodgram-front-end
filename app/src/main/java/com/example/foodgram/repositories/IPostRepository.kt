package com.example.foodgram.repositories

import com.example.foodgram.models.CommentPostResponse
import com.example.foodgram.models.CreatePostResponse
import com.example.foodgram.models.GetCommentByPostResponse
import com.example.foodgram.models.GetPostDetailResponse
import com.example.foodgram.models.GetPostListResponse
import com.example.foodgram.models.GetPostWithTagsResponse
import com.example.foodgram.models.GetUserPostResponse
import com.example.foodgram.models.LikePostResponse
import com.example.foodgram.models.RatingPostResponse
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import java.io.File

interface IPostRepository {
    val currentUserToken: Flow<String>
    val currentUserId: Flow<String>

    suspend fun getPosts() : Call<GetPostListResponse>

    suspend fun getPostDetail(postId: Int) : Call<GetPostDetailResponse>

    suspend fun getUserPost(userID: String) : Call<GetUserPostResponse>

    suspend fun likePost(postId: Int) : Call<LikePostResponse>

    suspend fun unlikePost(postId: Int) : Call<LikePostResponse>

    suspend fun ratingPost(postId: Int, rating: Int) : Call<RatingPostResponse>

    suspend fun unratePost(postId: Int) : Call<LikePostResponse>

    suspend fun commentPost(postId: Int, comment: String) : Call<CommentPostResponse>

    suspend fun getCommentsByPost(postId: Int) : Call<GetCommentByPostResponse>

    suspend fun getPostByTags(tagId: Int) : Call<GetPostWithTagsResponse>

    suspend fun createPost(title: String, image: File, caption: String, tags: List<String>) : Call<CreatePostResponse>
}