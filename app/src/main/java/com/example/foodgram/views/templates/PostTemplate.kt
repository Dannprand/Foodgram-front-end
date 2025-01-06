package com.example.foodgram.views.templates

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.foodgram.R
import com.example.foodgram.models.PostModel
import com.example.foodgram.models.PostUserModel
import com.example.foodgram.models.TagModel
import com.example.foodgram.enums.PagesEnum
import com.example.foodgram.uiStates.CommentPostStatusUIState
import com.example.foodgram.uiStates.LikeStatusUIState
import com.example.foodgram.uiStates.RateStatusUIState
import com.example.foodgram.viewmodels.HomeViewModel
import com.example.foodgram.viewmodels.PostViewModel
import com.example.foodgram.viewmodels.UserViewModel

// Template Post Card di HomeView
@Composable
fun HomePostTemplate(
    post: PostModel,
    homeViewModel: HomeViewModel,
    postViewModel: PostViewModel,
    userViewModel: UserViewModel,
    navController: NavHostController,
    showComment: (Int) -> Unit,
    showRateModal: (Int) -> Unit,
    isPostDetailClickable: Boolean = true
){
    var showCommentField by remember { mutableStateOf(false) }
    var textFieldValue by remember { mutableStateOf("") }
    val commentStatus = homeViewModel.commentPostListStatus
    val context = LocalContext.current
    val likeStatus = homeViewModel.likeStatusUIState
    val rateStatus = homeViewModel.ratingStatusUIState

    LaunchedEffect(commentStatus) {
        if (commentStatus is CommentPostStatusUIState.Success) {
            showCommentField = !showCommentField
            homeViewModel.clearCommentErrorMessage()
            homeViewModel.getPostList()
            postViewModel.getPostDetail(navController)
        } else if (commentStatus is CommentPostStatusUIState.Failed){
            Toast.makeText(context, commentStatus.errorMessage, Toast.LENGTH_SHORT).show()
            homeViewModel.clearCommentErrorMessage()
        }
    }

    LaunchedEffect(likeStatus) {
        if (likeStatus is LikeStatusUIState.Success) {
            homeViewModel.clearLikeErrorMessage()
            homeViewModel.getPostList()
            postViewModel.getPostDetail(navController)
        } else if (likeStatus is LikeStatusUIState.Failed){
            Toast.makeText(context, likeStatus.errorMessage, Toast.LENGTH_SHORT).show()
            homeViewModel.clearLikeErrorMessage()
        }
    }

    LaunchedEffect(rateStatus) {
        if (rateStatus is RateStatusUIState.Success) {
            homeViewModel.clearRateErrorMessage()
            homeViewModel.getPostList()
            postViewModel.getPostDetail(navController)
        } else if (rateStatus is RateStatusUIState.Failed){
            Toast.makeText(context, rateStatus.errorMessage, Toast.LENGTH_SHORT).show()
            homeViewModel.clearRateErrorMessage()
        }
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable {
                    userViewModel.setSelectedUserID(post.user.userId, navController)
                }
        ) {
            PostProfileImage("${post.user.profileImage}")
            Spacer(modifier = Modifier.width(12.dp))
            Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(8.dp)){
                Text(
                    text = "${post.user.username}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    items(post.tags){ tag ->
                        TagChip(tag = tag)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        PostImage(cornerRadius = 20.dp, imageUrl = "${post.imageUrl}"){
            if (isPostDetailClickable) {
                postViewModel.setSelectedPost(post.postId.toString())

                navController.navigate(PagesEnum.PostDetail.name)
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically){
            Text("${post.title}", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.weight(1f))
            Text("${post.ratingValue}", modifier = Modifier.padding(end = 4.dp))
            if(post.isCurrentUserRated){
                Image(painter = painterResource(R.drawable.ic_star),
                    contentDescription = "rating icon",
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .clickable {
                            homeViewModel.unratingPost(post.postId)
                        }
                )
            } else {
                Image(painter = painterResource(R.drawable.ic_star_outline),
                    contentDescription = "rating icon",
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .clickable{
                            showRateModal(post.postId)
                        }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("${post.like}", modifier = Modifier.padding(end = 4.dp))
            if (post.isCurrentUserLiked) {
                Image(painter = painterResource(R.drawable.ic_heart_filled),
                    contentDescription = "like icon",
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .clickable{
                            homeViewModel.toogleLikePost(post.postId, isLiked = true)
                        }
                )
            } else {
                Image(painter = painterResource(R.drawable.ic_heart),
                    contentDescription = "like icon",
                    modifier = Modifier
                        .width(20.dp)
                        .height(20.dp)
                        .clickable{
                            homeViewModel.toogleLikePost(post.postId, isLiked = false)
                        }
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("${post.commentCount}", modifier = Modifier.padding(end = 4.dp))
            Image(painter = painterResource(R.drawable.ic_comment),
                contentDescription = "comment icon",
                modifier = Modifier
                    .width(20.dp)
                    .height(20.dp)
                    .clickable { showCommentField = !showCommentField }
            )
        }

        Text("${post.caption}", textAlign = TextAlign.Justify, modifier = Modifier.fillMaxWidth())

        if(post.commentCount != 0){
            Text("See more", modifier = Modifier.clickable{
                homeViewModel.getComment(post.postId)
                showComment(post.postId)
            })
        }

        if (showCommentField) {
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                DefaultTextField(
                    inputValue = textFieldValue,
                    onInputValueChange = { value ->
                        textFieldValue = value // Update the state
                        homeViewModel.updateComment(post.postId, value)
                    },
                    modifier = Modifier.weight(1f).padding(end = 16.dp),
                    keyboardType = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    onKeyboardNext = KeyboardActions(
                        onDone = null
                    )
                )
                Box(modifier = Modifier.clickable{
                    if(homeViewModel.getCommentValue(post.postId) != ""){
                        homeViewModel.commentPost(post.postId, comment = textFieldValue)
                        showCommentField = !showCommentField
                    }
                }){
                    Image(painter = painterResource(R.drawable.ic_paperplane), contentDescription = "send Button", modifier = Modifier
                        .height(20.dp)
                        .aspectRatio(1f)
                    )
                }
            }
        }
    }
}
