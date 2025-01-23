package com.pawel.touristapp.weather.network

import com.pawel.touristapp.weather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherAPIService {
    @GET("data/2.5/weather")
    suspend fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): WeatherResponse
}