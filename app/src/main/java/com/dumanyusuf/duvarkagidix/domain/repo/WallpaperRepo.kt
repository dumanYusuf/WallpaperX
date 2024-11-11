package com.dumanyusuf.duvarkagidix.domain.repo

import com.dumanyusuf.duvarkagidix.data.remote.dto.WallpaperDto
import com.dumanyusuf.duvarkagidix.domain.model.WallpaperModel

interface WallpaperRepo {


    suspend fun getWallpaperList():WallpaperDto

}