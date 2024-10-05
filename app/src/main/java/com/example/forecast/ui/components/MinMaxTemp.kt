package com.example.forecast.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R

@Composable
fun MinMaxTemp(
    maxTemp: Int,
    minTemp: Int,
    color: Color,
    textColor: Color,
    modifier: Modifier
) {
    Box(modifier) {
        Card(
            Modifier
                .fillMaxSize(),
            colors = CardDefaults.cardColors(containerColor = color),
//            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onPrimary),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = Modifier.fillMaxSize()
            ) {

                IconLabelItem(
                    label = "$maxTemp°",
                    fontSize = 13.sp,
                    iconRes = R.drawable.baseline_arrow_upward_24,
                    iconDesc = "max temperature",
                    modifier = Modifier,
                    textColor = textColor,
                    iconModifier = Modifier.size(10.dp)
                )

                IconLabelItem(
                    label = "$minTemp°",
                    fontSize = 13.sp,
                    iconRes = R.drawable.baseline_arrow_downward_24,
                    iconDesc = "min temperature",
                    textColor = textColor,
                    modifier = Modifier,
                    iconModifier = Modifier.size(10.dp)
                )
            }
        }
    }
}

//@Preview
@Composable
private fun PreviewMinMaxTemp() {
    MinMaxTemp(
        16,
        10,
        color = Color.Transparent,
        textColor = Color.White,
        modifier = Modifier
            .size(100.dp, 30.dp)
    )
}