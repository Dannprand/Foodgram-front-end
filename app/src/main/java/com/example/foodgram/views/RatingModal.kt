package com.example.foodgram.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.foodgram.R
import com.example.foodgram.views.templates.FormButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RatingModal(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSubmitRating: (Int) -> Unit
) {
    // State to control the rating
    var rating by remember { mutableStateOf(0) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rate this item",
                style = MaterialTheme.typography.titleLarge
            )

            // Row for stars
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                (1..5).forEach { index ->
                    val iconRes = if (index <= rating) R.drawable.ic_star else R.drawable.ic_star_outline
                    Image(
                        painter = painterResource(iconRes),
                        contentDescription = "Star $index",
                        modifier = Modifier
                            .size(40.dp)
                            .clickable { rating = index }
                    )
                }
            }

            FormButton(buttonText = "Rate Recipe", textColor = Color.Black) {
                onSubmitRating(rating)
            }
        }
    }
}
