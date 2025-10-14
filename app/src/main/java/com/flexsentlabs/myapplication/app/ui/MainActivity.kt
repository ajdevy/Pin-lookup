package com.flexsentlabs.myapplication.app.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.flexsentlabs.myapplication.R
import com.flexsentlabs.myapplication.app.ui.theme.MyApplicationTheme
import com.flexsentlabs.myapplication.imagesearch.ui.ImageSearchScreen

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyApplicationTheme {
                ImageSearchScreen()
            }
        }
    }
}