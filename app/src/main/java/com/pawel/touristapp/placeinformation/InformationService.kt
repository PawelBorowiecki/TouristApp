package com.pawel.touristapp.placeinformation

import android.app.Service
import android.content.Intent
import android.os.IBinder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class InformationService(private val title: String) : Service() {
    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        coroutineScope.launch {
            val downloadedInformation = getInformationOkHttpForCoroutine(title)
            val broadcastIntent = Intent("com.pawel.DATA_DOWNLOADED")
            broadcastIntent.putExtra("Article", downloadedInformation)
            sendBroadcast(broadcastIntent)
            stopSelf()
        }

        return START_STICKY
    }

    private suspend fun getInformationOkHttpForCoroutine(title: String): String{
        val client = OkHttpClient()
        val request = Request.Builder().url("https://en.wikipedia.org/$title").build()
        return withContext(Dispatchers.IO){
            try {
                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    response.body?.string() ?: ""
                } else {
                    "Error: Unable to download article."
                }
            } catch (e: Exception) {
                "Error: ${e.message}"
            }
        }

    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        coroutineScope.cancel()
        super.onDestroy()
    }
}