package com.pawel.touristapp.weather.model

data class WeatherData(
    val city: String =  "",
    val temperature: Double = 0.0,
    val description: String = "",
    val id: String = ""
)