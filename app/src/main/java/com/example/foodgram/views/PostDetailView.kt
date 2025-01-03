package com.example.foodgram.views

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.foodgram.ui.theme.Orange80
import com.example.foodgram.uiStates.GetCommentListUIState
import com.example.foodgram.uiStates.PostDetailDataStatusUI
import com.example.foodgram.uiStates.RateStatusUIState
import com.example.foodgram.viewmodels.HomeViewModel
import com.example.foodgram.viewmodels.PostViewModel
import com.example.foodgram.viewmodels.UserViewModel
import com.example.foodgram.views.templates.CircleLoadingTemplate
import com.example.foodgram.views.templates.HomePostTemplate
import com.example.foodgram.views.templates.PageTitleLabel
import com.example.foodgram.views.templates.TopBarImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostDetailView(
    postViewModel: PostViewModel,
    homeViewModel: HomeViewModel,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    val dataStatus = postViewModel.postDetailStatus
    val context = LocalContext.current
    var showCommentModal by remember { mutableStateOf(false) }
    var showRateModal by remember { mutableStateOf(false) }
    var getCommentUIState = homeViewModel.getCommentUIState
    var ratingUIState = homeViewModel.ratingStatusUIState
    var selectedPostId by remember { mutableStateOf(0) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

    LaunchedEffect(true) {
        postViewModel.getPostDetail(navController)
    }

    LaunchedEffect(getCommentUIState) {
        if (getCommentUIState is GetCommentListUIState.Failed) {
            Toast.makeText(context, getCommentUIState.errorMessage, Toast.LENGTH_SHORT).show()
            homeViewModel.clearGetCommentListErrorMessage()
        }
    }

    LaunchedEffect(ratingUIState) {
        if (ratingUIState is RateStatusUIState.Failed) {
            Toast.makeText(context, ratingUIState.errorMessage, Toast.LENGTH_SHORT).show()
            homeViewModel.clearRateErrorMessage()
        } else if (ratingUIState is RateStatusUIState.Success) {
            showRateModal = false
            postViewModel.getPostDetail(navController)
        } else {

        }
    }

    when (dataStatus) {
        is PostDetailDataStatusUI.Success ->{
            Column(
                modifier = modifier
                    .verticalScroll(rememberScrollState())
            ) {
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

                    PageTitleLabel("Back")
                }

                Spacer(modifier = Modifier.height(20.dp))

                HomePostTemplate(
                    post = dataStatus.data,
                    homeViewModel = homeViewModel,
                    postViewModel = postViewModel,
                    userViewModel = userViewModel,
                    navController = navController,
                    showComment = { postId ->
                        selectedPostId = postId
                        showCommentModal = true
                    },
                    showRateModal = { postId ->
                        selectedPostId = postId
                        showRateModal = true
                    },
                    isPostDetailClickable = false
                )
            }

            if(showCommentModal){
                ModalBottomSheet(onDismissRequest = {
                    showCommentModal = false
                },
                    sheetState = sheetState
                ) {
                    when(getCommentUIState){
                        is GetCommentListUIState.Success -> {
                            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(8.dp)) {
                                items(getCommentUIState.data){ comment ->
                                    CommentView(data = comment)
                                }
                            }
                        }
                        else -> {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
                                Text("No Comment Found")
                            }
                        }
                    }
                }
            }

            if(showRateModal){
                com.example.foodgram.views.RatingModal(sheetState = sheetState,
                    onDismiss = {
                        showRateModal = false
                    }) { ratingValue ->
                    homeViewModel.ratingPost(postId = selectedPostId, rateValue = ratingValue)
                }
            }
        }

        is PostDetailDataStatusUI.Failed -> Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Post Not Found!",
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