package com.dumanyusuf.duvarkagidix.presentation

import com.dumanyusuf.duvarkagidix.domain.model.WallpaperModel

data class HomeState(
    val listWallpaper:List<WallpaperModel> = emptyList(),
    val isLoading:Boolean=false,
    val isError:String=""
)
