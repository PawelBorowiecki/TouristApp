package com.pawel.touristapp.placeWebView

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

class PlaceWebViewActivity : ComponentActivity() {
    private lateinit var webBroadcatReceiver: WebBroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val receivedLocation = intent.getStringExtra("location") ?: ""
        enableEdgeToEdge()
        setContent {
            WebViewScreen(receivedLocation)
        }
    }

    @Composable
    private fun WebViewScreen(location: String){
        val context = LocalContext.current
        val url = "https://en.wikipedia.org/wiki/${location.replace(" ", "_")}"
        webBroadcatReceiver = WebBroadcastReceiver(url)
        val intentFilter = IntentFilter("com.pawel.WEB_BROWSING")
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            context.registerReceiver(webBroadcatReceiver, intentFilter, RECEIVER_EXPORTED)
        }else{
            @Suppress("UnspecifiedRegisterReceiverFlag")
            context.registerReceiver(webBroadcatReceiver, intentFilter)
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                WebView(ctx).apply {
                    settings.javaScriptEnabled = true
                    settings.cacheMode = WebSettings.LOAD_NO_CACHE
                    webViewClient = WebViewClient()
                    loadUrl(url)
                    val broadcastIntent = Intent("com.pawel.WEB_BROWSING")
                    sendBroadcast(broadcastIntent)
                }
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(webBroadcatReceiver)
    }
}