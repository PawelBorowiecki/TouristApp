package com.pawel.touristapp.weather.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pawel.touristapp.weather.network.RetrofitInstance
import com.pawel.touristapp.weather.repository.WeatherRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _weatherList = MutableStateFlow<List<WeatherData>>(emptyList())
    val weatherList: StateFlow<List<WeatherData>> = _weatherList

    fun getWeathers() {
        viewModelScope.launch {
            _weatherList.value = repository.getWeathers()
        }
    }

    fun addWeather(weather: WeatherData) {
        viewModelScope.launch {
            repository.addWeather(weather)
            getWeathers()
        }
    }

    fun deleteWeather(id: String) {
        viewModelScope.launch {
            repository.deleteWeather(id)
            getWeathers()
        }
    }

    fun addWeatherFromApi(cityName: String, apiKey: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getCurrentWeather(cityName, apiKey)
                val weather = WeatherData(
                    city = response.name,
                    temperature = response.main.temp,
                    description = response.weatherDescription.firstOrNull()?.description ?: "N/A"
                )
                repository.addWeather(weather)
                getWeathers()
            }catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}