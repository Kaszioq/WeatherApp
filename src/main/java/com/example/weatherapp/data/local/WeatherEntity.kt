package com.example.weatherapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weather")
data class WeatherEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityName: String,
    val description: String,
    val temperature: Float,
    val feelsLike: Float,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Float,
    val icon: String,
    val timestamp: Long = System.currentTimeMillis()
)

