package com.example.weatherapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.weatherapp.WeatherViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun HistoryScreen(viewModel: WeatherViewModel, navController: NavController) {
    LaunchedEffect(Unit) {
        viewModel.loadHistory()
    }

    val history = viewModel.weatherHistory.collectAsState().value

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text("History", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(history) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFF2C2C2C) // Dark-friendly light gray
                    ),
                    elevation = CardDefaults.cardElevation(6.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp)) {
                        AsyncImage(
                            model = "https://openweathermap.org/img/wn/${item.icon}@2x.png",
                            contentDescription = "Weather Icon",
                            contentScale = ContentScale.Fit,
                            modifier = Modifier
                                .size(64.dp)
                                .align(Alignment.CenterVertically)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text("City: ${item.cityName}", style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text("Condition: ${item.description}")
                            Text("Temp: ${item.temperature}°C | Feels: ${item.feelsLike}°C")
                            Text("Humidity: ${item.humidity}% | Pressure: ${item.pressure} hPa")
                            Text("Wind: ${item.windSpeed} m/s")
                            Text(
                                "Time: ${
                                    SimpleDateFormat(
                                        "HH:mm dd/MM/yyyy",
                                        Locale.getDefault()
                                    ).format(Date(item.timestamp))
                                }",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.Light
                            )
                        }
                    }
                }
            }
        }
    }
}
