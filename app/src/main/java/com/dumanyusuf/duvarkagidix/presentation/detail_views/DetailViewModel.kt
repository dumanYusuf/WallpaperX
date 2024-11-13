package com.dumanyusuf.duvarkagidix.presentation.detail_views

import android.app.Activity
import android.app.WallpaperManager
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Size
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject


@HiltViewModel
class DetailViewModel @Inject constructor(
):ViewModel(){

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

            }
        }
    }





}