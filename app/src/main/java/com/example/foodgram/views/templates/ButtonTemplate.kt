package com.example.foodgram.views.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.foodgram.ui.theme.Orange80
import com.example.foodgram.uiStates.AuthenticationStatusUIState

// Template Button di Login Register
@Composable
fun AuthenticationButton(
    buttonText: String,
    onButtonClick: () -> Unit,
    buttonModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    buttonEnabled: Boolean,
    buttonColor: Color,
    textColor: Color,
    userDataStatusUIState: AuthenticationStatusUIState,
    loadingBarModifier: Modifier = Modifier
) {
    when(userDataStatusUIState) {
        is AuthenticationStatusUIState.Loading -> CircleLoadingTemplate(
            modifier = loadingBarModifier,
            color = Orange80,
            trackColor = Color.Transparent
        )
        else -> Button(
            onClick = onButtonClick,
            modifier = buttonModifier,
            colors = ButtonDefaults.buttonColors(buttonColor),
            enabled = buttonEnabled
        ) {
            Text(
                text = buttonText,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = textModifier,
                color = textColor
            )
        }
    }
}

// Template Button di Halaman Start (Halaman Paling Awal Sebelum Login Register)
@Composable
fun StartButton(
    buttonText: String,
    onButtonClick: () -> Unit,
    buttonModifier: Modifier = Modifier,
    textModifier: Modifier = Modifier,
    buttonColor: Color,
    textColor: Color
) {
    Button(
        onClick = onButtonClick,
        modifier = buttonModifier,
        colors = ButtonDefaults.buttonColors(buttonColor)
    ) {
        Text(
            text = buttonText,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = textModifier,
            color = textColor
        )
    }
}

// Template Button Settings (Icon)
@Composable
fun SettingsButton(
    onButtonClick: () -> Unit,
    iconModifier: Modifier = Modifier,
    iconColor: Color,
) {
    IconButton(
        onClick = onButtonClick
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            modifier = iconModifier,
            contentDescription = "Settings Button",
            tint = iconColor
        )
    }
}

// Template Button Logout (Icon)
@Composable
fun LogoutButton(
    onButtonClick: () -> Unit,
    iconModifier: Modifier = Modifier,
    iconColor: Color,
) {
    IconButton(
        onClick = onButtonClick
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Default.ExitToApp,
            modifier = iconModifier,
            contentDescription = "Logout Button",
            tint = iconColor
        )
    }
}

// Template Button untuk Submit Form
@Composable
fun FormButton(
    buttonText: String,
    textColor: Color,
    onButtonClick: () -> Unit,
) {
    Button(
        onClick = onButtonClick,
        modifier = Modifier,
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp,
            pressedElevation = 4.dp,
            disabledElevation = 4.dp,
            hoveredElevation = 4.dp,
            focusedElevation = 4.dp
        ),
        shape = RoundedCornerShape(8.dp),
        colors = ButtonDefaults.buttonColors(Color.White)
    ) {
        Text(
            "" + buttonText,
            color = textColor,
            modifier = Modifier
                .padding(0.dp, 2.dp, 0.dp, 0.dp),
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}