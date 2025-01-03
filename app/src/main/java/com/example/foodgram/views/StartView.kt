package com.example.foodgram.views

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsIgnoringVisibility
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.foodgram.R
import com.example.foodgram.enums.PagesEnum
import com.example.foodgram.views.templates.ApplicationImage
import com.example.foodgram.views.templates.StartButton

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StartView(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context
) {
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
            Image(
                painter = painterResource(id = R.drawable.foodgram_logo),
                contentDescription = "Example Image",
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(0.dp, 20.dp, 0.dp, 32.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StartButton(
                    buttonText = stringResource(id = R.string.loginText),
                    onButtonClick = {
                        navController.navigate(PagesEnum.Login.name)
                    },
                    buttonModifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 24.dp),
                    textModifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 15.dp),
                    buttonColor = Color.White,
                    textColor = Color.Black
                )

                StartButton(
                    buttonText = stringResource(id = R.string.registerText),
                    onButtonClick = {
                        navController.navigate(PagesEnum.Register.name)
                    },
                    buttonModifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(top = 16.dp),
                    textModifier = Modifier
                        .padding(vertical = 5.dp, horizontal = 15.dp),
                    buttonColor = Color.White.copy(alpha = 0.23f),
                    textColor = Color.White
                )
            }
        }

        ApplicationImage(
            R.drawable.start_people_image,
            "Bottom Image",
            Modifier
                .fillMaxWidth()
                .offset(y = 36.dp),
            ContentScale.Fit
        )
    }
}