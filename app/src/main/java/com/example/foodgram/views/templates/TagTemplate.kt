package com.example.foodgram.views.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.example.foodgram.models.TagModel

// Warna Berdasarkan Tag
object TagColor{
    fun getTagColor(tagId: Int) : Color {
        if(tagId == 1){
            return Color(0xFF2C7CB5)
        }else if (tagId == 2){
            return Color(0xFFFF8A00)
        }else if (tagId == 3){
            return Color(0xFF09BA10)
        }else if (tagId == 4){
            return Color(0xFF339DFF)
        }else if (tagId == 5){
            return Color(0xFFFF0000)
        }else{
            return Color(0xFFFF8A00)
        }
    }
}

// Template Tag Chip
@Composable
fun TagChip(
    tag: TagModel,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .background(
                color = TagColor.getTagColor(tag.tagId),
                shape = RoundedCornerShape(16.dp) // Rounded shape for the chip
            )
            .padding(horizontal = 12.dp, vertical = 6.dp)
            .height(IntrinsicSize.Min),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = tag.name,
            color = Color.White,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

// Template Tag List yang Scrollable di HomeView
@Composable
fun TagListScrollAble(onClick: (Int) -> Unit){
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TagChip(tag = TagModel.Tags[0], modifier = Modifier.clickable{
            onClick(TagModel.Tags[0].tagId)
        })
        TagChip(tag = TagModel.Tags[1], modifier = Modifier.clickable{
            onClick(TagModel.Tags[1].tagId)
        })
        TagChip(tag = TagModel.Tags[2], modifier = Modifier.clickable{
            onClick(TagModel.Tags[2].tagId)
        })
        TagChip(tag = TagModel.Tags[3], modifier = Modifier.clickable{
            onClick(TagModel.Tags[3].tagId)
        })
        TagChip(tag = TagModel.Tags[4], modifier = Modifier.clickable{
            onClick(TagModel.Tags[4].tagId)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun TagChipPreview() {
    TagChip(
        TagModel.Tags[0]
    )
}
