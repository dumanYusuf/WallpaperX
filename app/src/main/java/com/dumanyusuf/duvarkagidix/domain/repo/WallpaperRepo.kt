package com.dumanyusuf.duvarkagidix.domain.repo

import com.dumanyusuf.duvarkagidix.data.remote.dto.WallpaperDto


interface WallpaperRepo {


    // api
    suspend fun getWallpaperList():WallpaperDto


}