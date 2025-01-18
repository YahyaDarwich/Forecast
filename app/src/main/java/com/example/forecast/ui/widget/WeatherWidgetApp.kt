package com.example.forecast.ui.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.CircularProgressIndicator
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.provideContent
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.wrapContentSize
import androidx.glance.layout.wrapContentWidth
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextAlign
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.forecast.MainActivity
import com.example.forecast.R
import com.example.forecast.model.CurrentWeather
import com.example.forecast.ui.components.MaxMinTempWidget
import com.example.forecast.ui.screens.getWeatherColor
import com.example.forecast.ui.screens.getWeatherIcon
import kotlin.math.roundToInt


class WeatherWidgetApp : GlanceAppWidget() {
    override val stateDefinition = WeatherWidgetStateDefinition
    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        provideContent {
            Content()
        }
    }

    @Composable
    fun Content() {
        val state = currentState<WeatherWidgetState>()

        GlanceTheme {
            key(LocalSize.current) {
                when (state) {
                    is WeatherWidgetState.Loading ->
                        Box(
                            modifier = GlanceModifier.appWidgetBackground(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }

                    is WeatherWidgetState.Success -> {
                        Body(state.currentWeather)
                    }

                    is WeatherWidgetState.Error -> {
                        Refresh(state.message)
                    }
                }
            }
        }
    }

    @Composable
    fun Body(currentWeather: CurrentWeather) {
        val size = LocalSize.current
        val padding = if (size.width > 110.dp) 15.dp else 10.dp
        val textSize = if (size.width > 300.dp) 15.sp else 13.sp

        Box(
            modifier = GlanceModifier.background(
                ImageProvider(R.drawable.rectangle),
                colorFilter = ColorFilter.tint(
                    ColorProvider(
                        getWeatherColor(currentWeather)[1]
                    )
                )
            ).fillMaxSize().clickable(actionStartActivity<MainActivity>())
        ) {
            Column(
                modifier = GlanceModifier.fillMaxHeight().wrapContentWidth()
                    .padding(start = padding, bottom = padding),
                horizontalAlignment = Alignment.Horizontal.Start,
                verticalAlignment = Alignment.Vertical.CenterVertically
            ) {
                Text(
                    text = "${currentWeather.main.temp.roundToInt()}°",
                    style = TextStyle(
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorProvider(
                            Color.White
                        )
                    )
                )

                if (size.width > 150.dp) {
                    MaxMinTempWidget(
                        maxTemp = currentWeather.main.temp_max.roundToInt(),
                        minTemp = currentWeather.main.temp_min.roundToInt(),
                        modifier = GlanceModifier.fillMaxWidth(),
                        fontSize = 12.sp,
                        space = 5.dp,
                        color = ColorProvider(color = Color.White.copy(alpha = 0.8f)),
                        fontWeight = FontWeight.Normal
                    )
                }
            }

            Box(
                modifier = GlanceModifier.fillMaxSize(),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    provider = ImageProvider(
                        getWeatherIcon(
                            currentWeather.weather[0].id,
                            currentWeather.weather[0].icon
                        )
                    ),
                    contentDescription = "icon",
                    modifier = GlanceModifier.size(80.dp)
                )
            }

            if (size.width > 150.dp) {
                Box(
                    contentAlignment = Alignment.BottomStart,
                    modifier = GlanceModifier.fillMaxSize()
                        .padding(top = padding, bottom = padding, start = padding, end = 105.dp)
                ) {
                    Text(
                        text = currentWeather.name ?: "",
                        style = TextStyle(
                            fontSize = textSize,
                            color = ColorProvider(color = Color.White),
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1
                    )
                }
            }

            Box(
                contentAlignment = Alignment.BottomEnd,
                modifier = GlanceModifier.fillMaxSize().padding(padding)
            ) {
                Text(
                    text = currentWeather.weather[0].description,
                    style = TextStyle(
                        fontSize = textSize,
                        fontWeight = FontWeight.Medium,
                        color = ColorProvider(
                            Color.White
                        )
                    )
                )
            }
        }
    }

    @Composable
    fun Refresh(error: String) {
        Box(
            modifier = GlanceModifier.background(
                ColorProvider(
                    color = Color.Black.copy(
                        alpha = 0.5f
                    )
                )
            ).fillMaxSize()
        ) {
            Column(
                modifier = GlanceModifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = GlanceModifier.size(50.dp).clickable(
                        actionRunCallback<RefreshAction>()
                    ),
                    provider = ImageProvider(R.drawable.baseline_refresh_24),
                    contentDescription = "refresh"
                )

                Text(
                    text = error,
                    style = TextStyle(
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center,
                        color = ColorProvider(Color.White)
                    )
                )
            }
        }
    }
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WeatherWorker.enqueue(context, true)
    }
}