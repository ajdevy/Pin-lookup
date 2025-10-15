package com.flexsentlabs.myapplication.app.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.flexsentlabs.myapplication.R
import com.flexsentlabs.myapplication.app.ui.theme.MyApplicationTheme
import com.flexsentlabs.myapplication.imagesearch.ui.ImageSearchScreen
import org.koin.androidx.compose.koinViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Enable edge-to-edge
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                ImageSearchScreen(viewModel = koinViewModel())
            }
        }
    }
}