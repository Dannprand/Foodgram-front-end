package com.example.foodgram.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.foodgram.services.UserAPIService
import com.example.foodgram.models.GetUserResponse
import com.example.foodgram.utils.MultipartUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import retrofit2.Call
import java.io.File

interface UserRepository {
    val currentUserToken: Flow<String>
    val currentUserId: Flow<String>

    suspend fun saveUserToken(token: String)

    suspend fun saveUserID(userID: String)

    suspend fun getUserProfile(userID: String): Call<GetUserResponse>

    suspend fun updateUserProfile(username: String, profileImage: File): Call<GetUserResponse>
}

class NetworkUserRepository(
    private val userDataStore: DataStore<Preferences>,
    private val userAPIService: UserAPIService
): UserRepository {
    private companion object {
        val USER_TOKEN = stringPreferencesKey("token")
        val USER_ID = stringPreferencesKey("userId")
    }

    override val currentUserToken: Flow<String> = userDataStore.data.map { preferences ->
        preferences[USER_TOKEN] ?: "Unknown"
    }

    override val currentUserId: Flow<String> = userDataStore.data.map { preferences ->
        preferences[USER_ID] ?: "Unknown"
    }

    override suspend fun saveUserToken(token: String) {
        userDataStore.edit { preferences ->
            preferences[USER_TOKEN] = token
        }
    }

    override suspend fun saveUserID(userID: String) {
        userDataStore.edit { preferences ->
            preferences[USER_ID] = userID
        }
    }

    override suspend fun getUserProfile(userID: String): Call<GetUserResponse> {
        var token = currentUserToken.map { it }.firstOrNull()

        val userId = currentUserId.map { it }.firstOrNull()

        if (userId.isNullOrEmpty()) {
            throw IllegalStateException("User ID not found in DataStore")
        }

        if (userID != "") {
            return userAPIService.getUser("Bearer $token", userID.toInt())
        } else {
            return userAPIService.getUser("Bearer $token", userId.toInt())
        }
    }

    override suspend fun updateUserProfile(
        username: String,
        profileImage: File
    ): Call<GetUserResponse> {
        val token = currentUserToken.map { it }.firstOrNull()

        if (token.isNullOrEmpty()) {
            throw IllegalStateException("User ID not found in DataStore")
        }

        val userId = currentUserId.map { it }.firstOrNull()

        // Check if userId exists; throw an exception or handle the case if it's missing
        if (userId.isNullOrEmpty()) {
            throw IllegalStateException("User ID not found in DataStore")
        }

        val usernamePart = MultipartBody.Part.createFormData("username", username)

        var imagePart = MultipartUtils.createImageMultipart(profileImage, partName = "profile_image")

        return userAPIService.updateUser(token = "Bearer $token", userId = userId.toInt(), username = usernamePart, profile_image = imagePart!!)
    }
}