package com.example.foodgram.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.foodgram.R
import com.example.foodgram.ui.theme.FoodGramTheme
import com.example.foodgram.uiStates.AuthenticationStatusUIState
import com.example.foodgram.viewmodels.AuthenticationViewModel
import com.example.foodgram.views.templates.ApplicationImage
import com.example.foodgram.views.templates.AuthenticationButton
import com.example.foodgram.views.templates.AuthenticationLabel
import com.example.foodgram.views.templates.AuthenticationTextField
import com.example.foodgram.views.templates.PasswordTextField

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RegisterView(
    authenticationViewModel: AuthenticationViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context
) {
    val registerUIState by authenticationViewModel.authenticationUIState.collectAsState()

    val focusManager = LocalFocusManager.current

    LaunchedEffect(authenticationViewModel.dataStatus) {
        val dataStatus = authenticationViewModel.dataStatus
        if (dataStatus is AuthenticationStatusUIState.Failed) {
            Toast.makeText(context, dataStatus.errorMessage, Toast.LENGTH_SHORT).show()
            authenticationViewModel.clearErrorMessage()
        }
    }

    LaunchedEffect(true) {
        authenticationViewModel.resetViewModel()
    }

    Column(
        modifier = modifier
            .padding(WindowInsets.systemBarsIgnoringVisibility.asPaddingValues())
            .verticalScroll(rememberScrollState())
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ApplicationImage(
                R.drawable.foodgram_logo,
                contentDescription = "Example Image",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(0.dp, 20.dp, 0.dp, 32.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AuthenticationLabel(
                    "Email",
                    Modifier
                        .fillMaxWidth(0.9f)
                        .padding(0.dp, 4.dp, 0.dp, 8.dp),
                    Color.White,
                    TextAlign.Start,
                    20.sp,
                    FontWeight.SemiBold
                )

                AuthenticationTextField(
                    inputValue = authenticationViewModel.emailInput,
                    onInputValueChange = {
                        authenticationViewModel.changeEmailInput(it)
                        authenticationViewModel.checkRegisterForm()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    keyboardType = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    onKeyboardNext = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )

                Spacer(modifier = Modifier.padding(5.dp))

                AuthenticationLabel(
                    "Username",
                    Modifier
                        .fillMaxWidth(0.9f)
                        .padding(0.dp, 4.dp, 0.dp, 8.dp),
                    Color.White,
                    TextAlign.Start,
                    20.sp,
                    FontWeight.SemiBold
                )

                AuthenticationTextField(
                    inputValue = authenticationViewModel.usernameInput,
                    onInputValueChange = {
                        authenticationViewModel.changeUsernameInput(it)
                        authenticationViewModel.checkRegisterForm()
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    keyboardType = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    onKeyboardNext = KeyboardActions(
                        onNext = {
                            focusManager.moveFocus(FocusDirection.Down)
                        }
                    )
                )

                Spacer(modifier = Modifier.padding(5.dp))

                AuthenticationLabel(
                    "Password",
                    Modifier
                        .fillMaxWidth(0.9f)
                        .padding(0.dp, 4.dp, 0.dp, 8.dp),
                    Color.White,
                    TextAlign.Start,
                    20.sp,
                    FontWeight.SemiBold
                )

                PasswordTextField(
                    passwordInput = authenticationViewModel.passwordInput,
                    onPasswordInputValueChange = {
                        authenticationViewModel.changePasswordInput(it)
                        authenticationViewModel.checkRegisterForm()
                    },
                    passwordVisibilityIcon = painterResource(id = registerUIState.passwordVisibilityIcon),
                    onTrailingIconClick = {
                        authenticationViewModel.changePasswordVisibility()
                    },
                    passwordVisibility = registerUIState.passwordVisibility,
                    modifier = Modifier
                        .fillMaxWidth(0.9f),
                    keyboardImeAction = ImeAction.None,
                    onKeyboardNext = KeyboardActions(
                        onDone = null
                    )
                )

                AuthenticationButton(
                    buttonText = stringResource(id = R.string.registerText),
                    onButtonClick = {
                        val error = authenticationViewModel.checkRegisterFormValidation()

                        if (error.isNotEmpty()) {
                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        } else {
                            authenticationViewModel.registerUser(navController = navController)
                        }
                    },
                    buttonModifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 24.dp),
                    textModifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 15.dp),
                    buttonEnabled = registerUIState.buttonEnabled,
                    buttonColor = Color.White.copy(alpha = 0.23f),
                    textColor = authenticationViewModel.checkButtonEnabled(registerUIState.buttonEnabled),
                    userDataStatusUIState = authenticationViewModel.dataStatus,
                    loadingBarModifier = Modifier
                        .padding(top = 30.dp)
                        .size(40.dp)
                )
            }
        }

        ApplicationImage(
            R.drawable.login_register_foods_image,
            "Bottom Image",
            Modifier
                .fillMaxWidth()
                .offset(y = 20.dp),
            ContentScale.Fit
        )
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true
)
@Composable
fun RegisterViewPreview() {
    FoodGramTheme {
        RegisterView(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            authenticationViewModel = viewModel(),
            navController = rememberNavController(),
            context = LocalContext.current
        )
    }
}