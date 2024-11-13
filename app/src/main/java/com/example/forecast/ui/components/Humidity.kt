package com.example.forecast.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun Humidity(
    modifier: Modifier,
    humidity: Int,
    backgroundColor: Color,
    progressBackgroundColor: Color,
    progressColor: Color,
    cornerRadius: Dp,
    isVisible: Boolean = false
) {
    var percentage by remember {
        mutableFloatStateOf(0f)
    }

    val progress by animateFloatAsState(
        targetValue = percentage,
        animationSpec = tween(durationMillis = 1500),
        label = "percentage"
    )

    LaunchedEffect(key1 = isVisible) {
        percentage = if (isVisible)
            (humidity / 100f)
        else 0f
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(cornerRadius), modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
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

                AnimatedTextCounter(
                    count = (progress * 100).toInt(),
                    suffixText = "%",
                    animDuration = 110,
                    modifier = Modifier,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            ProgressBar(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .align(Alignment.CenterEnd)
                    .padding(end = dimensionResource(id = R.dimen.padding_medium)),
                progress = progress,
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
        humidity = 15,
        backgroundColor = Color.Red,
        progressBackgroundColor = Color.Green,
        progressColor = Color.Yellow,
        cornerRadius = 20.dp,
    )
}