package com.example.foodgram

import com.example.foodgram.repositories.IPostRepository
import com.example.foodgram.repositories.PostRepository
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.foodgram.services.AuthenticationAPIService
import com.example.foodgram.services.NetworkPostServices
import com.example.foodgram.services.UserAPIService
import com.example.foodgram.repositories.AuthenticationRepository
import com.example.foodgram.repositories.NetworkAuthenticationRepository
import com.example.foodgram.repositories.NetworkUserRepository
import com.example.foodgram.repositories.UserRepository
import com.example.foodgram.utils.Const
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


interface AppContainer{
    val authenticationRepository: AuthenticationRepository
    val postRepository: IPostRepository
    val userRepository: UserRepository
}

class DefaultAppContainer(
    private val userDataStore: DataStore<Preferences>
) : AppContainer {
    private val authenticationRetrofitService: AuthenticationAPIService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(AuthenticationAPIService::class.java)
    }

    private val networkPostServices: NetworkPostServices by lazy {
        val retrofit = initRetrofit()

        retrofit.create(NetworkPostServices::class.java)
    }

    private val userRetrofitService: UserAPIService by lazy {
        val retrofit = initRetrofit()

        retrofit.create(UserAPIService::class.java)
    }

    override val authenticationRepository: AuthenticationRepository by lazy {
        NetworkAuthenticationRepository(authenticationRetrofitService)
    }

    override val postRepository: IPostRepository by lazy {
        PostRepository(userDataStore, networkPostServices)
    }

    override val userRepository: UserRepository by lazy {
        NetworkUserRepository(userDataStore, userRetrofitService)
    }

    private fun initRetrofit(): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = (HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
        client.addInterceptor(logging)

        return Retrofit
            .Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .client(client.build())
            .baseUrl(Const.BASE_URL)
            .build()
    }
}