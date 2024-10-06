package com.example.forecast.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R
import java.util.Locale

@Composable
fun HomeShortWeatherInfo(
    feelsLike: Int,
    humidity: Int,
    pressure: Int,
    cornerRadius: Dp,
    color: Color,
    dividerColor: Color,
    modifier: Modifier
) {
    Box(modifier) {
        Card(
            Modifier
                .fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = color),
//            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
            shape = RoundedCornerShape(cornerRadius)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = dimensionResource(id = R.dimen.padding_large))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconLabelItem(
                        label = "feels like".capitalize(Locale.getDefault()),
                        fontSize = 13.sp,
                        iconRes = R.drawable.feelslike,
                        iconDesc = "temp feels like",
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier,
                        iconModifier = Modifier
                            .size(14.dp)
                            .padding(end = 5.dp)
                    )

                    Text(text = "${feelsLike}°")
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(dividerColor)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconLabelItem(
                        label = "humidity".capitalize(Locale.getDefault()),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        iconRes = R.drawable.humidity,
                        iconDesc = "humidity",
                        modifier = Modifier,
                        iconModifier = Modifier
                            .size(14.dp)
                            .padding(end = 5.dp)
                    )

                    Text(text = "${humidity}%")
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(20.dp)
                        .background(dividerColor)
                )

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    IconLabelItem(
                        label = "pressure".capitalize(Locale.getDefault()),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        iconRes = R.drawable.pressure,
                        iconDesc = "pressure",
                        modifier = Modifier,
                        iconModifier = Modifier
                            .size(14.dp)
                            .padding(end = 5.dp)
                    )

                    Text(text = "$pressure hPa")
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewHomeShortWeatherInfo() {
    HomeShortWeatherInfo(
        20,
        50,
        1114,
        20.dp,
        Color.DarkGray,
        dividerColor = Color.White,
        Modifier.size(300.dp, 70.dp)
    )
}