package com.example.forecast.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R
import java.util.Locale

@Composable
fun HomeMainTempInfo(
    temperature: Int,
    weatherIcon: Int,
    iconModifier: Modifier,
    modifier: Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically, modifier = modifier
    ) {
        Text(text = "$temperature°", fontSize = 130.sp, color = Color.White)

        Image(
            painter = painterResource(id = weatherIcon),
            contentDescription = "current weather icon",
            modifier = iconModifier
        )
    }
}

@Preview
@Composable
private fun PreviewHomeMainTempInfo() {
    HomeMainTempInfo(22, R.drawable.drizzle_sunny, Modifier.size(120.dp), Modifier.fillMaxWidth())
}

