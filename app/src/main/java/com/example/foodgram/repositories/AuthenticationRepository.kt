package com.example.foodgram.repositories

import com.example.foodgram.services.AuthenticationAPIService
import com.example.foodgram.models.UserResponse
import retrofit2.Call

interface AuthenticationRepository {
    fun register(username: String, email: String, password: String): Call<UserResponse>

    fun login(username: String, password: String): Call<UserResponse>
}

class NetworkAuthenticationRepository(
    private val authenticationAPIService: AuthenticationAPIService
): AuthenticationRepository {
    // Repository Register
    override fun register(username: String, email: String, password: String): Call<UserResponse> {
        var registerMap = HashMap<String, String>()

        registerMap["username"] = username
        registerMap["email"] = email
        registerMap["password"] = password

        return authenticationAPIService.register(registerMap)
    }

    override fun login(username: String, password: String): Call<UserResponse> {
        var loginMap = HashMap<String, String>()

        loginMap["username"] = username
        loginMap["password"] = password

        return authenticationAPIService.login(loginMap)
    }
}