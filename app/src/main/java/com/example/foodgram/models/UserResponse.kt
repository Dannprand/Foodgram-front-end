package com.example.foodgram.models

import com.google.gson.annotations.SerializedName

// Bentuk Response API User (Creator) of a Post
data class PostUserModel(
    val username: String,
    @SerializedName("profile_image") val profileImage: String,
    @SerializedName("user_id") val userId: Int
)