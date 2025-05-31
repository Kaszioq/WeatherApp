package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weatherapp.ui.HistoryScreen
import com.example.weatherapp.data.local.WeatherDatabase
import com.example.weatherapp.network.ApiClient
import com.example.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme(darkTheme = true) {
                Surface(color = MaterialTheme.colorScheme.background) {
                    AppNavHost()
                }
            }
        }
    }
}

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val context = androidx.compose.ui.platform.LocalContext.current.applicationContext
    val viewModel: WeatherViewModel = viewModel(
        factory = WeatherViewModelFactory(
            api = ApiClient.weatherApi,
            dao = WeatherDatabase.getDatabase(context).weatherDao()
        )
    )

    NavHost(navController = navController, startDestination = "weather") {
        composable("weather") {
            WeatherScreen(viewModel = viewModel, navController = navController)
        }
        composable("history") {
            HistoryScreen(viewModel = viewModel, navController = navController)
        }
    }
}