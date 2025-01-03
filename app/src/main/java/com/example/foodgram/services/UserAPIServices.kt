package com.example.foodgram.services

import com.example.foodgram.models.GetUserResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Path

interface UserAPIService {
    @GET("api/users/{userId}")
    fun getUser(@Header("Authorization") token: String, @Path("userId") userId: Int): Call<GetUserResponse>

    @Multipart
    @PATCH("api/users/{userId}/update")
    fun updateUser(@Header("Authorization") token: String, @Path("userId") userId: Int, @Part username: MultipartBody.Part, @Part profile_image: MultipartBody.Part): Call<GetUserResponse>
}