package com.example.foodgram.views

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.foodgram.uiStates.GetCommentListUIState
import com.example.foodgram.uiStates.GetPostStatusUIState
import com.example.foodgram.uiStates.RateStatusUIState
import com.example.foodgram.viewmodels.HomeViewModel
import com.example.foodgram.viewmodels.PostViewModel
import com.example.foodgram.viewmodels.UserViewModel
import com.example.foodgram.views.templates.HomePostTemplate
import com.example.foodgram.views.templates.TagListScrollAble
import com.example.foodgram.views.templates.TopBarImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(
    homeViewModel : HomeViewModel,
    postViewModel: PostViewModel,
    userViewModel: UserViewModel,
    navController: NavHostController
){
    val context = LocalContext.current
    val getPostStatus = homeViewModel.getPostListStatus
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showCommentModal by remember { mutableStateOf(false) }
    var showRateModal by remember { mutableStateOf(false) }
    var selectedPostId by remember { mutableStateOf(0) }
    var getCommentUIState = homeViewModel.getCommentUIState
    var ratingUIState = homeViewModel.ratingStatusUIState
    val lazyListState = rememberLazyListState() //for saving the scroll state

    LaunchedEffect(Unit) {
        homeViewModel.getPostList() // Fetch posts when the view appears
    }

    LaunchedEffect(getPostStatus) {
        if (getPostStatus is GetPostStatusUIState.Failed) {
            Toast.makeText(context, getPostStatus.errorMessage, Toast.LENGTH_SHORT).show()
            homeViewModel.clearGetPostListErrorMessage()
        }
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
        } else {

        }
    }

    when(getPostStatus){
        is GetPostStatusUIState.Success -> {
            Column(
                modifier = Modifier
                    .padding(20.dp, 40.dp)
            ) {
                TopBarImage()
                Spacer(modifier = Modifier.height(24.dp))
                Column {
                    Text(
                        text = "For Your Page",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TagListScrollAble(){ tagId ->
                        homeViewModel.getPostListByTag(tagId)
                    }
                }
                Spacer(modifier = Modifier.height(44.dp))
                LazyColumn(state = lazyListState, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(getPostStatus.data){ post ->
                        HomePostTemplate(post = post,
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
                            }
                        )
                    }
                }
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
                RatingModal(sheetState = sheetState,
                    onDismiss = {
                        showRateModal = false
                    }) { ratingValue ->
                    homeViewModel.ratingPost(postId = selectedPostId, rateValue =  ratingValue)
                }
            }
        }
        else -> {
            Column(modifier = Modifier.fillMaxSize()){
                Text(text = "No Data Found")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeViewPreview(){
    HomeView(
        homeViewModel =  viewModel(factory = HomeViewModel.Factory),
        postViewModel = viewModel(factory = PostViewModel.Factory),
        userViewModel = viewModel(factory = UserViewModel.Factory),
        navController = NavHostController(LocalContext.current))
}
