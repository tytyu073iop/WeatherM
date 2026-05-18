package com.example.weatherm.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weatherm.getPlatform
import com.example.weatherm.ui.CityWeatherState
import com.example.weatherm.data.local.CityEntity
import coil3.compose.SubcomposeAsyncImage

fun Modifier.shimmerEffect(): Modifier = composed {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmer_animation"
    )

    val shimmerColors = listOf(
        Color.LightGray.copy(alpha = 0.6f),
        Color.LightGray.copy(alpha = 0.2f),
        Color.LightGray.copy(alpha = 0.6f),
    )

    val brush = Brush.linearGradient(
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim, y = translateAnim)
    )

    return@composed this.then(background(brush))
}

@Composable
fun CityCardSkeleton() {
    AdaptiveWeatherCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Box(modifier = Modifier.fillMaxWidth(0.5f).height(24.dp).shimmerEffect())
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.fillMaxWidth(0.3f).height(16.dp).shimmerEffect())
            }
            Box(modifier = Modifier.size(64.dp).shimmerEffect())
        }
    }
}

@Composable
fun WeatherDetailsSkeleton() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(modifier = Modifier.size(100.dp, 60.dp).shimmerEffect())
        Spacer(modifier = Modifier.height(16.dp))
        Box(modifier = Modifier.size(150.dp, 30.dp).shimmerEffect())
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            repeat(3) {
                Box(modifier = Modifier.size(60.dp, 40.dp).shimmerEffect())
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Box(modifier = Modifier.size(200.dp, 30.dp).shimmerEffect())
        
        Spacer(modifier = Modifier.height(16.dp))

        Row {
            repeat(4) {
                Box(modifier = Modifier.size(100.dp, 120.dp).padding(8.dp).shimmerEffect())
            }
        }
    }
}

@Composable
fun AdaptiveWeatherCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val platform = getPlatform().name.lowercase()
    
    when {
        platform.contains("android") -> {
            Card(
                modifier = modifier.padding(8.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                content = { content() }
            )
        }
        platform.contains("ios") -> {
            Card(
                modifier = modifier.padding(8.dp),
                shape = RoundedCornerShape(0.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, Color.LightGray),
                content = { content() }
            )
        }
        platform.contains("linux") || platform.contains("jvm") -> {
            Card(
                modifier = modifier.padding(8.dp),
                shape = RoundedCornerShape(4.dp),
                border = BorderStroke(1.dp, Color.Black),
                content = { content() }
            )
        }
        else -> { // Web/Other
            Card(
                modifier = modifier.padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                content = { content() }
            )
        }
    }
}

@Composable
fun AdaptiveSearchField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Search city..."
) {
    val platform = getPlatform().name.lowercase()
    
    if (platform.contains("ios")) {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth().padding(8.dp),
            placeholder = { Text(placeholder) },
            shape = RoundedCornerShape(20.dp),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.LightGray.copy(alpha = 0.3f),
                unfocusedContainerColor = Color.LightGray.copy(alpha = 0.3f)
            )
        )
    } else {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = modifier.fillMaxWidth().padding(8.dp),
            placeholder = { Text(placeholder) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) }
        )
    }
}

@Composable
fun AdaptiveCityList(
    cities: List<CityWeatherState>,
    onCityClick: (CityEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    val platform = getPlatform().name.lowercase()
    
    if (platform.contains("web")) {
        BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
            val columns = when {
                maxWidth < 600.dp -> 1
                maxWidth < 900.dp -> 2
                else -> 3
            }
            
            Column {
                for (i in cities.indices step columns) {
                    Row(Modifier.fillMaxWidth()) {
                        for (j in 0 until columns) {
                            val index = i + j
                            if (index < cities.size) {
                                val cityState = cities[index]
                                Box(Modifier.weight(1f)) {
                                    CityCard(cityState, onCityClick)
                                }
                            } else {
                                Spacer(Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }
    } else {
        androidx.compose.foundation.lazy.LazyColumn(modifier = modifier) {
            items(cities) { cityState ->
                CityCard(cityState, onCityClick)
            }
        }
    }
}

@Composable
fun CityCard(
    cityState: CityWeatherState,
    onCityClick: (CityEntity) -> Unit
) {
    AdaptiveWeatherCard(
        modifier = Modifier.fillMaxWidth().clickable { onCityClick(cityState.entity) }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(cityState.entity.name, style = MaterialTheme.typography.headlineMedium)
                Text(cityState.currentWeather?.weather?.firstOrNull()?.description ?: "No data")
            }
            
            cityState.currentWeather?.main?.temp?.let { temp ->
                Text("${temp.toInt()}°C", style = MaterialTheme.typography.displaySmall)
            }

            cityState.currentWeather?.weather?.firstOrNull()?.icon?.let { icon ->
                SubcomposeAsyncImage(
                    model = "https://openweathermap.org/img/wn/$icon@2x.png",
                    contentDescription = null,
                    modifier = Modifier.size(64.dp),
                    loading = {
                        Box(modifier = Modifier.fillMaxSize().shimmerEffect())
                    }
                )
            }
        }
    }
}

@Composable
fun IosCitySelector(
    cities: List<CityEntity>,
    selectedCity: CityEntity?,
    onCitySelected: (CityEntity) -> Unit
) {
    if (cities.isEmpty()) return

    val selectedIndex = cities.indexOfFirst { it.name == selectedCity?.name }.coerceAtLeast(0)
    
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        edgePadding = 16.dp,
        containerColor = Color.Transparent,
        divider = {}
    ) {
        cities.forEachIndexed { index, city ->
            Tab(
                selected = selectedIndex == index,
                onClick = { onCitySelected(city) },
                text = { Text(city.name) }
            )
        }
    }
}
