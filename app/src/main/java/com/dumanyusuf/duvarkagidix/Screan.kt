package com.dumanyusuf.duvarkagidix

 sealed class Screan (val route:String){

     object HomePageView:Screan("home_page")
     object DetailPageView:Screan("detail_page")


 }