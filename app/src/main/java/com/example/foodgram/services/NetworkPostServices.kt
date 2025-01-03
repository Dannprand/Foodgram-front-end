package com.example.foodgram.services

import com.example.foodgram.models.CommentPostResponse
import com.example.foodgram.models.CreatePostResponse
import com.example.foodgram.models.GetCommentByPostResponse
import com.example.foodgram.models.GetPostDetailResponse
import com.example.foodgram.models.GetPostListResponse
import com.example.foodgram.models.GetPostWithTagsResponse
import com.example.foodgram.models.GetUserPostResponse
import com.example.foodgram.models.LikePostResponse
import com.example.foodgram.models.RatingPostResponse
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface NetworkPostServices {
    @GET("api/posts")
    fun getAllPost(@Header("Authorization") token: String): Call<GetPostListResponse>

    @GET("api/posts/{postId}")
    fun getPostDetail(@Header("Authorization") token: String, @Path("postId") postId: Int): Call<GetPostDetailResponse>

    @GET("api/users/{userId}/posts")
    fun getUserPost(@Header("Authorization") token: String, @Path("userId") userId: Int): Call<GetUserPostResponse>

    @GET("api/posts/{postId}/likes")
    fun likePost(@Header("Authorization") token: String, @Path("postId") postId: Int): Call<LikePostResponse>

    @GET("api/posts/{postId}/likes/delete")
    fun unlikePost(@Header("Authorization") token: String, @Path("postId") postId: Int): Call<LikePostResponse>

    @POST("api/posts/{postId}/ratings")
    fun ratePost(@Header("Authorization") token: String, @Path("postId") postId: Int, @Body ratingMap: HashMap<String, Int>): Call<RatingPostResponse>

    @POST("api/posts/{postId}/ratings/delete")
    fun unratePost(@Header("Authorization") token: String, @Path("postId") postId: Int): Call<LikePostResponse>

    @POST("api/posts/{postId}/comments")
    fun commentPost(@Header("Authorization") token: String, @Path("postId") postId: Int, @Body commentMap: HashMap<String, String>): Call<CommentPostResponse>

    @GET("api/posts/{postId}/comments")
    fun getCommentByPost(@Header("Authorization") token: String, @Path("postId") postId: Int): Call<GetCommentByPostResponse>

    @GET("api/tags/{tagId}/posts")
    fun getPostByTag(@Header("Authorization") token: String, @Path("tagId") tagId: Int): Call<GetPostWithTagsResponse>

    @Multipart
    @POST("api/posts/create")
    fun createPost(@Header("Authorization") token: String, @Part title: MultipartBody.Part, @Part image: MultipartBody.Part, @Part caption: MultipartBody.Part, @Part tags: MultipartBody.Part) : Call<CreatePostResponse>
}
