package com.dumanyusuf.duvarkagidix.presentation.home_views

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.rememberAsyncImagePainter
import com.dumanyusuf.duvarkagidix.Screan
import com.google.gson.Gson
import java.net.URLEncoder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomePageScrean(
    navController: NavController,
    viewModel: HomePageViewModel = hiltViewModel()
) {

    val state=viewModel.state.value
    val context= LocalContext.current

    Scaffold(

        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Duvar Kağıtları")
                },
            )
        },

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
                            modifier = Modifier.size(250.dp).clickable {
                                val charecterObject = Gson().toJson(category)
                                val encodedMovieObject = URLEncoder.encode(charecterObject, "UTF-8")
                                navController.navigate(Screan.DetailPageView.route+"/$encodedMovieObject")
                            },
                            painter = rememberAsyncImagePainter(model = category.webformatURL, imageLoader = ImageLoader(context) ),
                            contentDescription =null , contentScale = ContentScale.Crop)

                    }
                }
            }
        }
    )



}