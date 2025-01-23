package com.pawel.touristapp.placeinformation

import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

class DownloadingInformationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val receivedLocation = intent.getStringExtra("location") ?: ""
        enableEdgeToEdge()
        setContent {
            InformationScreen(receivedLocation)
        }
    }

    @Composable
    fun InformationScreen(location: String){
        val context = LocalContext.current
        var downloadedInformation = ""
        val informationServiceIntent = Intent(context, InformationService::class.java)

        DisposableEffect(Unit) {
            val receiver  = InformationBroadcastReceiver{ newDownloadedInformation ->
                downloadedInformation = newDownloadedInformation
            }
            val intentFilter =IntentFilter("com.pawel.DATA_DOWNLOADED")
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                context.registerReceiver(receiver, intentFilter, RECEIVER_EXPORTED)
            }else{
                @Suppress("UnspecifiedRegisterReceiverFlag")
                context.registerReceiver(receiver, intentFilter)
            }

            onDispose {
                context.unregisterReceiver(receiver)
            }
        }

        Column(modifier = Modifier.padding(8.dp, 64.dp, 8.dp, 0.dp)) {
            context.startService(informationServiceIntent)
            if(downloadedInformation.isEmpty()){
                Text("Failed to download")
            }else{
                Text(downloadedInformation)
            }

        }
    }
}