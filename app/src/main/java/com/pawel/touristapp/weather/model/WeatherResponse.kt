package com.pawel.touristapp.weather.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("name") val name: String,
    @SerializedName("weather") val weatherDescription: List<WeatherDescription>,
    @SerializedName("main") val main: MainWeather
)

data class WeatherDescription(
    @SerializedName("description") val description: String
)

data class MainWeather(
    @SerializedName("temp") val temp: Double
)