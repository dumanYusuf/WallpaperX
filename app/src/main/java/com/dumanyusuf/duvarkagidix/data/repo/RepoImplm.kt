package com.dumanyusuf.duvarkagidix.data.repo

import com.dumanyusuf.duvarkagidix.data.remote.WallpaperApi
import com.dumanyusuf.duvarkagidix.data.remote.dto.Hit
import com.dumanyusuf.duvarkagidix.data.remote.dto.WallpaperDto
import com.dumanyusuf.duvarkagidix.domain.repo.WallpaperRepo
import com.dumanyusuf.duvarkagidix.util.Constans
import javax.inject.Inject

class RepoImplm @Inject constructor(private val api:WallpaperApi):WallpaperRepo {

   /* override suspend fun getWallpaperList(page:Int,perPage:Int):WallpaperDto{
            return api.getWallpaperList(page,perPage,Constans.API_KEY)
        }*/

   /* override suspend fun getWallpaperList(): WallpaperDto {
        return api.getWallpaperList(page = 3, per_page = 200, Constans.API_KEY)
    }*/

    override suspend fun getWallpaperList(): WallpaperDto {
        // 3 farklı sayfadan verileri almak için paralel olarak API çağrıları yapabilirsiniz
        val page1 = api.getWallpaperList(page = 1, per_page = 200, Constans.API_KEY)
        val page2 = api.getWallpaperList(page = 2, per_page = 200, Constans.API_KEY)
        val page3 = api.getWallpaperList(page = 3, per_page = 200, Constans.API_KEY)

        // Verileri birleştirerek bir liste oluşturuyoruz
        val combinedList = mutableListOf<Hit>()

        // Her bir sayfanın verilerini tek bir listeye ekliyoruz
        combinedList.addAll(page1.hits)  // sayfa 1 verileri
        combinedList.addAll(page2.hits)  // sayfa 2 verileri
        combinedList.addAll(page3.hits)  // sayfa 3 verileri

        val total = page1.total      // API'den toplam öğe sayısı
        val totalHits = page1.totalHits  // API'den toplam hit sayısı

        // Bu listeyi ve gerekli parametreleri döndürüyoruz
        return WallpaperDto(hits = combinedList, total = total, totalHits = totalHits)
    }



}