package com.example.weatherapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter // Placeholder for Coil
import com.example.weatherapp.data.NetworkResult
import com.example.weatherapp.model.CurrentWeatherResponse
import com.example.weatherapp.model.WeatherCondition // Ensure this is imported

@Composable
fun CurrentWeatherCard(
    weatherDataResult: NetworkResult<CurrentWeatherResponse>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            when (weatherDataResult) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is NetworkResult.Success -> {
                    val weatherData = weatherDataResult.data
                    CurrentWeatherContent(weatherData)
                }
                is NetworkResult.Error -> {
                    Text(
                        text = "Error: ${weatherDataResult.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Composable
fun CurrentWeatherContent(weatherData: CurrentWeatherResponse, modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = weatherData.name, // City Name
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))

        val weatherCondition = weatherData.weather.firstOrNull()
        if (weatherCondition != null) {
            // Placeholder for Weather Icon - Coil will be used here later
            // For now, a simple text representation or a placeholder image
             Image(
                 painter = rememberAsyncImagePainter( // Using Coil's painter
                     model = "https://openweathermap.org/img/wn/${weatherCondition.icon}@2x.png"
                 ),
                 contentDescription = weatherCondition.description,
                 modifier = Modifier.size(80.dp)
             )
            Text(
                text = weatherCondition.main, // e.g., "Clouds"
                fontSize = 20.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = weatherCondition.description, // e.g., "scattered clouds"
                fontSize = 16.sp,
                color = Color.Gray // Or use theme color
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        Text(
            text = "${weatherData.main.temp}°C", // Current Temperature
            fontSize = 48.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Feels like: ${weatherData.main.feelsLike}°C",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            WeatherDetailItem("Humidity", "${weatherData.main.humidity}%")
            WeatherDetailItem("Wind", "${weatherData.wind.speed} m/s")
            WeatherDetailItem("Pressure", "${weatherData.main.pressure} hPa")
        }
    }
}

@Composable
fun WeatherDetailItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 14.sp, color = Color.Gray) // Or use theme color
        Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

// Add Previews for different states if desired (Loading, Error, Success)
// @Preview(showBackground = true, name = "Current Weather Loading")
// @Composable
// fun CurrentWeatherCardLoadingPreview() {
//     WeatherAppTheme {
//         CurrentWeatherCard(weatherDataResult = NetworkResult.Loading)
//     }
// }
//
// @Preview(showBackground = true, name = "Current Weather Error")
// @Composable
// fun CurrentWeatherCardErrorPreview() {
//     WeatherAppTheme {
//         CurrentWeatherCard(weatherDataResult = NetworkResult.Error("Sample error message"))
//     }
// }
//
// @Preview(showBackground = true, name = "Current Weather Success")
// @Composable
// fun CurrentWeatherCardSuccessPreview() {
//     // Create a sample CurrentWeatherResponse for preview
//     val sampleData = CurrentWeatherResponse(...) // Fill with sample data
//     WeatherAppTheme {
//         CurrentWeatherCard(weatherDataResult = NetworkResult.Success(sampleData))
//     }
// }
