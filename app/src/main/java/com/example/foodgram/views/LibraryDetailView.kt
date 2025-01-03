package com.example.foodgram.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.foodgram.models.TagModel
import com.example.foodgram.ui.theme.Orange80
import com.example.foodgram.uiStates.LibraryStatusUIState
import com.example.foodgram.viewmodels.LibraryViewModel
import com.example.foodgram.views.templates.CircleLoadingTemplate
import com.example.foodgram.views.templates.DetailLibraryCard
import com.example.foodgram.views.templates.PageTitleLabel
import com.example.foodgram.views.templates.TopBarImage

@Composable
fun LibraryDetailView(
    libraryViewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
    token: String,
    navController: NavHostController
) {
    val libraryStatus = libraryViewModel.libraryStatus
    val pageTitle = TagModel.Tags.find { it.tagId == libraryViewModel.selectedTagID }!!.name

    LaunchedEffect(true) {
        libraryViewModel.getLibraryPosts(token = token)
    }

    LazyColumn(
        modifier = modifier
    ) {
        item {
            TopBarImage()

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .padding(0.dp, 16.dp, 0.dp, 0.dp)
                        .size(32.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )

                Spacer(modifier = Modifier.width(16.dp))

                PageTitleLabel(pageTitle)
            }
        }

        when (libraryStatus) {
            is LibraryStatusUIState.Success ->
                items(libraryStatus.data) { postData ->
                    Spacer(modifier = Modifier.height(20.dp))

                    DetailLibraryCard(postData)
                }
            is LibraryStatusUIState.Failed ->
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "No Recipes Found!",
                            fontSize = 21.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

        }
    }
}