package com.dumanyusuf.duvarkagidix.presentation.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.TextToolbar
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.dumanyusuf.duvarkagidix.presentation.HomePageViewModel

@Composable
fun HomePageScrean(
    viewModel: HomePageViewModel= hiltViewModel()
) {

    val state=viewModel.state.value
    val context= LocalContext.current

    Scaffold(
        content = {innerPadding->
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize().padding(innerPadding),
                columns = GridCells.Fixed(2)) {
                items(viewModel.state.value.listWallpaper){category->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .size(250.dp)
                    ) {
                        Image(
                            modifier = Modifier.size(250.dp),
                            painter = rememberAsyncImagePainter(model = category.webformatURL, imageLoader = ImageLoader(context) ),
                            contentDescription =null , contentScale = ContentScale.Crop)

                    }
                }
            }
        }
    )



}