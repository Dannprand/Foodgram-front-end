package com.example.foodgram.repositories

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.foodgram.services.NetworkPostServices
import com.example.foodgram.models.CommentPostResponse
import com.example.foodgram.models.CreatePostResponse
import com.example.foodgram.models.GetCommentByPostResponse
import com.example.foodgram.models.GetPostDetailResponse
import com.example.foodgram.models.GetPostListResponse
import com.example.foodgram.models.GetPostWithTagsResponse
import com.example.foodgram.models.GetUserPostResponse
import com.example.foodgram.models.LikePostResponse
import com.example.foodgram.models.RatingPostResponse
import com.example.foodgram.utils.MultipartUtils
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import okhttp3.MultipartBody
import retrofit2.Call
import java.io.File

class PostRepository(
    private val userDataStore: DataStore<Preferences>,
    private val networkPostServices: NetworkPostServices
) : IPostRepository {
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
    override suspend fun getPosts(): Call<GetPostListResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("token not found in DataStore")
        }

        return networkPostServices.getAllPost(token = "Bearer $token")
    }

    override suspend fun getPostDetail(postId: Int): Call<GetPostDetailResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("token not found in DataStore")
        }

        return networkPostServices.getPostDetail(token = "Bearer $token", postId = postId)
    }

    override suspend fun getUserPost(userID: String): Call<GetUserPostResponse> {
        var token = currentUserToken.map { it }.firstOrNull()

        if (token.isNullOrEmpty()) {
            throw IllegalStateException("token not found in DataStore")
        }

        var userId = currentUserId.map { it }.firstOrNull()

        if (userId.isNullOrEmpty()) {
            throw IllegalStateException("User id not found in DataStore")
        }

        if (userID != "") {
            return networkPostServices.getUserPost(token = "Bearer $token", userId = userID.toInt())
        } else {
            return networkPostServices.getUserPost(token = "Bearer $token", userId = userId.toInt())
        }
    }

    override suspend fun likePost(postId: Int): Call<LikePostResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("User ID not found in DataStore")
        }

        return networkPostServices.likePost(token = "Bearer $token", postId = postId)
    }

    override suspend fun unlikePost(postId: Int): Call<LikePostResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("User ID not found in DataStore")
        }

        return networkPostServices.unlikePost(token = token, postId = postId)
    }

    override suspend fun ratingPost(postId: Int, rating: Int): Call<RatingPostResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("token not found in DataStore")
        }

        var ratingMap = HashMap<String, Int>()

        ratingMap["rating"] = rating

        return networkPostServices.ratePost("Bearer $token", postId, ratingMap)
    }

    override suspend fun unratePost(postId: Int): Call<LikePostResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("User ID not found in DataStore")
        }

        return networkPostServices.unratePost(token = token, postId = postId)
    }

    override suspend fun commentPost(postId: Int, comment: String): Call<CommentPostResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("token not found in DataStore")
        }

        var commentMap = HashMap<String, String>()

        commentMap["content"] = comment

        return networkPostServices.commentPost(token = "Bearer $token", postId = postId, commentMap = commentMap)
    }

    override suspend fun getCommentsByPost(postId: Int): Call<GetCommentByPostResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("token not found in DataStore")
        }

        return networkPostServices.getCommentByPost(token = "Bearer $token", postId = postId)
    }

    override suspend fun getPostByTags(tagId: Int): Call<GetPostWithTagsResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("token not found in DataStore")
        }

        return networkPostServices.getPostByTag(token = "Bearer $token", tagId = tagId)
    }

    override suspend fun createPost(
        title: String,
        image: File,
        caption: String,
        tags: List<String>
    ): Call<CreatePostResponse> {
        var token = currentUserToken.map { it }.firstOrNull()
        if (token.isNullOrEmpty()) {
            throw IllegalStateException("token not found in DataStore")
        }


        val tagsJson = Gson().toJson(tags) // Assuming tags is List<Int>

        val titlePart = MultipartBody.Part.createFormData("title", title)
        val captionPart = MultipartBody.Part.createFormData("caption", caption)
        val tagsPart = MultipartBody.Part.createFormData("tags", tagsJson)
        var imagePart = MultipartUtils.createImageMultipart(image, "image")

        return networkPostServices.createPost(token = "Bearer $token",
            titlePart, imagePart!!, captionPart, tagsPart)
    }
}