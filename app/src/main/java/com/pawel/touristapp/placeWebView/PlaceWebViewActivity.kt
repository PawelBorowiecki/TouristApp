package com.pawel.touristapp.placeWebView

import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

class PlaceWebViewActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val receivedLocation = intent.getStringExtra("location") ?: ""
        enableEdgeToEdge()
        setContent {
            WebViewScreen(receivedLocation)
        }
    }

    @Composable
    fun WebViewScreen(location: String){
        val url = "https://en.wikipedia.org/wiki/${location.replace(" ", "_")}"

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.javaScriptEnabled = true
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    webViewClient = WebViewClient()
                    loadUrl(url)
                }
            }
        )
    }
}