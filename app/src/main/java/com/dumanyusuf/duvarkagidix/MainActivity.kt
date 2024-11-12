package com.dumanyusuf.duvarkagidix

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.dumanyusuf.duvarkagidix.presentation.home_views.HomePageScrean
import com.dumanyusuf.duvarkagidix.presentation.navigation.PageController
import com.dumanyusuf.duvarkagidix.ui.theme.DuvarKagidiXTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DuvarKagidiXTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                   PageController()
                }
            }
        }
    }
}

