package com.example.foodgram.views.templates

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.foodgram.models.PostModel

// Template Detail Library di LibraryDetailView
@Composable
fun DetailLibraryCard(libraryPost: PostModel) {
    Column {
        PostImage(
            cornerRadius = 10.dp,
            imageUrl = libraryPost.imageUrl
        ) { }

        Spacer(modifier = Modifier.height(12.dp))

        Text(libraryPost.title, fontWeight = FontWeight.Bold)

        Text(libraryPost.caption, textAlign = TextAlign.Justify)
    }
}