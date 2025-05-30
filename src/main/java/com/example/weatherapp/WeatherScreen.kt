package com.example.weatherapp

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun WeatherScreen(viewModel: WeatherViewModel = viewModel()) {
    var city by remember { mutableStateOf("Warsaw") }
    val weather = viewModel.weather.collectAsState().value

    LaunchedEffect(Unit) {
        viewModel.fetchWeather(city, BuildConfig.OPEN_WEATHER_KEY)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") }
        )

        Button(
            onClick = { viewModel.fetchWeather(city, BuildConfig.OPEN_WEATHER_KEY) },
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text("Get Weather")
        }

        weather?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "City: ${it.name}")
            Text(text = "Temp: ${it.main.temp} °C")
            Text(text = "Humidity: ${it.main.humidity}%")
            Text(text = "Condition: ${it.weather.firstOrNull()?.description}")
        }
    }
}
