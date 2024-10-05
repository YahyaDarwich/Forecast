package com.example.forecast.data

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.glance.ColorFilter
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.LocalSize
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionParametersOf
import androidx.glance.action.actionStartActivity
import androidx.glance.action.clickable
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver
import androidx.glance.appwidget.SizeMode
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.components.Scaffold
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.background
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxHeight
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.size
import androidx.glance.layout.width
import androidx.glance.layout.wrapContentWidth
import androidx.glance.state.GlanceStateDefinition
import androidx.glance.state.PreferencesGlanceStateDefinition
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider
import com.example.forecast.MainActivity
import com.example.forecast.R
import com.example.forecast.WeatherApplication
import com.example.forecast.model.CurrentWeather
import com.example.forecast.ui.components.MaxMinTempWidget
import com.example.forecast.ui.screens.getWeatherColor
import com.example.forecast.ui.screens.getWeatherIcon
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

private val shouldRefreshKey = booleanPreferencesKey("refresh-key")
private val refreshParamKey = ActionParameters.Key<Boolean>("refresh-key")

class WeatherWidgetApp : GlanceAppWidget() {
    override val stateDefinition: GlanceStateDefinition<*>?
        get() = PreferencesGlanceStateDefinition
    override val sizeMode: SizeMode
        get() = SizeMode.Exact

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val application = context.applicationContext as WeatherApplication
        val weatherAppRepository = application.container.weatherAppRepository
        var isErrorLoading = false

        val lat = "34.38586232938427"
        val long = "36.009997289005724"

        var currentWeather: CurrentWeather?

        try {
            currentWeather = withContext(Dispatchers.IO) {
                weatherAppRepository.getCurrentWeather(lat, long)
            }
        } catch (e: Exception) {
            currentWeather = null
            isErrorLoading = true
        }

        provideContent {
            var shouldRefresh = currentState<Preferences>()[shouldRefreshKey] ?: false

            GlanceTheme {
                key(LocalSize.current) {
                    if (!isErrorLoading && currentWeather != null) Body(currentWeather)
                    else {
                        RefreshAction(shouldRefresh)
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
                        getWeatherColor(
                            id = currentWeather.weather[0].id,
                            iconCode = currentWeather.weather[0].icon,
                            temperature = currentWeather.main.temp.roundToInt()
                        )[1]
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
    fun RefreshAction(refresh: Boolean) {
        Box(
            contentAlignment = Alignment.Center, modifier = GlanceModifier.background(
                ColorProvider(
                    color = Color.Black.copy(
                        alpha = 0.5f
                    )
                )
            ).fillMaxSize()
        ) {
            Image(
                modifier = GlanceModifier.size(50.dp).clickable(
                    actionRunCallback<RefreshAction>(
                        parameters = actionParametersOf(refreshParamKey to !refresh)
                    )
                ),
                provider = ImageProvider(R.drawable.baseline_refresh_24),
                contentDescription = "refresh"
            )
        }
    }
}

class WeatherWidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget
        get() = WeatherWidgetApp()
}

class RefreshAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        val refresh = requireNotNull(parameters[refreshParamKey])

        updateAppWidgetState(
            context = context,
            definition = PreferencesGlanceStateDefinition,
            glanceId = glanceId
        ) {
            it.toMutablePreferences()
                .apply {
                    this[shouldRefreshKey] = !refresh
                }
        }

        WeatherWidgetApp().update(context, glanceId)
    }
}