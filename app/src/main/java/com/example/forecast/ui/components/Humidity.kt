package com.example.forecast.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R
import java.util.Locale

@Composable
fun Humidity(
    modifier: Modifier,
    humidity: Int,
    backgroundColor: Color,
    progressBackgroundColor: Color,
    progressColor: Color,
    cornerRadius: Dp
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(cornerRadius), modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconLabelItem(
                    label = "humidity".uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    iconRes = R.drawable.humidity,
                    iconDesc = "humidity",
                    modifier = Modifier,
                    iconModifier = Modifier
                        .size(14.dp)
                        .padding(end = 5.dp)
                )

                Text(
                    text = "${humidity}%",
                    fontSize = 25.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            ProgressBar(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .align(Alignment.CenterEnd)
                    .padding(end = 20.dp),
                progress = humidity / 100f,
                width = 30.dp,
                backgroundColor = progressBackgroundColor,
                progressColor = progressColor,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewHumidity() {
    Humidity(
        modifier = Modifier.size(150.dp),
        humidity = 25,
        backgroundColor = Color.Red,
        progressBackgroundColor = Color.Green,
        progressColor = Color.Yellow,
        cornerRadius = 20.dp
    )
}