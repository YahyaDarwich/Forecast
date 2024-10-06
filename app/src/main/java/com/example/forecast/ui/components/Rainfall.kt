package com.example.forecast.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
fun RainFall(
    modifier: Modifier,
    rainfall: Float,
    backgroundColor: Color,
    cornerRadius: Dp
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(cornerRadius), modifier = modifier
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            ) {
                IconLabelItem(
                    label = "Rainfall".uppercase(),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    iconRes = R.drawable.baseline_water_drop_14,
                    iconDesc = "rainfall",
                    modifier = Modifier,
                    iconModifier = Modifier
                        .size(14.dp)
                        .padding(end = 5.dp)
                )

                Text(
                    text = "$rainfall mm",
                    fontSize = 27.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 15.dp)
                )

                Text(
                    text = "in last hour",
                    fontSize = 19.sp,
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewRainfall() {
    RainFall(
        modifier = Modifier.size(150.dp),
        rainfall = 1.8f,
        backgroundColor = Color.Red,
        cornerRadius = 20.dp
    )
}
