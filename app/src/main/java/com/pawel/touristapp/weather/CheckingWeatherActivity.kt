package com.pawel.touristapp.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pawel.touristapp.weather.model.WeatherData
import com.pawel.touristapp.weather.model.WeatherViewModel

class CheckingWeatherActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val receivedLocation = intent.getStringExtra("location") ?: ""
        enableEdgeToEdge()
        setContent {
            WeatherScreen(receivedLocation)
        }
    }

    @Composable
    private fun WeatherScreen(location: String, viewModel: WeatherViewModel = viewModel()){
        val weatherList by viewModel.weatherList.collectAsState(initial = emptyList())

        LaunchedEffect(Unit) {
            viewModel.getWeathers()
        }
        viewModel.addWeatherFromApi(
            cityName = location,
            apiKey = Constants.OPEN_WEATHER_API_KEY
        )

        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.background(Color.Blue)
        ) {
            LazyColumn(
                modifier = Modifier.padding(0.dp, 64.dp, 0.dp, 0.dp)
            ) {
                items(weatherList){ weather ->
                    WeatherItem(weather = weather, onDelete = { viewModel.deleteWeather(weather.id) })
                }
            }
        }

    }

    @Composable
    private fun WeatherItem(weather: WeatherData, onDelete: () -> Unit){
        OutlinedCard(
            colors = CardDefaults.cardColors(containerColor = Color.Cyan),
            border = BorderStroke(6.dp, Color.Gray),
            modifier = Modifier.fillMaxWidth().height(240.dp).padding(8.dp, 4.dp, 8.dp, 4.dp)
        ) {
            Text(text = "City: ${weather.city}", modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp), color = Color.DarkGray, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Temperature: ${weather.temperature}°C", modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp), color = Color.DarkGray, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Description: ${weather.description}", modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp), color = Color.DarkGray, fontSize = 22.sp, fontWeight = FontWeight.Bold)

            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.DarkGray)
            }
        }
    }
}