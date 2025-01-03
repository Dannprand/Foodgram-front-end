package com.example.foodgram.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.foodgram.models.TagModel
import com.example.foodgram.enums.PagesEnum
import com.example.foodgram.viewmodels.LibraryViewModel
import com.example.foodgram.views.templates.PageTitleLabel
import com.example.foodgram.views.templates.TagImage
import com.example.foodgram.views.templates.TopBarImage

@Composable
fun LibraryView(
    libraryViewModel: LibraryViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController
) {
    Column(
        modifier = modifier
    ) {
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

                        PageTitleLabel("Library Of Recipes")
                    }
                }


            }
        )
    }
}