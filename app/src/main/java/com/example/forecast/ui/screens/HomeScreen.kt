package com.example.forecast.ui.screens

import android.content.res.Configuration
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R
import com.example.forecast.model.CurrentWeather
import com.example.forecast.ui.components.AiTodayWeatherReport
import com.example.forecast.ui.components.ComingDaysForecast
import com.example.forecast.ui.components.FeelsLike
import com.example.forecast.ui.components.HomeMainTempInfo
import com.example.forecast.ui.components.HomeShortWeatherInfo
import com.example.forecast.ui.components.Humidity
import com.example.forecast.ui.components.IconLabelItem
import com.example.forecast.ui.components.MinMaxTemp
import com.example.forecast.ui.components.RainFall
import com.example.forecast.ui.components.TodayForecast
import com.example.forecast.ui.components.Wind
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import kotlin.math.roundToInt

@Composable
fun HomeScreen(
    weatherUIState: WeatherUIState,
    todayWeatherReportUiState: TodayWeatherReportUiState,
    paddingValues: PaddingValues
) {
    when (weatherUIState) {
        is WeatherUIState.Loading -> LoadingBody()
        is WeatherUIState.Success ->
            HomeBody(
                Modifier.padding(paddingValues),
                weatherUIState.currentWeather,
                weatherUIState.todayForecast,
                weatherUIState.upcomingDaysForecast,
                todayWeatherReportUiState
            )

        is WeatherUIState.Error -> Text(text = "error")
    }
}

@Composable
fun HomeBody(
    modifier: Modifier,
    currentWeather: CurrentWeather,
    todayForecast: List<CurrentWeather>,
    upcomingDaysForecast: Map<String, MutableList<CurrentWeather>>,
    todayWeatherReportUiState: TodayWeatherReportUiState
) {
    val scrollState = rememberScrollState()

    val colors = getWeatherColor(
        id = currentWeather.weather[0].id,
        iconCode = currentWeather.weather[0].icon,
        temperature = currentWeather.main.temp.roundToInt()
    ).map { colorResource(id = it) }

    SetStatusBarColor(color = colors[0])

    val overlayColor = getWeatherOverlayColor(
        id = currentWeather.weather[0].id,
        iconCode = currentWeather.weather[0].icon,
        temperature = currentWeather.main.temp.roundToInt()
    )

    val itemsOverlayColor = overlayColor.copy(0.5f)

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
                color = itemsOverlayColor,
                textColor = Color.White,
                elevation = 0.dp,
                modifier = Modifier
                    .align(Alignment.Start)
                    .size(100.dp, 75.dp)
                    .padding(bottom = dimensionResource(id = R.dimen.padding_extra_extra_large))
            )

            AiTodayWeatherReport(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .padding(
                        bottom = dimensionResource(id = R.dimen.padding_mid_large)
                    ),
                cornerRadius = cornerRadius,
                backgroundColor = itemsOverlayColor,
                loadingColor = overlayColor,
                collapsedMaxLines = 3,
                isLoading = todayWeatherReportUiState == TodayWeatherReportUiState.Loading,
                weatherReport = when (todayWeatherReportUiState) {
                    is TodayWeatherReportUiState.Success -> todayWeatherReportUiState.summaryReport
                        ?: "No report"

                    else -> "No report"
                }
            )

            HomeShortWeatherInfo(
                feelsLike = currentWeather.main.feels_like.roundToInt(),
                humidity = currentWeather.main.humidity,
                pressure = currentWeather.main.pressure,
                cornerRadius = cornerRadius,
                color = itemsOverlayColor,
                dividerColor = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .wrapContentHeight()
                    .padding(bottom = dimensionResource(id = R.dimen.padding_mid_large))
            )

            TodayForecast(
                todayForecast = todayForecast,
                modifier = Modifier
                    .wrapContentHeight()
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
                currentWeather = currentWeather.weather[0].description,
                color = itemsOverlayColor,
                cornerRadius = cornerRadius,
                itemsBackground = Brush.verticalGradient(colors),
                currentIconWeather = getWeatherIcon(
                    currentWeather.weather[0].id,
                    currentWeather.weather[0].icon
                )
            )

            ComingDaysForecast(
                daysForecast = upcomingDaysForecast,
                color = itemsOverlayColor,
                cornerRadius = cornerRadius,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(332.dp)
                    .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
            )

            GridWeatherInfo(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(0.9f)
                    .padding(bottom = dimensionResource(id = R.dimen.padding_medium)),
                currentWeather = currentWeather,
                itemsOverlayColor = itemsOverlayColor,
                cornerRadius = cornerRadius
            )
        }
    }
}

@Composable
fun GridWeatherInfo(
    modifier: Modifier,
    currentWeather: CurrentWeather,
    itemsOverlayColor: Color,
    cornerRadius: Dp
) {
    var humidityItemVisibility by remember {
        mutableStateOf(false)
    }

    var humidityItemHasBeenVisible by remember {
        mutableStateOf(false)
    }

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small)),
    ) {
        item {
            Humidity(
                modifier = Modifier
                    .onGloballyPositioned {
                        if (!humidityItemHasBeenVisible &&
                            it.boundsInWindow().bottom > -it.boundsInWindow().height / 2
                        ) {
                            humidityItemVisibility = true
                            humidityItemHasBeenVisible = true
                        }
                    }
                    .fillMaxWidth()
                    .aspectRatio(1f),
                humidity = currentWeather.main.humidity,
                backgroundColor = itemsOverlayColor,
                progressBackgroundColor = colorResource(id = R.color.humidity_progress_background),
                progressColor = colorResource(id = R.color.humidity_progress),
                cornerRadius = cornerRadius,
                isVisible = humidityItemVisibility
            )
        }

        item {
            Wind(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f), // square
                degree = currentWeather.wind.deg.toFloat(),
                cornerRadius = cornerRadius,
                backgroundColor = itemsOverlayColor,
                speed = (currentWeather.wind.speed * 3.6).roundToInt(),
            )
        }

        item {
            FeelsLike(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                feelsLike = currentWeather.main.feels_like.toInt(),
                backgroundColor = itemsOverlayColor,
                cornerRadius = cornerRadius
            )
        }

        item {
            if ((currentWeather.rain?.`1h` ?: 0f) > 0f) {
                currentWeather.rain?.`1h`?.let {
                    RainFall(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        rainfall = it,
                        backgroundColor = itemsOverlayColor,
                        cornerRadius = cornerRadius
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun LoadingBody() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = dimensionResource(id = R.dimen.padding_medium))
    ) {
        Box(
            modifier = Modifier
                .padding(top = dimensionResource(id = R.dimen.padding_extra_large))
                .align(Alignment.Start)
                .clip(RoundedCornerShape(10.dp))
                .size(100.dp, 20.dp)
                .shimmerEffect()
        )

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_extra_large))
                .fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )

            Box(
                modifier = Modifier
                    .size(100.dp, 80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .shimmerEffect()
            )
        }

        Box(
            modifier = Modifier
                .padding(bottom = dimensionResource(id = R.dimen.padding_extra_extra_large))
                .align(Alignment.Start)
                .clip(RoundedCornerShape(50.dp))
                .size(100.dp, 40.dp)
                .shimmerEffect()
        )

        Row(
            modifier = Modifier
                .padding(
                    bottom = dimensionResource(id = R.dimen.padding_mid_large)
                )
                .height(110.dp)
                .fillMaxWidth(0.9f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp, 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )

                Box(
                    modifier = Modifier
                        .size(50.dp, 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp, 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )

                Box(
                    modifier = Modifier
                        .size(50.dp, 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp, 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )

                Box(
                    modifier = Modifier
                        .size(50.dp, 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
            }
        }

        Column(
            modifier = Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(bottom = dimensionResource(id = R.dimen.padding_medium))
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = dimensionResource(id = R.dimen.padding_small),
                        horizontal = dimensionResource(
                            id = R.dimen.padding_medium
                        )
                    )
            ) {
                Box(
                    modifier = Modifier
                        .size(100.dp, 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )

                Box(
                    modifier = Modifier
                        .size(70.dp, 20.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .shimmerEffect()
                )
            }

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(
                        vertical = dimensionResource(
                            id = R.dimen.padding_small
                        )
                    )
            ) {
                items(5) {
                    Box(
                        modifier = Modifier
                            .size(65.dp, 150.dp)
                            .padding(horizontal = 5.dp)
                            .clip(RoundedCornerShape(25.dp))
                            .shimmerEffect()
                    )
                }
            }
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
fun SetStatusBarColor(color: Color) {
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(color = color, darkIcons = color.luminance() > 0.5f)
    }
}

@Composable
@ReadOnlyComposable
fun getWeatherColor(id: Int, iconCode: String, temperature: Int): List<Int> {
    return when (id) {
        800 -> if (iconCode.contains("d")) listOf(
            R.color.clear_day_1, R.color.clear_day_2
        ) else if (temperature >= 45) {
            listOf(
                R.color.clear_night_1_very_hot,
                R.color.clear_night_2_very_hot
            )
        } else if (temperature >= 25) {
            listOf(
                R.color.clear_night_1_hot,
                R.color.clear_night_2_hot
            )
        } else {
            listOf(
                R.color.clear_night_1_cold,
                R.color.clear_night_2_cold
            )
        }

        803 -> listOf(
            R.color.mostly_cloud_day_1,
            R.color.mostly_cloud_day_2
        )

        801, 802, 804 -> if (iconCode.contains("d")) listOf(
            R.color.cloudy_day_1, R.color.cloudy_day_2
        ) else if (temperature >= 25) {
            listOf(
                R.color.cloudy_night_1_hot,
                R.color.cloudy_night_2_hot
            )
        } else {
            listOf(
                R.color.cloudy_night_1_cold,
                R.color.cloudy_night_2_cold
            )
        }

        in 511..531 -> listOf(
            R.color.drizzle_day_1,
            R.color.drizzle_day_2
        )

        in 300..321 -> listOf(
            R.color.drizzle_day_1,
            R.color.drizzle_day_2
        )

        in 500..504 -> listOf(
            R.color.drizzle_day_1,
            R.color.drizzle_day_2
        )

        200, 201, 210, 212, 221, 230, 231, 232, 202, 211 -> listOf(
            R.color.drizzle_day_1,
            R.color.drizzle_day_2
        )

        622, 620, 621, 600, 601, 602, 611, 612, 613, 615, 616 -> listOf(
            R.color.snow_day_1,
            R.color.snow_day_2
        )

        else -> listOf(
            R.color.haze_day_1,
            R.color.haze_day_2
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

fun Modifier.shimmerEffect(): Modifier = composed {
    customShimmerEffect(
        colors = listOf(
            shimmerColor(),
            shimmerColor().copy(0.5f),
            shimmerColor()
        ), fromTopStart = true
    )
}

fun Modifier.customShimmerEffect(
    colors: List<Color>,
    fromTopStart: Boolean,
    animationDuration: Int = 1000
): Modifier = composed {
    var size by remember {
        mutableStateOf(IntSize.Zero)
    }

    val transition = rememberInfiniteTransition(label = "shimmer effect")
    val startOffsetX by transition.animateFloat(
        initialValue = -2 * size.width.toFloat(),
        targetValue = 2 * size.width.toFloat(),
        animationSpec = infiniteRepeatable(animation = tween(animationDuration)),
        label = "shimmer effect"
    )

    background(
        brush = Brush.linearGradient(
            colors = colors,
            start = Offset(startOffsetX, if (fromTopStart) 0f else size.height.toFloat()),
            end = Offset(startOffsetX + size.width.toFloat(), size.height.toFloat())
        )
    ).onGloballyPositioned { size = it.size }
}

@Composable
fun shimmerColor(): Color {
    val context = LocalContext.current
    val uiMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

    return when (uiMode) {
        Configuration.UI_MODE_NIGHT_YES -> colorResource(id = R.color.shimmer_dark)
        else -> colorResource(id = R.color.shimmer_light)
    }
}