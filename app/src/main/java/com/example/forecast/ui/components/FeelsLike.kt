package com.example.forecast.ui.components

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
fun FeelsLike(
    modifier: Modifier,
    feelsLike: Int = 19,
    backgroundColor: Color = Color.Gray,
    cornerRadius: Dp = 20.dp
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(cornerRadius), modifier = modifier
    ) {
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium))) {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                IconLabelItem(
                    label = "feels like".uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    iconRes = R.drawable.feelslike,
                    iconDesc = "feelslike",
                    modifier = Modifier,
                    iconModifier = Modifier
                        .size(14.dp)
                        .padding(end = 5.dp)
                )

                Text(
                    text = "${feelsLike}°",
                    fontSize = 25.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = "Similar to the actual \ntemperature.",
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold, modifier = Modifier.align(Alignment.BottomStart)
            )
        }
    }
}

@Preview
@Composable
fun PreviewFeelsLike() {
    FeelsLike(modifier = Modifier.size(170.dp))
}