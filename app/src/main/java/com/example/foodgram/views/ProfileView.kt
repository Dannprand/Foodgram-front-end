package com.example.foodgram.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.foodgram.enums.PagesEnum
import com.example.foodgram.ui.theme.Orange80
import com.example.foodgram.uiStates.UserDataStatusUIState
import com.example.foodgram.uiStates.UserPostDataStatusUIState
import com.example.foodgram.viewmodels.PostViewModel
import com.example.foodgram.viewmodels.UserViewModel
import com.example.foodgram.views.templates.CircleLoadingTemplate
import com.example.foodgram.views.templates.PageTitleLabel
import com.example.foodgram.views.templates.PostImage
import com.example.foodgram.views.templates.ProfileImage
import com.example.foodgram.views.templates.SectionTitleLabel
import com.example.foodgram.views.templates.SettingsButton
import com.example.foodgram.views.templates.TopBarImage

@Composable
fun ProfileView(
    userViewModel: UserViewModel,
    postViewModel: PostViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context
) {
    val dataStatus = userViewModel.dataStatus
    val userPostDataStatus = userViewModel.userPostDataStatus
    val currentUserID = userViewModel.userID.collectAsState()

    LaunchedEffect(dataStatus) {
        if (dataStatus is UserDataStatusUIState.Failed) {
            Toast.makeText(context, "DATA ERROR: ${dataStatus.errorMessage}", Toast.LENGTH_SHORT).show()
            userViewModel.clearDataErrorMessage()
        }
    }

    LaunchedEffect(userPostDataStatus) {
        if (userPostDataStatus is UserPostDataStatusUIState.Failed) {
            Toast.makeText(context, "DATA ERROR: ${userPostDataStatus.errorMessage}", Toast.LENGTH_SHORT).show()
            userViewModel.clearUserPostDataErrorMessage()
        }
    }

    LaunchedEffect(currentUserID) {
        if (currentUserID.value != "Unknown") {
            userViewModel.getUserProfile()
            userViewModel.getUserPost()
        }
    }

    Column(
        modifier = modifier
    ) {
        when (userPostDataStatus) {
            is UserPostDataStatusUIState.Success -> if (userPostDataStatus.data.isNotEmpty()) {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    content = {
                        item(span = { GridItemSpan(2) }) {
                            Column {
                                TopBarImage()

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                ) {
                                    PageTitleLabel("Profile")

                                    if (userViewModel.checkIsCurrentUserOrNot()) {
                                        SettingsButton(
                                            onButtonClick = {
                                                navController.navigate(PagesEnum.Settings.name)
                                            },
                                            iconModifier = Modifier
                                                .size(32.dp)
                                                .padding(0.dp, 4.dp, 0.dp, 0.dp),
                                            iconColor = Color.Black
                                        )
                                    }
                                }

                                when (dataStatus) {
                                    is UserDataStatusUIState.Success ->
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

                                    is UserDataStatusUIState.Failed -> Column(
                                        modifier = Modifier
                                            .fillMaxSize(),
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
                                            color = Orange80,
                                            trackColor = Color.Transparent,
                                        )
                                    }
                                }

                                SectionTitleLabel("Post")
                            }
                        }

                        items(userPostDataStatus.data) { postData ->
                            PostImage(
                                cornerRadius = 10.dp,
                                imageUrl = postData.imageUrl
                            ) {
                                postViewModel.setSelectedPost(postData.postId.toString())

                                navController.navigate(PagesEnum.PostDetail.name)
                            }
                        }
                    }
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    content = {
                        item(span = { GridItemSpan(2) }) {
                            Column {
                                TopBarImage()

                                when (dataStatus) {
                                    is UserDataStatusUIState.Success ->
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
                                            color = Orange80,
                                            trackColor = Color.Transparent,
                                        )
                                    }
                                }

                                SectionTitleLabel("Post")
                            }
                        }

                        item(span = { GridItemSpan(2) }) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(0.dp, 50.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "No Post Found!",
                                    fontSize = 21.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                )
            }
            is UserPostDataStatusUIState.Failed -> Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "No Post Found!",
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
                    color = Orange80,
                    trackColor = Color.Transparent,
                )
            }
        }
    }
}