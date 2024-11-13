package com.dumanyusuf.duvarkagidix

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.dumanyusuf.duvarkagidix.presentation.navigation.PageController
import com.dumanyusuf.duvarkagidix.ui.theme.DuvarKagidiXTheme
import com.google.android.gms.ads.MobileAds
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        MobileAds.initialize(this@MainActivity) {}


        setContent {
            DuvarKagidiXTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                   PageController()
                }
            }
        }
    }
}


// uygulama kimliği
// ca-app-pub-3993872063354474~5020723587

// geciş reklamı kimligi
// ca-app-pub-3993872063354474/2087308604

// geciş test reklamı
// ca-app-pub-3940256099942544/1033173712

