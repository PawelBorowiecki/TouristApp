package com.pawel.touristapp.weather

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pawel.touristapp.R
import com.pawel.touristapp.weather.model.WeatherData
import com.pawel.touristapp.weather.model.WeatherViewModel

class CheckingWeatherActivity : ComponentActivity() {
    companion object{
        const val CHANNEL_ID = "weather_channel_id"
        const val NOTIFICATION_ID = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val receivedLocation = intent.getStringExtra("location") ?: ""
        enableEdgeToEdge()
        createNotificationChannel()
        setContent {
            WeatherScreen(receivedLocation)
        }
    }

    private fun createNotificationChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val name = "weather channel"
            val descriptionText = "Weather notification channel"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun checkNotificationPermission(): Boolean{
        return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
        }else{
            true
        }
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    private fun sendNotification(context: Context, location: String){
        val builer = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentTitle("Saved in database.")
            .setContentText("Weather in $location has already been saved in database.")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        with(NotificationManagerCompat.from(context)){
            notify(NOTIFICATION_ID, builer.build())
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
        NotificationToast(
            context = LocalContext.current,
            location = location
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

    @Composable
    private fun NotificationToast(context: Context, location: String){
        var permissionGranted by remember { mutableStateOf(checkNotificationPermission()) }
        val requestPermissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            permissionGranted = isGranted
        }

        if(permissionGranted){
            sendNotification(context, location)
        }else{
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}