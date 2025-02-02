package com.pawel.touristapp.weather.model

import java.util.Date

data class WeatherData(
    val city: String =  "",
    val temperature: Double = 0.0,
    val description: String = "",
    val date: Date = Date(),
    val id: String = ""
)