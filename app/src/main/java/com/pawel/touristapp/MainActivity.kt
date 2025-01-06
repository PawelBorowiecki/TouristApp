package com.pawel.touristapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.pawel.touristapp.placeinformation.DownloadingInformationActivity
import com.pawel.touristapp.ui.theme.TouristAppTheme
import com.pawel.touristapp.weather.CheckingWeatherActivity

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TouristAppTheme {
                MainScreen()
            }
        }
    }

    @Composable
    private fun MainScreen(){
        var placeInput by remember { mutableStateOf("") }
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
            ) {
            TextField(
                value = placeInput,
                onValueChange = { placeInput = it },
                label = { Text("Enter place name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {
                    openMap(placeInput)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = placeInput.isNotBlank()
            ) {
                Text("Find way")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {
                    checkWeather(placeInput)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = placeInput.isNotBlank()
            ) {
                Text("Check weather and compare with previous searchings")
            }
            Spacer(modifier = Modifier.padding(8.dp))
            Button(
                onClick = {
                    downloadInformation(placeInput)
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = placeInput.isNotBlank()
            ) {
                Text("Download information about place")
            }
        }
    }

    private fun openMap(location: String){
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("geo:0,0?q=$location")
        }
        startActivity(intent)
    }

    private fun checkWeather(location: String){
        val intent = Intent(this, CheckingWeatherActivity::class.java).apply {
            putExtra("location", location)
        }
        startActivity(intent)
    }

    private fun downloadInformation(location: String){
        val intent = Intent(this, DownloadingInformationActivity::class.java).apply {
            putExtra("location", location)
        }
        startActivity(intent)
    }
}