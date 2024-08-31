package com.example.forecast.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R
import com.example.forecast.model.CurrentWeather
import com.example.forecast.ui.components.ComingDaysForecast
import com.example.forecast.ui.components.HomeMainTempInfo
import com.example.forecast.ui.components.HomeShortWeatherInfo
import com.example.forecast.ui.components.IconLabelItem
import com.example.forecast.ui.components.MinMaxTemp
import com.example.forecast.ui.components.TodayForecast
import kotlin.math.roundToInt

@Composable
fun HomeScreen(weatherUIState: WeatherUIState, paddingValues: PaddingValues) {
    when (weatherUIState) {
        is WeatherUIState.Loading -> Text(text = "loading")
        is WeatherUIState.Success -> HomeBody(
            Modifier.padding(paddingValues),
            weatherUIState.currentWeather,
            weatherUIState.todayForecast,
            weatherUIState.upcomingDaysForecast
        )

        is WeatherUIState.Error -> Text(text = "error")
    }
}

@Composable
fun HomeBody(
    modifier: Modifier,
    currentWeather: CurrentWeather,
    todayForecast: List<CurrentWeather>,
    upcomingDaysForecast: Map<String, MutableList<CurrentWeather>>
) {
    val scrollState = rememberScrollState()

    val colors = getWeatherColor(
        id = currentWeather.weather[0].id,
        iconCode = currentWeather.weather[0].icon,
        temperature = currentWeather.main.temp.roundToInt()
    )

    val overlayColor = getWeatherOverlayColor(
        id = currentWeather.weather[0].id,
        iconCode = currentWeather.weather[0].icon,
        temperature = currentWeather.main.temp.roundToInt()
    ).copy(0.5f)

    val cornerRadius = 25.dp

    Box(
        modifier = modifier
            .drawWithCache {
                onDrawBehind {
                    val brush = Brush.verticalGradient(colors)
                    drawRect(brush, blendMode = BlendMode.Multiply)
                }
            }
            .background(Color.Transparent)
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            IconLabelItem(
                label = currentWeather.name ?: "",
                fontSize = 15.sp,
                iconRes = R.drawable.baseline_place_24,
                iconDesc = "place",
                modifier = Modifier
                    .padding(top = dimensionResource(id = R.dimen.padding_large))
                    .align(Alignment.Start),
                iconModifier = Modifier
                    .padding(end = 5.dp)
                    .size(14.dp)
            )

            HomeMainTempInfo(
                temperature = currentWeather.main.temp.roundToInt(),
                weatherIcon = getWeatherIcon(
                    currentWeather.weather[0].id,
                    currentWeather.weather[0].icon
                ),
                modifier = Modifier.fillMaxWidth(),
                iconModifier = Modifier.size(120.dp)
            )

            MinMaxTemp(
                currentWeather.main.temp_max.roundToInt(),
                currentWeather.main.temp_min.roundToInt(),
                color = overlayColor,
                textColor = Color.White,
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(100.dp, 75.dp)
                    .padding(bottom = dimensionResource(id = R.dimen.padding_extra_extra_large))
            )

            HomeShortWeatherInfo(
                feelsLike = currentWeather.main.feels_like.roundToInt(),
                humidity = currentWeather.main.humidity,
                pressure = currentWeather.main.pressure,
                cornerRadius = cornerRadius,
                color = overlayColor,
                dividerColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .size(330.dp, 110.dp)
                    .padding(bottom = dimensionResource(id = R.dimen.padding_mid_large))
            )

            TodayForecast(
                todayForecast = todayForecast,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
                currentWeather = currentWeather.weather[0].description,
                color = overlayColor,
                cornerRadius = cornerRadius,
                itemsBackground = Brush.verticalGradient(colors),
                currentIconWeather = getWeatherIcon(
                    currentWeather.weather[0].id,
                    currentWeather.weather[0].icon
                )
            )

            ComingDaysForecast(
                daysForecast = upcomingDaysForecast,
                color = overlayColor,
                cornerRadius = cornerRadius,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(332.dp)
                    .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            )
        }
    }
}

fun getWeatherIcon(id: Int, iconCode: String): Int {
    return when (id) {
        803 -> if (iconCode.contentEquals("04d")) R.drawable.mostly_cloudy else R.drawable.mostly_cloudy_night
        801 -> if (iconCode.contentEquals("02d")) R.drawable.clear_cloudy else R.drawable.clear_cloudy_night
        804 -> R.drawable.partly_cloudy
        in 511..531 -> if (iconCode.contentEquals("09d") || iconCode.contentEquals("13d")) R.drawable.drizzle else R.drawable.drizzle_night
        in 300..321 -> if (iconCode.contentEquals("09d")) R.drawable.drizzle else R.drawable.drizzle_night
        in 500..504 -> if (iconCode.contentEquals("10d")) R.drawable.drizzle_sunny else R.drawable.drizzle_night
        802 -> R.drawable.cloudy
        202, 211 -> R.drawable.thunderstroms_2
        210, 212, 221, 230, 231, 232 -> R.drawable.thunderstroms
        200, 201 -> if (iconCode.contentEquals("11d")) R.drawable.thunderstroms_sunny else R.drawable.thunderstroms_sunny_night
        611, 612, 613, 615, 616 -> R.drawable.sleet
        600, 601, 602 -> R.drawable.snow
        620, 621 -> R.drawable.snow_flurries
        622 -> R.drawable.hail
        800 -> if (iconCode.contentEquals("01d")) R.drawable.sunny else R.drawable.night
        781 -> R.drawable.tornado
        741 -> R.drawable.fog
        else -> R.drawable.mist
    }
}

@Composable
@ReadOnlyComposable
fun getWeatherColor(id: Int, iconCode: String, temperature: Int): List<Color> {
    return when (id) {
        800 -> if (iconCode.contains("d")) listOf(
            colorResource(id = R.color.clear_day_1), colorResource(id = R.color.clear_day_2)
        ) else if (temperature >= 45) {
            listOf(
                colorResource(id = R.color.clear_night_1_very_hot),
                colorResource(id = R.color.clear_night_2_very_hot)
            )
        } else if (temperature >= 25) {
            listOf(
                colorResource(id = R.color.clear_night_1_hot),
                colorResource(id = R.color.clear_night_2_hot)
            )
        } else {
            listOf(
                colorResource(id = R.color.clear_night_1_cold),
                colorResource(id = R.color.clear_night_2_cold)
            )
        }

        803 -> listOf(
            colorResource(id = R.color.mostly_cloud_day_1),
            colorResource(id = R.color.mostly_cloud_day_2)
        )

        801, 802, 804 -> if (iconCode.contains("d")) listOf(
            colorResource(id = R.color.cloudy_day_1), colorResource(id = R.color.cloudy_day_2)
        ) else if (temperature >= 25) {
            listOf(
                colorResource(id = R.color.cloudy_night_1_hot),
                colorResource(id = R.color.cloudy_night_2_hot)
            )
        } else {
            listOf(
                colorResource(id = R.color.cloudy_night_1_cold),
                colorResource(id = R.color.cloudy_night_2_cold)
            )
        }

        in 511..531 -> listOf(
            colorResource(id = R.color.drizzle_day_1),
            colorResource(id = R.color.drizzle_day_2)
        )

        in 300..321 -> listOf(
            colorResource(id = R.color.drizzle_day_1),
            colorResource(id = R.color.drizzle_day_2)
        )

        in 500..504 -> listOf(
            colorResource(id = R.color.drizzle_day_1),
            colorResource(id = R.color.drizzle_day_2)
        )

        200, 201, 210, 212, 221, 230, 231, 232, 202, 211 -> listOf(
            colorResource(id = R.color.drizzle_day_1),
            colorResource(id = R.color.drizzle_day_2)
        )

        622, 620, 621, 600, 601, 602, 611, 612, 613, 615, 616 -> listOf(
            colorResource(id = R.color.snow_day_1),
            colorResource(id = R.color.snow_day_2)
        )

        else -> listOf(
            colorResource(id = R.color.haze_day_1),
            colorResource(id = R.color.haze_day_2)
        )
    }
}

@Composable
@ReadOnlyComposable
fun getWeatherOverlayColor(id: Int, iconCode: String, temperature: Int): Color {
    return when (id) {
        800 -> if (iconCode.contains("d")) colorResource(id = R.color.clear_background_day)
        else if (temperature >= 45)
            colorResource(id = R.color.clear_background_night_very_hot)
        else if (temperature >= 25)
            colorResource(id = R.color.clear_background_night_hot)
        else
            colorResource(id = R.color.clear_background_night_cold)

        803 -> colorResource(id = R.color.mostly_cloudy_background_day)

        801, 802, 804 -> if (iconCode.contains("d")) colorResource(id = R.color.cloudy_background_day)
        else if (temperature >= 25)
            colorResource(id = R.color.cloudy_background_night_hot)
        else
            colorResource(id = R.color.cloudy_background_night_cold)

        in 511..531 -> colorResource(id = R.color.drizzle_background_day)

        in 300..321 -> colorResource(id = R.color.drizzle_background_day)

        in 500..504 -> colorResource(id = R.color.drizzle_background_day)

        200, 201, 210, 212, 221, 230, 231, 232, 202, 211 -> colorResource(id = R.color.drizzle_background_day)

        622, 620, 621, 600, 601, 602, 611, 612, 613, 615, 616 -> colorResource(id = R.color.snow_background_day)

        else -> colorResource(id = R.color.haze_background_day)
    }
}