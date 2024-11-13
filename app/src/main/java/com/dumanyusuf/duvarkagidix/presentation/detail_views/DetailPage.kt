package com.dumanyusuf.duvarkagidix.presentation.detail_views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import com.dumanyusuf.duvarkagidix.R
import com.dumanyusuf.duvarkagidix.Screan
import com.dumanyusuf.duvarkagidix.domain.model.WallpaperModel
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailPage(
    wallperModel: WallpaperModel,
    navController: NavController,
   viewModel: DetailViewModel= hiltViewModel()
) {

    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val snakbarHostState= remember { SnackbarHostState() }



    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snakbarHostState){
                Snackbar (
                    snackbarData = it,
                    contentColor = Color.Blue,
                    containerColor = Color.White,
                    actionColor = Color.Red
                )


            }
        },
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(model = wallperModel.largeImageURL),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds
                )
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f)),
                                startY = 300f
                            )
                        )
                )

              Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                  IconButton(
                      onClick = { navController.popBackStack() },
                      modifier = Modifier
                          .padding(top = 50.dp, start = 16.dp)
                          .size(50.dp)
                          //.align(Alignment.TopStart)
                          .background(
                              color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                              shape = MaterialTheme.shapes.small
                          )
                  ) {
                      Icon(
                          painter = painterResource(id = R.drawable.back),
                          contentDescription = "Back",
                          tint = MaterialTheme.colorScheme.onBackground
                      )
                  }
                  // Download Button
                  IconButton(
                      onClick = {
                          scope.launch {

                              val sb=snakbarHostState.showSnackbar("Resmi indirmek istiyor musun?", actionLabel = "Evet", duration = SnackbarDuration.Short)

                              if (sb==SnackbarResult.ActionPerformed){
                                  viewModel.downloadImageToGallery(context, wallperModel.webformatURL)
                                  Toast.makeText(context, "Resim indirildi", Toast.LENGTH_SHORT).show()
                              }

                          }

                      },
                      modifier = Modifier
                          .padding(top = 50.dp, end = 16.dp)
                          .size(48.dp)
                          .background(Color.White, shape = MaterialTheme.shapes.small)
                  ) {
                      Icon(
                          painter = painterResource(id = R.drawable.dowload),
                          contentDescription = "Download",
                          tint = MaterialTheme.colorScheme.primary
                      )
                  }

              }

                // Bottom controls and text
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 100.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Fotoğraf: ${wallperModel.tags}",
                        fontSize = 20.sp,
                        color = Color.White,
                        modifier = Modifier
                            .padding(horizontal = 10.dp)
                            .align(Alignment.CenterHorizontally)
                    )

                    // Wallpaper Set Button
                    Button(
                        onClick = { showDialog = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            text = "Duvar Kağıdı Olarak Ayarla",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.White
                        )
                    }


                }
            }
        }
    )

    // Wallpaper Set Dialog
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Duvar Kağıdı Olarak Ayarla") },
            text = {
                Column {
                    Text("Duvar kağıdını nereye ayarlamak istersiniz?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        listOf(
                            "Ana Ekran" to "Home",
                            "Kilit Ekranı" to "Lock",
                            "Ana ve Kilit Ekranı" to "Both"
                        ).forEach { (label, type) ->
                            TextButton(
                                onClick = {
                                    scope.launch {
                                        viewModel.setWallpaper(context, wallperModel.webformatURL, type)
                                        showDialog = false
                                    }
                                }
                            ) {
                                Text(text = label, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }
}






