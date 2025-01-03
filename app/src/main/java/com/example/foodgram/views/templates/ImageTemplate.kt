package com.example.foodgram.views.templates

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.foodgram.R
import com.example.foodgram.models.TagModel
import com.example.foodgram.ui.theme.Orange80
import com.example.foodgram.utils.Const
import com.example.foodgram.viewmodels.PostViewModel
import com.example.foodgram.viewmodels.UserViewModel
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream

// Template Image Logo App untuk di Halaman Login Register dan Start
@Composable
fun ApplicationImage(
    resource: Int,
    contentDescription: String,
    modifier: Modifier,
    contentScale: ContentScale
) {
    Image(
        painter = painterResource(id = resource),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale
    )
}

// Template Image di Top Bar Setiap View Selain Login Register dan Start
@Composable
fun TopBarImage() {
    Image(
        painter = painterResource(id = R.drawable.foodgram_text_logo),
        contentDescription = "Top Bar Image",
        modifier = Modifier
            .height(60.dp),
        contentScale = ContentScale.Fit
    )
}

// Template Image Profile User di ProfileView
@Composable
fun ProfileImage(imageUrl: String) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(Const.BASE_URL + imageUrl)
            .placeholder(R.drawable.profile_placeholder)
            .crossfade(true)
            .build(),
        contentDescription = "Profile Image",
        modifier = Modifier
            .height(150.dp)
            .clip(RoundedCornerShape(100.dp)),
        contentScale = ContentScale.Fit
    )
}

// Template Image Profile User di Post Card di Home dan Post Detail
@Composable
fun PostProfileImage(imageUrl: String) {
    AsyncImage(
        model = "http://10.0.2.2:3000/${imageUrl}",
        contentDescription = "Post Profile Image",
        placeholder = painterResource(id = R.drawable.start_people_image), // Fallback image
        modifier = Modifier
            .height(50.dp)
            .aspectRatio(1f)
            .clip(RoundedCornerShape(100.dp)),
        contentScale = ContentScale.FillWidth
    )
}

// Template Image Post di Post Card
@Composable
fun PostImage(cornerRadius: Dp, imageUrl: String, onClickAction: () -> Unit) {
    AsyncImage(
        model = "http://10.0.2.2:3000/${imageUrl}",
        contentDescription = "Post Image",
        placeholder = painterResource(id = R.drawable.start_people_image), // Fallback image
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable {
                onClickAction()
            }
    )
}

// Template Image Tag di LibraryView
@Composable
fun TagImage(
    tag: TagModel,
    cornerRadius: Dp,
    onClickAction: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable {
                onClickAction()
            }
    ) {
        AsyncImage(
            model = "http://10.0.2.2:3000/images/${tag.name.lowercase()}.jpg",
            contentDescription = "Tag Image",
            placeholder = painterResource(id = R.drawable.start_people_image),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Orange80)
                    )
                )
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(8.dp)
        ) {
            Text(
                text = tag.name,
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

// Template Image Picker di Form Update User Profile (SettingsView)
@Composable
fun SquareProfileImagePicker(
    context: Context,
    modifier: Modifier = Modifier,
    userViewModel: UserViewModel,
) {
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val file = uriToFile(context, uri)
            userViewModel.saveSelectedImage(file)
            userViewModel.checkUpdateUserForm()
        }
    }

    val selectedFile = userViewModel.selectedImageFile.collectAsState().value

    Box(
        modifier = modifier
            .size(160.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(160.dp)
                .clickable {
                    imagePicker.launch("image/*")
                }
                .clip(RoundedCornerShape(14.dp))
                .background(Orange80.copy(alpha = 0.2f)),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Orange80),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Image",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp),
                    tint = Color.White
                )
            }
        }

        if (selectedFile != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(selectedFile.toUri())
                    .placeholder(R.drawable.profile_placeholder)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// Template Image Picker di Form Create Post
@Composable
fun SquarePostImagePicker(
    context: Context,
    modifier: Modifier = Modifier,
    postViewModel: PostViewModel,
) {
    val imagePicker = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val file = uriToFile(context, uri)
            postViewModel.saveSelectedImage(file)
            postViewModel.checkCreatePostForm()
        }
    }

    val selectedFile = postViewModel.selectedImageFile.collectAsState().value

    Box(
        modifier = modifier
            .size(160.dp)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(160.dp)
                .clickable {
                    imagePicker.launch("image/*")
                }
                .clip(RoundedCornerShape(14.dp))
                .background(Orange80.copy(alpha = 0.2f)),
        ) {
            Box(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(120.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Orange80),
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Image",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(30.dp),
                    tint = Color.White
                )
            }
        }

        if (selectedFile != null) {
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(selectedFile.toUri())
                    .placeholder(R.drawable.profile_placeholder)
                    .crossfade(true)
                    .build(),
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )
        }
    }
}

// Function untuk Convert URI to File
fun uriToFile(context: Context, uri: Uri): File {
    val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
    val tempFile = File(context.cacheDir, "uploaded_image.jpg")
    val outputStream: OutputStream = FileOutputStream(tempFile)

    inputStream?.copyTo(outputStream)

    return tempFile
}