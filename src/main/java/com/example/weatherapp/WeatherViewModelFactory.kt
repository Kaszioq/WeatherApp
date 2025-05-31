package com.example.weatherapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherapp.data.local.WeatherDao
import com.example.weatherapp.network.WeatherApi
import com.example.weatherapp.repository.WeatherRepository

class WeatherViewModelFactory(
    private val api: WeatherApi,
    private val dao: WeatherDao
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewModel::class.java)) {
            val repository = WeatherRepository(api, dao)
            return WeatherViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
