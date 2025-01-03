package com.example.foodgram.viewmodels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import com.example.foodgram.FoodGramApplication
import com.example.foodgram.models.GetUserPostResponse
import com.example.foodgram.models.GetUserResponse
import com.example.foodgram.repositories.IPostRepository
import com.example.foodgram.repositories.UserRepository
import com.example.foodgram.enums.PagesEnum
import com.example.foodgram.uiStates.FormUIState
import com.example.foodgram.uiStates.UserDataStatusUIState
import com.example.foodgram.uiStates.UserPostDataStatusUIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class UserViewModel(
    private val userRepository: UserRepository,
    private val postRepository: IPostRepository,
) : ViewModel() {
    private val _formUIState = MutableStateFlow(FormUIState())

    val formUIState: StateFlow<FormUIState>
        get() {
            return _formUIState.asStateFlow()
        }

    val userID: StateFlow<String> = userRepository.currentUserId.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )

    val token: StateFlow<String> = userRepository.currentUserToken.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = ""
    )

    private val _selectedUserID = MutableStateFlow("0")

    private val selectedUserID: MutableStateFlow<String> get() = _selectedUserID

    fun setSelectedUserID(selectedUserID: Int, navController: NavHostController) {
        _selectedUserID.value = selectedUserID.toString()

        navController.navigate(PagesEnum.Profile.name)
    }

    fun setSelectedUserIDToOwner(navController: NavHostController) {
        _selectedUserID.value = userID.value

        navController.navigate(PagesEnum.Profile.name)
    }

    var dataStatus: UserDataStatusUIState by mutableStateOf(UserDataStatusUIState.Start)
        private set

    var userPostDataStatus: UserPostDataStatusUIState by mutableStateOf(UserPostDataStatusUIState.Start)
        private set

    var usernameInput by mutableStateOf("")
        private set

    private val _selectedImageFile = MutableStateFlow<File?>(null)

    val selectedImageFile: StateFlow<File?> = _selectedImageFile

    fun saveSelectedImage(file: File) {
        _selectedImageFile.value = file
    }

    fun changeUsernameInput(usernameInput: String) {
        this.usernameInput = usernameInput
    }

    fun checkUpdateUserForm() {
        if (usernameInput.trim().count() >= 3 && selectedImageFile.value != null) {
            _formUIState.update { currentState ->
                currentState.copy(
                    buttonEnabled = true
                )
            }
        } else {
            _formUIState.update { currentState ->
                currentState.copy(
                    buttonEnabled = false
                )
            }
        }
    }

    fun checkSaveButtonEnabled(isEnabled: Boolean): Color {
        if (isEnabled) {
            return Color.Black
        }

        return Color.DarkGray.copy(alpha = 0.5f)
    }

    fun checkIsCurrentUserOrNot(): Boolean {
        return selectedUserID.value == userID.value
    }

    fun logoutUser(navController: NavHostController) {
        viewModelScope.launch {
            saveUserIDToken("Unknown", "Unknown") {
                navController.navigate(PagesEnum.Start.name) {
                    popUpTo(PagesEnum.Home.name) {
                        inclusive = true
                    }
                }
            }
        }
    }

    fun updateUserProfile(token: String, navController: NavHostController) {
        viewModelScope.launch {
            Log.d("token-home", "TOKEN AT HOME: ${token}")

            dataStatus = UserDataStatusUIState.Loading

            try {
                val call = userRepository.updateUserProfile(username = usernameInput, profileImage = selectedImageFile.value!!)
                call.enqueue(object : Callback<GetUserResponse> {
                    override fun onResponse(call: Call<GetUserResponse>, res: Response<GetUserResponse>) {
                        if (res.isSuccessful) {
                            dataStatus = UserDataStatusUIState.Success(res.body()!!.data)

                            navController.popBackStack()

                            Log.d("data-result", "UPDATE PROFILE DATA: ${dataStatus}")
                        } else {
                            dataStatus = UserDataStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                        dataStatus = UserDataStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                dataStatus = UserDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            dataStatus = UserDataStatusUIState.Loading

            try {
                val call = userRepository.getUserProfile(_selectedUserID.value)
                call.enqueue(object : Callback<GetUserResponse> {
                    override fun onResponse(call: Call<GetUserResponse>, res: Response<GetUserResponse>) {
                        if (res.isSuccessful) {
                            dataStatus = UserDataStatusUIState.Success(res.body()!!.data)

                            Log.d("data-result", "USER PROFILE DATA: ${dataStatus}")
                        } else {
                            dataStatus = UserDataStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<GetUserResponse>, t: Throwable) {
                        dataStatus = UserDataStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                dataStatus = UserDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    fun getUserPost() {
        viewModelScope.launch {
            userPostDataStatus = UserPostDataStatusUIState.Loading

            try {
                val call = postRepository.getUserPost(selectedUserID.value)
                call.enqueue(object : Callback<GetUserPostResponse> {
                    override fun onResponse(call: Call<GetUserPostResponse>, res: Response<GetUserPostResponse>) {
                        if (res.isSuccessful) {
                            userPostDataStatus = UserPostDataStatusUIState.Success(res.body()!!.data)

                            Log.d("data-result", "USER POST DATA: ${userPostDataStatus}")
                        } else {
                            dataStatus = UserDataStatusUIState.Failed(res.message())
                        }
                    }

                    override fun onFailure(call: Call<GetUserPostResponse>, t: Throwable) {
                        userPostDataStatus = UserPostDataStatusUIState.Failed(t.localizedMessage)
                    }

                })
            } catch (error: IOException) {
                userPostDataStatus = UserPostDataStatusUIState.Failed(error.localizedMessage)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as FoodGramApplication)
                val userRepository = application.container.userRepository
                val postRepository = application.container.postRepository
                UserViewModel(userRepository, postRepository)
            }
        }
    }

    fun saveUserIDToken(token: String, userID: String, afterAction:() -> Unit) {
        viewModelScope.launch {
            userRepository.saveUserID(userID)
            userRepository.saveUserToken(token)

            afterAction.invoke()
        }
    }

    fun resetViewModel() {
        changeUsernameInput("")
        _selectedImageFile.value = null
    }

    fun clearDataErrorMessage() {
        dataStatus = UserDataStatusUIState.Start
    }

    fun clearUserPostDataErrorMessage() {
        userPostDataStatus = UserPostDataStatusUIState.Start
    }
}