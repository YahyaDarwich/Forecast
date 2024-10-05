package com.example.forecast.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProgressBar(
    modifier: Modifier = Modifier,
    progress: Float = 0.8f,
    width: Dp = 10.dp,
    textColor: Color = Color.White,
    textSize: TextUnit = 10.sp,
    backgroundColor: Color = Color.Blue,
    progressColor: Color = Color.Green,
    shape: Shape = RoundedCornerShape(10.dp)
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(5.dp), modifier = modifier
    ) {
        Text(
            text = "100", color = textColor, fontSize = textSize
        )

        Box(
            modifier = Modifier
                .clip(shape)
                .width(width)
                .fillMaxHeight()
                .weight(2f)
                .background(backgroundColor)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(progress)
                    .align(Alignment.BottomCenter)
                    .clip(shape)
                    .background(progressColor)
            )
        }

        Text(
            text = "0", fontSize = textSize, color = textColor,
        )
    }
}

//@Preview
@Composable
private fun PreviewProgressBar() {
    ProgressBar(modifier = Modifier.width(150.dp))
}