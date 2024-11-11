package com.dumanyusuf.duvarkagidix.data.remote.dto

import com.dumanyusuf.duvarkagidix.domain.model.WallpaperModel

data class WallpaperDto(
    val hits: List<Hit>,
    val total: Int,
    val totalHits: Int,
)


fun WallpaperDto.toWallpaper():List<WallpaperModel>{
    return hits.map {
        WallpaperModel(
            it.id,
            it.pageURL,
            it.type,
            it.tags,
            it.previewURL,
            it.previewWidth,
            it.webformatURL,
            it.largeImageURL,
            it.collections,
            it.likes,

            )
    }
}