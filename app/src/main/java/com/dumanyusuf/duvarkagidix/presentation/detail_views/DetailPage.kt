package com.dumanyusuf.duvarkagidix.presentation.detail_views

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Icon
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.NavController
import coil.ImageLoader
import com.dumanyusuf.duvarkagidix.R
import com.dumanyusuf.duvarkagidix.Screan
import com.dumanyusuf.duvarkagidix.domain.model.WallpaperModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailPage(wallperModel: WallpaperModel, navController: NavController) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(



        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = rememberAsyncImagePainter(model = wallperModel.largeImageURL),
                    contentDescription = null,
                    contentScale = ContentScale.Crop
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
                  IconButton(
                      onClick = {  },
                      modifier = Modifier
                          .padding(top = 50.dp, start = 16.dp, end = 10.dp)
                          .size(50.dp)
                          //.align(Alignment.TopStart)
                          .background(
                              color = MaterialTheme.colorScheme.background.copy(alpha = 0.6f),
                              shape = MaterialTheme.shapes.small
                          )
                  ) {
                      Icon(
                          imageVector = Icons.Default.FavoriteBorder,
                          contentDescription = "Favorite",
                          tint = MaterialTheme.colorScheme.onBackground
                      )
                  }

              }

                // Bottom controls and text
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 60.dp)
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

                    // Download Button
                    IconButton(
                        onClick = {
                            scope.launch {
                                downloadImageToGallery(context, wallperModel.webformatURL)
                                Toast.makeText(context, "Resim indirildi", Toast.LENGTH_SHORT).show()
                            }
                        },
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 16.dp)
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
                                        setWallpaper(context, wallperModel.webformatURL, type)
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


suspend fun setWallpaper(context: Context, imageUrl: String, type: String) {
    val wallpaperManager = WallpaperManager.getInstance(context)

    // Coil ImageLoader kullanarak resmi indirip InputStream'e dönüştürme
    val imageLoader = ImageLoader.Builder(context).build()
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .size(Size.ORIGINAL) // En büyük boyutta resim al
        .build()

    val result = withContext(Dispatchers.IO) {
        val response = imageLoader.execute(request)
        if (response is SuccessResult) {
            val bitmap = response.drawable.toBitmap()
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            ByteArrayInputStream(stream.toByteArray())
        } else {
            null
        }
    }

    result?.let { inputStream ->
        when (type) {
            "Home" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setStream(inputStream, null, true, WallpaperManager.FLAG_SYSTEM)
                }
            }
            "Lock" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setStream(inputStream, null, true, WallpaperManager.FLAG_LOCK)
                }
            }
            "Both" -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    wallpaperManager.setStream(inputStream)
                }
            }
        }
    }
}


suspend fun downloadImageToGallery(context: Context, imageUrl: String) {
    // Resmi Coil ile indiriyoruz
    val imageLoader = ImageLoader.Builder(context).build()
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .build()

    val result = withContext(Dispatchers.IO) {
        val response = imageLoader.execute(request)
        if (response is SuccessResult) {
            response.drawable.toBitmap()
        } else {
            null
        }
    }

    result?.let { bitmap ->
        // API seviyesini kontrol et
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Scoped storage (Android 10 ve sonrası)
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "duvar_kagidi_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/Duvarkagidlari")
            }

            // Resmi kaydetmek için URI'yi alıyoruz
            val uri = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            uri?.let { imageUri ->
                context.contentResolver.openOutputStream(imageUri)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
                    outputStream.flush()
                    // Burada resmin başarıyla kaydedildiğini belirten bir işlem yapabilirsiniz.
                }
            }
        } else {
            // Android 9 ve öncesi için
            val path = context.getExternalFilesDir(null)?.absolutePath + "/duvar_kagidi_${System.currentTimeMillis()}.jpg"
            val file = File(path)
            FileOutputStream(file).use { out ->
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
            }
            // Burada resmi galeriye göstermek için uygun işlemi yapabilirsiniz.
        }
    }
}


