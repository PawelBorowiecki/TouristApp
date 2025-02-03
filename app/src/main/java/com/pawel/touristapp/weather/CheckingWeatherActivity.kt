package com.pawel.touristapp.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pawel.touristapp.R
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
        Box(
            modifier = Modifier.fillMaxSize()
        ){
            Image(
                painter = painterResource(id = R.drawable.weather_activity_background_image),
                contentDescription = "WeatherActivity background image",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.matchParentSize()
            )
            Column(
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier.padding(0.dp, 64.dp, 0.dp, 0.dp).fillMaxSize()
                ) {
                    items(weatherList){ weather ->
                        WeatherItem(weather = weather, onDelete = { viewModel.deleteWeather(weather.id) })
                    }
                }
            }
        }


    }

    @Composable
    private fun WeatherItem(weather: WeatherData, onDelete: () -> Unit){
        OutlinedCard(
            colors = CardDefaults.cardColors(containerColor = Color.Transparent),
            border = BorderStroke(6.dp, Color.Black),
            modifier = Modifier.fillMaxWidth().height(250.dp).padding(8.dp, 4.dp, 8.dp, 4.dp).background(brush = Brush.radialGradient(colors = listOf(Color.Transparent, Color.White), tileMode = TileMode.Clamp))
        ) {
            Text(text = "City: ${weather.city}", modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp), color = Color(46, 46, 45), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Date: ${weather.date}", modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp), color = Color(46, 46, 45), fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(text = "Temperature: ${weather.temperature}°C", modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp), color = Color(46, 46, 45), fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(text = "Description: ${weather.description}", modifier = Modifier.padding(8.dp, 16.dp, 8.dp, 8.dp), color = Color(46, 46, 45), fontSize = 22.sp, fontWeight = FontWeight.Bold)

            IconButton(onClick = onDelete) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete", tint = Color.Black)
            }
        }
    }
}