package com.example.foodgram.views

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.foodgram.models.TagModel
import com.example.foodgram.uiStates.CreatePostStatusUIState
import com.example.foodgram.viewmodels.PostViewModel
import com.example.foodgram.views.templates.CircleLoadingTemplate
import com.example.foodgram.views.templates.DefaultTextField
import com.example.foodgram.views.templates.FormButton
import com.example.foodgram.views.templates.PageTitleLabel
import com.example.foodgram.views.templates.SectionTitleLabel
import com.example.foodgram.views.templates.SquarePostImagePicker
import com.example.foodgram.views.templates.TagCheckbox
import com.example.foodgram.views.templates.TopBarImage

@Composable
fun CreatePostView(
    postViewModel: PostViewModel,
    token: String,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    context: Context
) {
    val createPostUIState by postViewModel.formUIState.collectAsState()
    val createPostStatus = postViewModel.createPostStatus

    val focusManager = LocalFocusManager.current

    LaunchedEffect(createPostStatus) {
        if (createPostStatus is CreatePostStatusUIState.Failed) {
            Toast.makeText(context, "DATA ERROR: ${createPostStatus.errorMessage}", Toast.LENGTH_SHORT).show()
            postViewModel.clearDataErrorMessage()
        } else if (createPostStatus is CreatePostStatusUIState.Success) {
            Toast.makeText(context, "Successfully Created Post", Toast.LENGTH_SHORT).show()

            postViewModel.resetViewModel()
        }
    }

    if (createPostStatus is CreatePostStatusUIState.Loading) {
        Column {
            CircleLoadingTemplate()
        }
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            content = {
                item(span = { GridItemSpan(2) }) {
                    Column {
                        TopBarImage()

                        PageTitleLabel("Upload Post")

                        Spacer(modifier = Modifier.height(20.dp))

                        SectionTitleLabel("Title")

                        Spacer(modifier = Modifier.height(10.dp))

                        DefaultTextField(
                            inputValue = postViewModel.titleInput,
                            onInputValueChange = {
                                postViewModel.changeTitleInput(it)
                                postViewModel.checkCreatePostForm()
                            },
                            modifier = Modifier
                                .fillMaxWidth(),
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

                        Spacer(modifier = Modifier.height(20.dp))

                        SectionTitleLabel("Category")

                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }

                items(TagModel.Tags) { tag ->
                    TagCheckbox(
                        tag = tag,
                        isChecked = postViewModel.selectedTags.contains(tag.tagId.toString()),
                        onCheckedChange = {
                            postViewModel.selectOrDeselectTags(tag.tagId)
                        }
                    )
                }

                item(span = { GridItemSpan(2) }) {
                    Column {
                        Spacer(modifier = Modifier.height(10.dp))
                        SectionTitleLabel("Caption")

                        Spacer(modifier = Modifier.height(10.dp))

                        DefaultTextField(
                            inputValue = postViewModel.captionInput,
                            onInputValueChange = {
                                postViewModel.changeCaptionInput(it)
                                postViewModel.checkCreatePostForm()
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            keyboardType = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            ),
                            onKeyboardNext = KeyboardActions(
                                onDone = null
                            ),
                            singleLine = false
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        SectionTitleLabel("Photo 1 x 1")

                        Spacer(modifier = Modifier.height(10.dp))

                        SquarePostImagePicker(
                            context = context,
                            postViewModel = postViewModel
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        FormButton(
                            "Post",
                            textColor = postViewModel.checkPostButtonEnabled(createPostUIState.buttonEnabled)
                        ) {
                            val error = postViewModel.checkCreatePostFormValidation()

                            if (error.isNotEmpty()) {
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            } else {
                                postViewModel.createPost(token, navController)
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        )
    }
}