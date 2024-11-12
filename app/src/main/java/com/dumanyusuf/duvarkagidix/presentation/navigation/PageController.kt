package com.dumanyusuf.duvarkagidix.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.dumanyusuf.duvarkagidix.Screan
import com.dumanyusuf.duvarkagidix.domain.model.WallpaperModel
import com.dumanyusuf.duvarkagidix.presentation.detail_views.DetailPage
import com.dumanyusuf.duvarkagidix.presentation.favori_views.FavoriPage
import com.dumanyusuf.duvarkagidix.presentation.home_views.views.HomePageScrean
import com.google.gson.Gson
import java.net.URLDecoder

@Composable
fun PageController() {

    val navController= rememberNavController()

    NavHost(navController=navController, startDestination = Screan.HomePageView.route){

        composable(Screan.HomePageView.route){
            HomePageScrean(navController = navController)
        }

        composable(Screan.DetailPageView.route+"/{wallpaper}",
            arguments = listOf(
                navArgument("wallpaper"){type= NavType.StringType}
            )
        ){

            val jsonNews = it.arguments?.getString("wallpaper")
            val decodedJsonNews = URLDecoder.decode(jsonNews, "UTF-8")
            val wallpaper = Gson().fromJson(decodedJsonNews, WallpaperModel::class.java)
            DetailPage(wallpaper,navController)
        }
        composable(Screan.FavoriPageView.route){
            FavoriPage()
        }

    }

}