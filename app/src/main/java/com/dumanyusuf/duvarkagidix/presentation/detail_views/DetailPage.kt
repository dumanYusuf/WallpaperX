package com.dumanyusuf.duvarkagidix.presentation.detail_views

import android.annotation.SuppressLint
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun DetailPage(wallperModel: WallpaperModel, navController: NavController) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope() // CoroutineScope oluşturuyoruz


    Scaffold(
        content = {
            Column(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = rememberAsyncImagePainter(model = wallperModel.largeImageURL),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 50.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(onClick = {
                            navController.navigate(Screan.HomePageView.route)
                        }) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                painter = painterResource(id = R.drawable.back),
                                contentDescription = "Back"
                            )
                        }
                        IconButton(onClick = {
                            // İndirme işlemi yapılabilir

                            scope.launch {
                                downloadImageToGallery(context, wallperModel.webformatURL)
                                Toast.makeText(context,"Resim indirildi",Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(
                                modifier = Modifier.size(50.dp),
                                painter = painterResource(id = R.drawable.dowload),
                                contentDescription = "Download"
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .align(Alignment.BottomCenter)
                            .background(MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f))
                            .padding(16.dp)
                    ) {

                        Text(
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier.fillMaxSize().padding(10.dp).align(Alignment.Center),
                            text = "Fotoğraf: " +wallperModel.tags)

                        Button(
                            onClick = {
                                showDialog = true
                            },
                            modifier = Modifier
                                .align(Alignment.Center)
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            shape = MaterialTheme.shapes.large,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
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
        }
    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Duvar Kağıdı Olarak Ayarla") },
            text = {
                Column {
                    Text("Duvar kağıdını nereye ayarlamak istersiniz?")
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        TextButton(onClick = {
                            // Coroutine başlatıyoruz
                            scope.launch {
                                setWallpaper(context, wallperModel.webformatURL, "Home")
                            }
                        }) {
                            Text(text = "Ana Ekran")
                        }
                        TextButton(onClick = {
                            // Coroutine başlatıyoruz
                            scope.launch {
                                setWallpaper(context, wallperModel.webformatURL, "Lock")
                            }
                        }) {
                            Text(text = "Kilit Ekranı")
                        }
                        TextButton(onClick = {
                            // Coroutine başlatıyoruz
                            scope.launch {
                                setWallpaper(context, wallperModel.webformatURL, "Both")
                            }
                        }) {
                            Text(text = "Ana Ekran ve Kilit Ekranı")
                        }
                    }
                }
            },
            confirmButton = {

            },
            dismissButton = { /* boş bırakıldı */ }
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


