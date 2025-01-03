package com.example.foodgram.models

import com.google.gson.annotations.SerializedName





// Bentuk Response API Get Post Detail
data class GetPostDetailResponse(
    val status: Int,
    val message: String,
    val data: PostModel
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
