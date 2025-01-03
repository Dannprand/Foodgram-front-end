package com.example.foodgram.views.templates

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Template Label Authentication di Authentication Form (Login & Register)
@Composable
fun AuthenticationLabel(
    label: String,
    labelModifier: Modifier,
    labelColor: Color,
    labelAlign: TextAlign,
    labelSize: TextUnit,
    labelFontWeight: FontWeight
) {
    Text(
        "" + label,
        textAlign = labelAlign,
        color = labelColor,
        modifier = labelModifier,
        fontSize = labelSize,
        fontWeight = labelFontWeight
    )
}

// Template Label Page Title
@Composable
fun PageTitleLabel(
    label: String,
) {
    Text(
        "" + label,
        textAlign = TextAlign.Start,
        color = Color.Black,
        modifier = Modifier
            .padding(0.dp, 16.dp, 0.dp, 0.dp),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

// Template Label Section Title
@Composable
fun SectionTitleLabel(
    label: String,
) {
    Text(
        "" + label,
        color = Color.Black,
        modifier = Modifier
            .padding(0.dp, 2.dp, 0.dp, 0.dp),
        fontSize = 22.sp,
        fontWeight = FontWeight.SemiBold
    )
}