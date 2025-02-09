package com.pawel.touristapp.placeWebView

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class WebBroadcastReceiver(private val receivedUrl: String) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "com.pawel.WEB_BROWSING"){
            Toast.makeText(context, receivedUrl, Toast.LENGTH_SHORT).show()
        }
    }
}