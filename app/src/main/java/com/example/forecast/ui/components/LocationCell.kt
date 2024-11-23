package com.example.forecast.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.model.LocalNames
import com.example.forecast.model.Location
import com.example.forecast.ui.theme.ForecastTheme
import java.util.Locale

@Composable
fun LocationCell(modifier: Modifier = Modifier, data: Location) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        val name = data.name +
                if (data.country.isNotEmpty())
                    ", " + Locale(
                        Locale.ENGLISH.language,
                        data.country
                    ).displayCountry else ""

        Text(
            text = name,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            modifier = Modifier.padding(bottom = 5.dp),
            color = Color.White
        )

        Text(
            text = data.state ?: "",
            maxLines = 1,
            fontSize = 14.sp,
            color = Color.White.copy(0.8f)
        )

        Divider(
            color = Color.White.copy(0.5f),
            modifier = Modifier.padding(top = 5.dp),
        )
    }
}


val location =
    Location("Tripoli k", 13.47867, 15.5367573, "LB", "North Governorate", LocalNames("LB"))

@Preview
@Composable
private fun PreviewLocationCell() {
    ForecastTheme {
        LocationCell(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 5.dp)
                .height(55.dp),
            data = location
        )
    }
}