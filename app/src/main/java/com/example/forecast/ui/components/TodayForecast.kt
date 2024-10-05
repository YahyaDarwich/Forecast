package com.example.forecast.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R
import com.example.forecast.model.CurrentWeather
import com.example.forecast.ui.screens.getWeatherIcon
import com.example.forecast.utils.DateHelper
import java.util.Locale


@Composable
fun TodayForecast(
    todayForecast: List<CurrentWeather>,
    currentWeather: String,
    itemsBackground: Brush,
    currentIconWeather: Int, color: Color,
    cornerRadius: Dp,
    modifier: Modifier
) {
    Card(
        modifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
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
                IconLabelItem(
                    label = "3 Hours forecast",
                    fontSize = 12.sp,
                    iconRes = R.drawable.baseline_access_time_24,
                    iconDesc = "3 Hours forecast",
                    fontWeight = FontWeight.SemiBold,
                    iconModifier = Modifier
                        .size(22.dp, 15.dp)
                        .padding(end = 5.dp)
                )

                IconLabelItem(
                    label = currentWeather.capitalize(Locale.getDefault()),
                    fontSize = 12.sp,
                    iconRes = currentIconWeather,
                    iconDesc = "weatherDescription",
                    fontWeight = FontWeight.SemiBold,
                    iconModifier = Modifier
                        .size(30.dp, 20.dp)
                        .padding(end = 5.dp)
                )
            }
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
            items(items = todayForecast, key = { it.dt_txt }) {
                TimeWeatherInfoItem(
                    DateHelper.formatDate(it.dt_txt).uppercase(Locale.getDefault()),
                    getWeatherIcon(it.weather[0].id, it.weather[0].icon),
                    it.main.feels_like.toInt(),
                    it.main.humidity,
                    background = itemsBackground,
                    textColor = Color.White,
                    elevation = 3.dp,
                    cornerSize = 25.dp,
                    modifier = Modifier
                        .size(65.dp, 150.dp)
                        .padding(horizontal = 5.dp)
                )
            }
        }
    }
}

@Composable
fun TimeWeatherInfoItem(
    time: String,
    weatherIcon: Int,
    temp: Int,
    humidity: Int,
    textColor: Color,
    background: Brush,
    cornerSize: Dp,
    elevation: Dp,
    modifier: Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(cornerSize),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
                .background(background)
        ) {
            Text(
                text = time,
                color = textColor,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
            Image(painter = painterResource(id = weatherIcon), contentDescription = time)
            Text(text = "$temp°", color = textColor, fontWeight = FontWeight.SemiBold)
            Text(
                text = "$humidity%",
                fontSize = 12.sp,
                color = textColor,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

//@Preview
@Composable
private fun PreviewTimeWeatherInfoItem() {
    TimeWeatherInfoItem(
        "10 AM",
        R.drawable.baseline_place_24,
        10, humidity = 85, elevation = 1.dp,
        cornerSize = 20.dp,
        textColor = Color.White,
        background = Brush.verticalGradient(),
        modifier = Modifier
            .size(60.dp, 150.dp)
            .background(Color.Red, RoundedCornerShape(30.dp))
    )
}