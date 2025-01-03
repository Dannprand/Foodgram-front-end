package com.example.foodgram.views.templates

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// Template TextField di Form Login Register
@Composable
fun AuthenticationTextField(
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardOptions,
    onKeyboardNext: KeyboardActions
) {
    TextField(
        value = inputValue,
        onValueChange = onInputValueChange,
        singleLine = true,
        modifier = modifier
            .background(Color.White, RoundedCornerShape(size = 50.dp)),
        shape = RoundedCornerShape(size = 50.dp),
        keyboardOptions = keyboardType,
        keyboardActions = onKeyboardNext,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        )
    )
}

// Template TextField Password di Login Register
@Composable
fun PasswordTextField(
    passwordInput: String,
    onPasswordInputValueChange: (String) -> Unit,
    passwordVisibilityIcon: Painter,
    onTrailingIconClick: () -> Unit,
    passwordVisibility: VisualTransformation,
    modifier: Modifier = Modifier,
    onKeyboardNext: KeyboardActions,
    keyboardImeAction: ImeAction
) {
    TextField(
        value = passwordInput,
        onValueChange = onPasswordInputValueChange,
        singleLine = true,
        trailingIcon = {
            Image(
                painter = passwordVisibilityIcon,
                contentDescription = null,
                modifier = Modifier
                    .clickable {
                        onTrailingIconClick()
                    }
            )
        },
        visualTransformation = passwordVisibility,
        modifier = modifier,
        shape = RoundedCornerShape(size = 50.dp),
        keyboardActions = onKeyboardNext,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = keyboardImeAction
        ),
    )
}

// Template TextField di Form Create Post dan Settings
@Composable
fun DefaultTextField(
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardOptions,
    onKeyboardNext: KeyboardActions,
    singleLine: Boolean = true
) {
    TextField(
        value = inputValue,
        onValueChange = onInputValueChange,
        singleLine = singleLine,
        modifier = modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray, RoundedCornerShape(size = 16.dp)),
        shape = RoundedCornerShape(size = 16.dp),
        keyboardOptions = keyboardType,
        keyboardActions = onKeyboardNext,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
    )
}