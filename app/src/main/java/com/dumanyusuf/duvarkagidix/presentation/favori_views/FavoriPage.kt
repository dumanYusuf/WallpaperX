package com.dumanyusuf.duvarkagidix.presentation.favori_views

import android.annotation.SuppressLint
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoriPage() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Favorilerim")
                }
            )
        },
        content = {

        }
    )

}