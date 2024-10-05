package com.example.forecast.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun Wind(
    modifier: Modifier = Modifier,
    speed: Int = 98,
    circleSize: Float = 0.9f,
    circleStrokeWidth: Dp = 12.dp,
    degree: Float = 30f,
    backgroundColor: Color = Color.Gray,
    circleStrokeColor: Color = Color.White.copy(0.6f),
    indicatorColor: Color = Color.White,
    cornerRadius: Dp = 20.dp
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(cornerRadius), modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(25.dp)
        ) {
            IconLabelItem(
                label = "wind".uppercase(),
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                iconRes = R.drawable.wind,
                iconDesc = "wind",
                modifier = Modifier,
                iconModifier = Modifier
                    .size(14.dp)
                    .padding(end = 5.dp)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth(circleSize)
                    .fillMaxHeight(circleSize)
                    .align(Alignment.CenterHorizontally)
            ) {
                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val radius = size.minDimension / 2
                    val center = Offset(size.width / 2, size.height / 2)
                    val stroke = Stroke(
                        width = circleStrokeWidth.toPx(), pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(8f, 10f), 0f
                        )
                    )

                    drawCircle(
                        radius = radius,
                        center = center,
                        color = circleStrokeColor,
                        style = stroke
                    )

                    val lineEnd = Offset(
                        center.x + radius * cos(Math.toRadians((-90 + degree).toDouble())).toFloat(),
                        center.y + radius * sin(Math.toRadians((-90 + degree).toDouble())).toFloat()
                    )

                    val lineLength = 10.dp.toPx() // Adjust the length as needed
                    val lineStart = Offset(
                        lineEnd.x - lineLength * cos(Math.toRadians((-90 + degree).toDouble())).toFloat(),
                        lineEnd.y - lineLength * sin(Math.toRadians((-90 + degree).toDouble())).toFloat()
                    )

                    drawLine(
                        color = indicatorColor,
                        start = lineStart,
                        end = lineEnd,
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )

                    drawCircle(
                        color = indicatorColor,
                        radius = 4.dp.toPx(), // Adjust the radius for visibility
                        center = lineEnd
                    )
                }

                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = speed.toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 25.sp,
                        color = Color.White
                    )
                    Text(text = "Km/h", fontSize = 15.sp, color = Color.White)
                }
            }
        }
    }
}

@Preview
@Composable
fun PreviewWind() {
    Wind(modifier = Modifier.size(200.dp))
}