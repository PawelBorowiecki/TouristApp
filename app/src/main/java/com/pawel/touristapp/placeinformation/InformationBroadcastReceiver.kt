package com.pawel.touristapp.placeinformation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class InformationBroadcastReceiver(private val onDataReceived: (String) -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if(intent?.action == "com.pawel.DATA_DOWNLOADED"){
            val data = intent.getStringExtra("Article") ?: ""
            onDataReceived(data)
        }
    }
}