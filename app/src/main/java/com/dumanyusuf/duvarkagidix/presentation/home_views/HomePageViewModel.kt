package com.dumanyusuf.duvarkagidix.presentation.home_views

import android.app.Activity
import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumanyusuf.duvarkagidix.domain.use_case.home_use_case.GetWallpaperUseCase
import com.dumanyusuf.duvarkagidix.util.Resource
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val useCase: GetWallpaperUseCase,
) : ViewModel() {

    private val _state = mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private var job: Job? = null

    // Reklamın gösterilip gösterilmediğini kontrol etmek için bir bayrak
    private var isAdShown = false
    private val adUnitId = "ca-app-pub-3993872063354474/2087308604" // Test Interstitial ID
    var interstitialAd: InterstitialAd? = null

    init {
        Log.e("init calıstı","init calıstı")
        getWallpaper()
    }

    fun getWallpaper() {
        Log.e("wallpaper calıstı","walpaper calıstı")
        _state.value = HomeState(isLoading = true)

        job?.cancel()
        job = useCase.getNewsApp().onEach {
            when (it) {
                is Resource.Success -> {
                    Log.e("wallpaper calıstı sucesss","walpaper calıstı sucesss")
                    _state.value = HomeState(listWallpaper = it.data ?: emptyList())
                    Log.e("success", "${it.data}")
                }
                is Resource.Loading -> {
                    _state.value = HomeState(isLoading = true)
                    Log.e("loading", "${it}")
                }
                is Resource.Error -> {
                    _state.value = HomeState(isError = it.message ?: "Error")
                    Log.e("error", "${it.message}")
                }
            }
        }.launchIn(viewModelScope)
    }


    fun loadInterstitialAd(context: Context) {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(context, adUnitId, adRequest, object : InterstitialAdLoadCallback() {
            override fun onAdLoaded(ad: InterstitialAd) {
                interstitialAd = ad
            }

            override fun onAdFailedToLoad(adError: LoadAdError) {
                interstitialAd = null
            }
        })
    }

    fun showInterstitialAd(context: Context, onAdClosed: () -> Unit) {
        if (!isAdShown && interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    isAdShown = true  // Reklam bir kez gösterildikten sonra tekrar gösterilmesin
                    onAdClosed()
                }
            }
            interstitialAd?.show(context as Activity)
        } else {
            // Eğer reklam gösterilmediyse, normal şekilde işlemi tamamla
            onAdClosed()
        }
    }



}
