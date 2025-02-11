package com.pawel.touristapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.pawel.touristapp.placeWebView.PlaceWebViewActivity
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
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(id = R.drawable.main_activity_background_image),
                contentDescription = "MainActivity background image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                TextField(
                    value = placeInput,
                    onValueChange = { placeInput = it },
                    label = { Text(text = "Enter place name", textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth()) },
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center)
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
                    Text("Read information about place")
                }
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
        val intent = Intent(this, PlaceWebViewActivity::class.java).apply {
            putExtra("location", location)
        }
        startActivity(intent)
    }
}