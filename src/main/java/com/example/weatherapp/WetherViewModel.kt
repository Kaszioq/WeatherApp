package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weatherapp.repository.WeatherRepository
import com.example.weatherapp.data.local.WeatherEntity
import com.example.weatherapp.network.WeatherResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _weather = MutableStateFlow<WeatherResponse?>(null)
    val weather: StateFlow<WeatherResponse?> = _weather

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _weatherHistory = MutableStateFlow<List<WeatherEntity>>(emptyList())
    val weatherHistory: StateFlow<List<WeatherEntity>> = _weatherHistory

    fun fetchWeather(city: String, apiKey: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val response = repository.fetchWeather(city, apiKey)
                _weather.value = response

                val entity = WeatherEntity(
                    cityName = response.name,
                    description = response.weather.firstOrNull()?.description ?: "N/A",
                    temperature = response.main.temp,
                    feelsLike = response.main.feels_like,
                    humidity = response.main.humidity,
                    pressure = response.main.pressure,
                    windSpeed = response.wind.speed,
                    icon = response.weather.firstOrNull()?.icon ?: "",
                    timestamp = System.currentTimeMillis()
                )

                repository.saveWeather(entity)
                loadHistory() // refresh after save
            } catch (e: Exception) {
                e.printStackTrace()
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadHistory() {
        viewModelScope.launch {
            _weatherHistory.value = repository.getSavedWeather()
        }
    }

    suspend fun getSavedWeather(): List<WeatherEntity> {
        return repository.getSavedWeather()
    }
}
