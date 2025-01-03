package com.example.foodgram.models

import com.google.gson.annotations.SerializedName

// Bentuk Response API User
data class UserResponse(
    val status: Int,
    val message: String,
    val data: UserModel
)

// Bentuk Model User
data class UserModel (
    val user_id: Int,
    val token: String?
)

// Bentuk Response API Get User
data class GetUserResponse(
    val status: Int,
    val message: String,
    val data: GetUserModel
)

// Bentuk Model Response API Get User
data class GetUserModel(
    val username: String,
    @SerializedName("profile_image") val profileImage: String
)

// Bentuk Response API User (Creator) of a Post
data class PostUserModel(
    val username: String,
    @SerializedName("profile_image") val profileImage: String,
    @SerializedName("user_id") val userId: Int
)