package com.example.foodgram

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.foodgram.ui.theme.FoodGramTheme
import com.example.foodgram.views.FoodGramApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FoodGramTheme {
                FoodGramApp()
            }
        }
    }
}