package com.example.foodgram.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.foodgram.uiStates.CreatePostStatusUIState
import com.example.foodgram.uiStates.UserDataStatusUIState
import com.example.foodgram.viewmodels.UserViewModel
import com.example.foodgram.views.templates.CircleLoadingTemplate
import com.example.foodgram.views.templates.DefaultTextField
import com.example.foodgram.views.templates.FormButton
import com.example.foodgram.views.templates.LogoutButton
import com.example.foodgram.views.templates.PageTitleLabel
import com.example.foodgram.views.templates.ProfileImage
import com.example.foodgram.views.templates.SectionTitleLabel
import com.example.foodgram.views.templates.SquareProfileImagePicker
import com.example.foodgram.views.templates.TopBarImage

@Composable
fun SettingsView(
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    token: String,
    context: Context
) {
    val updateProfileUIState by userViewModel.formUIState.collectAsState()

    val focusManager = LocalFocusManager.current
    val dataStatus = userViewModel.dataStatus

    LaunchedEffect(dataStatus) {
        if (dataStatus is UserDataStatusUIState.Failed) {
            Toast.makeText(context, "DATA ERROR: ${dataStatus.errorMessage}", Toast.LENGTH_SHORT).show()
            userViewModel.clearDataErrorMessage()
        }
    }

    LaunchedEffect(true) {
        userViewModel.resetViewModel()

        if (dataStatus is UserDataStatusUIState.Success) {
            userViewModel.changeUsernameInput(dataStatus.data.username)
        }
    }

    if (dataStatus is UserDataStatusUIState.Loading) {
        Column {
            CircleLoadingTemplate()
        }
    } else {
        Column(
            modifier = modifier
                .verticalScroll(rememberScrollState())
        ) {
            TopBarImage()

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                PageTitleLabel("Setting")

                LogoutButton(
                    onButtonClick = {
                        userViewModel.logoutUser(navController = navController)
                    },
                    iconModifier = Modifier
                        .size(32.dp)
                        .padding(0.dp, 4.dp, 0.dp, 0.dp),
                    iconColor = Color.Red
                )
            }

            when (dataStatus) {
                is UserDataStatusUIState.Success ->
                    Column {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 20.dp)
                        ) {
                            ProfileImage(dataStatus.data.profileImage)

                            Spacer(modifier = Modifier.height(8.dp))

                            SectionTitleLabel(dataStatus.data.username)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        SectionTitleLabel("Change Username")

                        Spacer(modifier = Modifier.height(10.dp))

                        DefaultTextField(
                            inputValue = userViewModel.usernameInput,
                            onInputValueChange = {
                                userViewModel.changeUsernameInput(it)
                                userViewModel.checkUpdateUserForm()
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
                            keyboardType = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            onKeyboardNext = KeyboardActions(
                                onDone = null
                            )
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        SectionTitleLabel("Change Profile")

                        Spacer(modifier = Modifier.height(10.dp))

                        SquareProfileImagePicker(
                            context = context,
                            userViewModel = userViewModel
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        FormButton(
                            "Save",
                            textColor = userViewModel.checkSaveButtonEnabled(updateProfileUIState.buttonEnabled)
                        ) {
                            userViewModel.updateUserProfile(
                                token = token,
                                navController = navController
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }

                is UserDataStatusUIState.Failed -> Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(0.dp, 50.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No User Found!",
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                else -> Column(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircleLoadingTemplate(
                        color = Color.White,
                        trackColor = Color.Transparent,
                    )
                }
            }
        }
    }
}