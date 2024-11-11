package com.dumanyusuf.duvarkagidix.data.di

import com.dumanyusuf.duvarkagidix.data.remote.WallpaperApi
import com.dumanyusuf.duvarkagidix.data.repo.RepoImplm
import com.dumanyusuf.duvarkagidix.domain.repo.WallpaperRepo
import com.dumanyusuf.duvarkagidix.util.Constans
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {



    @Provides
    @Singleton
    fun getProvidesWallpaper():WallpaperApi{
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constans.BASE_URL).build().create(WallpaperApi::class.java)
    }

    @Provides
    @Singleton
    fun getRepo(api:WallpaperApi):WallpaperRepo{
        return RepoImplm(api)
    }

}