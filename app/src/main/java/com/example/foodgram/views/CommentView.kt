package com.example.foodgram.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.example.foodgram.models.CommentModelPost
import com.example.foodgram.models.GetUserModel
import com.example.foodgram.views.templates.PostProfileImage

@Composable
fun CommentView(data: CommentModelPost) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        PostProfileImage(imageUrl = data.user.profileImage)
        Column(horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                text = data.user.username,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = data.content,
                textAlign = TextAlign.Justify
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CommentModalViewPreview(){
    CommentView(data = CommentModelPost(commentId = 1, user = GetUserModel(username = "Brownies", profileImage = "images/user.png"), content = "Makanannya enak banget"))
}