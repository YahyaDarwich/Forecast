package com.example.forecast.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R
import com.example.forecast.model.CurrentWeather
import com.example.forecast.ui.screens.getWeatherIcon
import com.example.forecast.utils.DateHelper
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun ComingDaysForecast(
    daysForecast: Map<String, MutableList<CurrentWeather>>,
    color: Color,
    cornerRadius: Dp,
    modifier: Modifier
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = color),
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier
    ) {
        LazyColumn(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_small))
        ) {
            itemsIndexed(items = daysForecast.toList()) { index, (date, weatherList) ->
                val weather1 = weatherList[0]
                val weather2 = if (weatherList.size > 1) weatherList[1] else null

                ComingDayItem(
                    dayName = if (index == 0) "today" else DateHelper.formatDate(
                        weather1.dt_txt,
                        outputPattern = "EEEE"
                    ),
                    humidity = (weather1.main.humidity + (weather2?.main?.humidity ?: 0)) / 2,
                    maxTempWeatherIcon = getWeatherIcon(
                        weather1.weather[0].id,
                        weather1.weather[0].icon
                    ),
                    minTempWeatherIcon = weather2?.weather?.get(0)?.let {
                        getWeatherIcon(
                            it.id, it.icon
                        )
                    },
                    maxWeatherTemp = weather1.main.temp.roundToInt(),
                    minWeatherTemp = weather2?.main?.temp?.roundToInt()
                )
            }
        }
    }
}

@Composable
fun ComingDayItem(
    dayName: String,
    humidity: Int,
    maxTempWeatherIcon: Int,
    minTempWeatherIcon: Int?,
    maxWeatherTemp: Int,
    minWeatherTemp: Int?,
    textColor: Color = Color.White
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = dayName.capitalize(Locale.getDefault()),
            color = textColor,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.weight(2f)
        )

        Text(
            text = "$humidity%", fontSize = 12.sp,
            modifier = Modifier.weight(1f), color = textColor
        )

        Row(Modifier.weight(2f), horizontalArrangement = Arrangement.Center) {
            Image(
                painter = painterResource(id = maxTempWeatherIcon),
                contentDescription = maxWeatherTemp.toString(),
                modifier = Modifier
                    .size(50.dp)
            )

            if (minTempWeatherIcon != null) {
                Image(
                    painter = painterResource(id = minTempWeatherIcon),
                    contentDescription = minWeatherTemp.toString(),
                    modifier = Modifier
                        .size(50.dp)
                )
            }
        }

        Row(Modifier.weight(1f), horizontalArrangement = Arrangement.Center) {
            Text(
                text = "$maxWeatherTemp°", color = textColor, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f), textAlign = TextAlign.Center
            )

            if (minWeatherTemp != null) {
                Text(
                    text = "$minWeatherTemp°",
                    color = textColor,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

//@Preview
@Composable
private fun PreviewComingDayItem() {
    ComingDayItem(
        dayName = "today",
        humidity = 25,
        maxTempWeatherIcon = R.drawable.drizzle,
        minTempWeatherIcon = R.drawable.thunderstroms_sunny,
        maxWeatherTemp = 22,
        minWeatherTemp = 12
    )
}