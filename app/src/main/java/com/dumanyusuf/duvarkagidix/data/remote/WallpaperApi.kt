package com.dumanyusuf.duvarkagidix.data.remote

import com.dumanyusuf.duvarkagidix.data.remote.dto.WallpaperDto
import retrofit2.http.GET
import retrofit2.http.Query


interface WallpaperApi {


    @GET("api/?key=45607829-2d46b1e145852912e9249c94a")
    suspend fun getWallpaperList(
        @Query("page") page:Int,
        @Query("per_page") per_page:Int,
        @Query("key") key:String
    ):WallpaperDto

}