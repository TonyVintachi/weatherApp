package com.example.weatherapp.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.example.weatherapp.model.ForecastListItem
import com.example.weatherapp.model.ForecastResponse
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@Composable
fun ForecastView(
    forecastResult: NetworkResult<ForecastResponse>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.padding(vertical = 8.dp)) {
        Text(
            text = "5-Day Forecast",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            color = MaterialTheme.colorScheme.onSurface
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp), // Adjust height as needed
            contentAlignment = Alignment.Center
        ) {
            when (forecastResult) {
                is NetworkResult.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is NetworkResult.Success -> {
                    // Group by day and take one forecast per day (e.g., midday)
                    // OpenWeatherMap 5-day forecast gives data every 3 hours.
                    // We need to process this to show a daily summary.
                    val dailyForecasts = processForecastDataToDaily(forecastResult.data.list)
                    ForecastContent(dailyForecasts)
                }
                is NetworkResult.Error -> {
                    Text(
                        text = "Error: ${forecastResult.message}",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ForecastContent(dailyForecasts: List<DailyForecastSummary>, modifier: Modifier = Modifier) {
    if (dailyForecasts.isEmpty()) {
        Text("No forecast data available.", modifier = modifier.padding(16.dp))
        return
    }
    LazyRow(
        modifier = modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(dailyForecasts) { forecastItem ->
            ForecastItemCard(forecastItem)
        }
    }
}

@Composable
fun ForecastItemCard(forecast: DailyForecastSummary, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier.width(130.dp), // Fixed width for each item
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = forecast.dayOfWeek, // e.g., "Mon"
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = forecast.date, // e.g., "Oct 23"
                fontSize = 12.sp,
                color = Color.Gray // Or use theme color
            )
            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = rememberAsyncImagePainter( // Coil placeholder
                    model = "https://openweathermap.org/img/wn/${forecast.icon}@2x.png"
                ),
                contentDescription = forecast.condition,
                modifier = Modifier.size(50.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = forecast.condition,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "${forecast.maxTemp}°C / ${forecast.minTemp}°C",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

// Data class to hold processed daily summary
data class DailyForecastSummary(
    val dayOfWeek: String,
    val date: String,
    val minTemp: Int,
    val maxTemp: Int,
    val condition: String,
    val icon: String
)

// Helper function to process 3-hourly forecast data into daily summaries
fun processForecastDataToDaily(list: List<ForecastListItem>): List<DailyForecastSummary> {
    val sdfDay = SimpleDateFormat("E", Locale.getDefault()) // "Mon"
    val sdfDate = SimpleDateFormat("MMM d", Locale.getDefault()) // "Oct 23"
    sdfDay.timeZone = TimeZone.getTimeZone("UTC") // Assuming dt is UTC
    sdfDate.timeZone = TimeZone.getTimeZone("UTC")

    return list
        .groupBy { item ->
            // Group by date (ignoring time)
            val calendar = java.util.Calendar.getInstance(TimeZone.getTimeZone("UTC"))
            calendar.timeInMillis = item.dt * 1000L
            calendar.get(java.util.Calendar.DAY_OF_YEAR)
        }
        .mapNotNull { (_, itemsOnSameDay) ->
            if (itemsOnSameDay.isEmpty()) return@mapNotNull null

            // For simplicity, take the conditions from the first item of the day (e.g., midnight or earliest)
            // Or, one could choose the most frequent condition, or midday condition.
            val representativeItem = itemsOnSameDay.first() // Or choose more strategically
            val weatherCondition = representativeItem.weather.firstOrNull()

            val minTemp = itemsOnSameDay.minOfOrNull { it.main.tempMin }?.toInt() ?: 0
            val maxTemp = itemsOnSameDay.maxOfOrNull { it.main.tempMax }?.toInt() ?: 0

            DailyForecastSummary(
                dayOfWeek = sdfDay.format(Date(representativeItem.dt * 1000L)),
                date = sdfDate.format(Date(representativeItem.dt * 1000L)),
                minTemp = minTemp,
                maxTemp = maxTemp,
                condition = weatherCondition?.main ?: "N/A",
                icon = weatherCondition?.icon ?: "01d" // Default icon
            )
        }
        .take(5) // Ensure we only show 5 days
}

// Add Previews for ForecastView (Loading, Error, Success with sample data)
// Similar to CurrentWeatherUI, these can be implemented fully later.
