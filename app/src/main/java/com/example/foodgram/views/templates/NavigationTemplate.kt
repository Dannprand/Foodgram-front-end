package com.example.foodgram.views.templates

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.example.foodgram.enums.PagesEnum
import com.example.foodgram.ui.theme.Orange80

// Navigation Bar yang Custom
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun CustomBottomNavigation(
    currentScreenRoute:String,
    onItemSelected:(BottomNavScreen)->Unit
) {
    val items = BottomNavScreen.Items.list

    Surface(
        shape = RoundedCornerShape(50.dp),
        modifier = Modifier
            .padding(20.dp, 0.dp, 20.dp, 16.dp)
    ) {
        Row(
            modifier = Modifier
                .background(color = Orange80)
                .padding(6.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                CustomBottomNavigationItem(item = item, isSelected = item.route == currentScreenRoute) {
                    onItemSelected(item)
                }
            }
        }
    }
}

// Navigation Bar Component
@ExperimentalAnimationApi
@Composable
fun CustomBottomNavigationItem(item: BottomNavScreen, isSelected: Boolean, onClick:() -> Unit){
    val background = if (isSelected) Color.White.copy(alpha = 0.2f) else Color.Transparent

    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = onClick)
    ){
        Row(
            modifier= Modifier
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            Icon(
                imageVector = item.icon,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(32.dp)
            )

        }
    }
}

// Class untuk Item Navigation Bar
sealed class BottomNavScreen(val route: String, val icon: ImageVector, val title: String) {
    object Library : BottomNavScreen(PagesEnum.Library.name, Icons.Default.Edit, "Library")
    object Home : BottomNavScreen(PagesEnum.Home.name, Icons.Default.Home, "Home")
    object UploadPost : BottomNavScreen(PagesEnum.CreatePost.name, Icons.Default.Add, "Upload")
    object Profile : BottomNavScreen(PagesEnum.Profile.name, Icons.Default.Person, "Profile")

    object Items {
        val list = listOf(Home, UploadPost, Profile, Library)
    }
}