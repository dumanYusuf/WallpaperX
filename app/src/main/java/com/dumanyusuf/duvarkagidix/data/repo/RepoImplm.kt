package com.dumanyusuf.duvarkagidix.data.repo

import com.dumanyusuf.duvarkagidix.data.remote.WallpaperApi
import com.dumanyusuf.duvarkagidix.data.remote.dto.Hit
import com.dumanyusuf.duvarkagidix.data.remote.dto.WallpaperDto
import com.dumanyusuf.duvarkagidix.domain.repo.WallpaperRepo
import com.dumanyusuf.duvarkagidix.util.Constans
import javax.inject.Inject

class RepoImplm @Inject constructor(private val api: WallpaperApi) : WallpaperRepo {

    // Singleton veri saklama
    private var cachedWallpapers: WallpaperDto? = null

    override suspend fun getWallpaperList(): WallpaperDto {
        // Eğer önceden alınmış veriler varsa, onları döndürüyoruz
        cachedWallpapers?.let {
            return it
        }

        // Eğer önceden veri yoksa, API'den alıyoruz
        val page1 = api.getWallpaperList(page = 1, per_page = 200, Constans.API_KEY)
        val page2 = api.getWallpaperList(page = 2, per_page = 200, Constans.API_KEY)
        val page3 = api.getWallpaperList(page = 3, per_page = 200, Constans.API_KEY)

        // Verileri birleştirip saklıyoruz
        val combinedList = mutableListOf<Hit>()
        combinedList.addAll(page1.hits)
        combinedList.addAll(page2.hits)
        combinedList.addAll(page3.hits)

        val total = page1.total
        val totalHits = page1.totalHits

        val wallpaperDto = WallpaperDto(hits = combinedList, total = total, totalHits = totalHits)

        // Veriyi bellekte saklıyoruz
        cachedWallpapers = wallpaperDto

        return wallpaperDto
    }
}
