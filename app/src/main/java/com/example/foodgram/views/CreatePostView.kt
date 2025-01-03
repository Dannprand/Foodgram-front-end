package com.example.foodgram.views

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.example.foodgram.viewmodels.PostViewModel

@Composable
fun CreatePostView(
    postViewModel: PostViewModel,
    token: String,
    modifier: Modifier = Modifier
) {
    val createPostUIState by postViewModel.formUIState.collectAsState()
    val createPostStatus = postViewModel.createPostStatus

    val focusManager = LocalFocusManager.current

}