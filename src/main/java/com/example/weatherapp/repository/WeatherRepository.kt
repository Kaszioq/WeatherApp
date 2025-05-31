package com.example.weatherapp.repository

import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.network.WeatherResponse

class WeatherRepository(
    private val api: WeatherApi,
    private val dao: WeatherDao
) {
    suspend fun fetchWeather(city: String, apiKey: String): WeatherResponse {
        return api.getWeatherByCity(city, apiKey)
    }

    suspend fun saveWeather(weather: WeatherEntity) {
        dao.insertWeather(weather)
    }

    suspend fun getSavedWeather(): List<WeatherEntity> {
        return dao.getAllWeather()
    }
}
