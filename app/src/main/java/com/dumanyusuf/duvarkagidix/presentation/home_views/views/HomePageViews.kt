package com.dumanyusuf.duvarkagidix.presentation.home_views.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.dumanyusuf.duvarkagidix.Screan
import com.dumanyusuf.duvarkagidix.presentation.home_views.HomePageViewModel
import com.google.gson.Gson
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScrean(
    navController: NavController,
    viewModel: HomePageViewModel = hiltViewModel()
) {
    val state = viewModel.state.value
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        Log.e("test reklamı yüklendi","test reklamı yuklendi")
        viewModel.loadInterstitialAd(context)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Duvar Kağıtları",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                },
                modifier = Modifier.padding(8.dp)
            )
        },

        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onBackground
                    )
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.listWallpaper) { wallpaper ->
                            val isLargeCard = state.listWallpaper.indexOf(wallpaper) % 7 == 0
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(if (isLargeCard) 280.dp else 180.dp)
                                    .clickable {
                                        if (viewModel.interstitialAd != null) {
                                            viewModel.showInterstitialAd(context) {
                                                val wallpaperObject = Gson().toJson(wallpaper)
                                                val encodedWallpaperObject = URLEncoder.encode(wallpaperObject, "UTF-8")
                                                navController.navigate("${Screan.DetailPageView.route}/$encodedWallpaperObject")
                                            }
                                        }
                                        else{
                                            val wallpaperObject = Gson().toJson(wallpaper)
                                            val encodedWallpaperObject = URLEncoder.encode(wallpaperObject, "UTF-8")
                                            navController.navigate("${Screan.DetailPageView.route}/$encodedWallpaperObject")
                                        }
                                    },
                                shape = MaterialTheme.shapes.medium,
                                elevation = CardDefaults.cardElevation(10.dp)
                            ) {
                                Box {
                                    Image(
                                        painter = rememberAsyncImagePainter(model = wallpaper.webformatURL, imageLoader = ImageLoader(context)),
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.fillMaxSize()
                                    )

                                    // Gradient Overlay
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(
                                                Brush.verticalGradient(
                                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f)),
                                                    startY = 100f
                                                )
                                            )
                                            .padding(8.dp)
                                    ) {
                                        // Title
                                        Text(
                                            text = wallpaper.type ?: "Beautiful Wallpaper",
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyLarge,
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        },
    )
}

