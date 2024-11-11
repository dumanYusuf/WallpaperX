package com.dumanyusuf.duvarkagidix.presentation

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dumanyusuf.duvarkagidix.domain.use_case.home_use_case.GetWallpaperUseCase
import com.dumanyusuf.duvarkagidix.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class HomePageViewModel @Inject constructor(private val useCase: GetWallpaperUseCase):ViewModel() {



    private val _state= mutableStateOf(HomeState())
    val state: State<HomeState> = _state

    private var job: Job?=null

    init {
        getWallpaper()
    }

     fun getWallpaper(){
        Log.e("start viewModel","Start")
        job?.cancel()
        job=useCase.getNewsApp().onEach {
            when(it){
                is Resource.Success->{
                    _state.value= HomeState(listWallpaper = it.data ?: emptyList())
                    Log.e("sucesss","${it.data}")

                }
                is Resource.Loading->{
                    _state.value= HomeState(isLoading = true)
                    Log.e("loadding","${it}")
                }
                is Resource.Error->{
                    _state.value= HomeState(isError = it.message ?:"Error")
                    Log.e("error","${it.message}")
                }
            }
        }.launchIn(viewModelScope)
    }

}