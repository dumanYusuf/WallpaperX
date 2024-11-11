package com.dumanyusuf.duvarkagidix.domain.use_case.home_use_case

import android.util.Log
import com.dumanyusuf.duvarkagidix.data.remote.dto.toWallpaper
import com.dumanyusuf.duvarkagidix.domain.model.WallpaperModel
import com.dumanyusuf.duvarkagidix.domain.repo.WallpaperRepo
import com.dumanyusuf.duvarkagidix.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetWallpaperUseCase @Inject constructor(private val repo: WallpaperRepo) {

    fun getNewsApp(): Flow<Resource<List<WallpaperModel>>> = flow {
        try {
            emit(Resource.Loading())
            val listNews = repo.getWallpaperList()
            if (listNews.hits.isNotEmpty()) {
                emit(Resource.Success(listNews.toWallpaper()))
            } else {
                emit(Resource.Error("Error: No articles found"))
                Log.e("NewsUseCase", "Error: No articles found")
            }
        } catch (e: Exception) {
            val errorMessage = "Error: ${e.localizedMessage ?: "Unknown error"}"
            emit(Resource.Error(errorMessage))
            Log.e("NewsUseCase", errorMessage, e)
        }
    }

}