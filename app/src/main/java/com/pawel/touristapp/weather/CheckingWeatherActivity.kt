package com.pawel.touristapp.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyColumn {
                items(weatherList){ weather ->
                    WeatherItem(weather = weather, onDelete = { viewModel.deleteWeather(weather.id) })
                }
            }
        }

    }

    @Composable
    private fun WeatherItem(weather: WeatherData, onDelete: () -> Unit){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = "City: ${weather.city}")
                Text(text = "Temperature: ${weather.temperature}°C")
                Text(text = "Description: ${weather.description}")
            }
            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    }
}