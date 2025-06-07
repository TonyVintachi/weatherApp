package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
// import androidx.activity.viewModels // Import for by viewModels() - Not used in this version
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState // Import for collectAsState
import androidx.compose.runtime.getValue // Import for getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel // Import for viewModel()
import com.example.weatherapp.ui.CurrentWeatherCard
import com.example.weatherapp.ui.ForecastView
import com.example.weatherapp.ui.theme.WeatherAppTheme
import com.example.weatherapp.viewmodel.WeatherViewModel
import android.content.res.Configuration // Required for uiMode in Preview

class MainActivity : ComponentActivity() {
    // private val weatherViewModel: WeatherViewModel by viewModels() // Alternative way to get ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                // Obtain ViewModel using viewModel() Composable utility
                val weatherViewModel: WeatherViewModel = viewModel()
                MainScreen(weatherViewModel = weatherViewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(weatherViewModel: WeatherViewModel, modifier: Modifier = Modifier) {
    // Collect states from ViewModel
    val currentWeatherResult by weatherViewModel.currentWeather.collectAsState()
    val forecastResult by weatherViewModel.forecast.collectAsState()

    // Trigger data fetch for default location when MainScreen is first composed
    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeatherForDefaultLocation()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather App") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            CurrentWeatherCard(weatherDataResult = currentWeatherResult)
            Spacer(modifier = Modifier.height(16.dp))
            ForecastView(forecastResult = forecastResult)
        }
    }
}

@Preview(showBackground = true, name = "Main Screen Light Mode")
@Composable
fun MainScreenPreview() {
    WeatherAppTheme {
        // For preview, you might need to pass a dummy/mock ViewModel or use static data.
        // This preview will likely show loading states or require a more complex setup
        // if the ViewModel makes actual calls in preview.
        // For simplicity, we'll pass a default ViewModel instance.
        // In a real app, you'd mock the ViewModel's data for previews.
        val previewViewModel = WeatherViewModel() // Be cautious with this in previews
        MainScreen(weatherViewModel = previewViewModel)
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, name = "Main Screen Dark Mode")
@Composable
fun MainScreenPreviewDark() {
    WeatherAppTheme {
        val previewViewModel = WeatherViewModel()
        MainScreen(weatherViewModel = previewViewModel)
    }
}
