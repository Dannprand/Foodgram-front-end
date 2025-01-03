package com.example.foodgram.models

import com.google.gson.annotations.SerializedName

// Bentuk Response API Get Post List
data class GetPostListResponse(
    val status: Int,
    val message: String,
    val data: List<PostModel>
)

// Bentuk Response API Get Post Detail
data class GetPostDetailResponse(
    val status: Int,
    val message: String,
    val data: PostModel
)

// Bentuk Response API Post
data class PostModel(
    @SerializedName("post_id") val postId: Int,
    @SerializedName("image_url") val imageUrl: String,
    val caption: String,
    val title: String,
    val user: PostUserModel,
    val ratingValue: Double,
    val ratingCount: Int,
    val tags: List<TagModel>,
    var like: Int,
    var commentCount: Int,
    @SerializedName("is_current_user_liked") var isCurrentUserLiked: Boolean,
    @SerializedName("is_current_user_rated") var isCurrentUserRated: Boolean
)

// Bentuk Response API Tag
data class TagModel(
    @SerializedName("tag_id") val tagId: Int,
    val name: String
){
    // Default Built In Tags
    companion object {
        val Tags = listOf(
            TagModel(1, "Sweet"),
            TagModel(2, "Savoury"),
            TagModel(3, "Dessert"),
            TagModel(4, "Drink"),
            TagModel(5, "Healthy")
        )
    }
}

// Bentuk Response API User Post
data class UserPost(
    @SerializedName("post_id") val postId : Int,
    @SerializedName("image_url") val imageUrl: String
)

// Bentuk Response API Get User Post
data class GetUserPostResponse(
    val status: Int,
    val message: String,
    val data: List<UserPost>
)

// Bentuk Response API Liking a Post
data class LikePostResponse(
    val status: Int,
    val message: String
)

// Bentuk Response API Rating a Post
data class RatingPostResponse(
    val status: Int,
    val message: String
)

// Bentuk Response API Commenting a Post
data class CommentPostResponse(
    val status: Int,
    val message: String
)

// Bentuk Response API Get a Post's Comment
data class GetCommentByPostResponse(
    val status: Int,
    val message: String,
    val data: List<CommentModelPost>
)

// Bentuk Model untuk Comment Post
data class CommentModelPost(
    @SerializedName("comment_id") val commentId: Int,
    val user: GetUserModel,
    val content: String
)

// Bentuk Response API Post List with Tags
data class GetPostWithTagsResponse(
    val status: Int,
    val message: String,
    val data: List<PostModel>
)

// Bentuk Response API Creating a Post
data class CreatePostResponse(
    val status: Int,
    val message: String,
    val data: CreatePostModel
)

// Bentuk Model untuk Create Post
data class CreatePostModel(
    @SerializedName("post_id") val postId: Int,
    val title: String,
    val caption: String,
    @SerializedName("image_url")val imageUrl: String,
    val userId: Int
)