package com.pawel.touristapp.weather.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.pawel.touristapp.weather.model.WeatherData
import kotlinx.coroutines.tasks.await

class WeatherRepository {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("weather")

    suspend fun addWeather(weather: WeatherData) {
        val document = collection.document()
        val weatherWithId = weather.copy(id = document.id)
        document.set(weatherWithId).await()
    }

    suspend fun getWeathers(): List<WeatherData> {
        val snapshot = collection.get().await()
        return snapshot.documents.mapNotNull { it.toObject(WeatherData::class.java) }
    }

    suspend fun deleteWeather(id: String) {
        collection.document(id).delete().await()
    }
}