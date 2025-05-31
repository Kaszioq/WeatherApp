package com.example.weatherapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.network.ApiClient
import com.example.weatherapp.network.WeatherResponse
import kotlinx.coroutines.delay

@Composable
fun WeatherScreen(
    viewModel: WeatherViewModel,
    navController: NavController
) {
    val context = LocalContext.current.applicationContext
    val factory = WeatherViewModelFactory(
        api = ApiClient.weatherApi,
        dao = WeatherDatabase.getDatabase(context).weatherDao()
    )
    val viewModel: WeatherViewModel = viewModel(factory = factory)

    var city by remember { mutableStateOf("Warsaw") }
    val weather by viewModel.weather.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Auto-refresh every 20 seconds
    LaunchedEffect(city) {
        while (true) {
            viewModel.fetchWeather(city, ApiClient.API_KEY)
            delay(20_000)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = {
                    navController.navigate("history")
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(Icons.Default.List, contentDescription = "History")
                Spacer(modifier = Modifier.width(4.dp))
                Text("History")
            }
        }

        if (isLoading) {
            CircularProgressIndicator()
        }

        error?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
        }

        weather?.let {
            WeatherInfo(it)
        }
    }
}

@Composable
fun WeatherInfo(data: WeatherResponse) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF2C2C2C) // Light gray background
        )
    )
    {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = data.name, style = MaterialTheme.typography.titleLarge)

            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("https://openweathermap.org/img/wn/${data.weather.firstOrNull()?.icon ?: "01d"}@2x.png")
                        .crossfade(true)
                        .build(),
                    contentDescription = "Weather Icon",
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(data.weather.firstOrNull()?.description?.replaceFirstChar { it.uppercaseChar() } ?: "N/A")
                    Text("${data.main.temp}°C", fontWeight = FontWeight.Bold)
                    Text("Humidity: ${data.main.humidity}%")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Pressure: ${data.main.pressure} hPa")
            Text("Wind: ${data.wind.speed} m/s")
        }
    }
}
