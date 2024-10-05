package com.example.forecast.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.layout.Alignment
import androidx.glance.layout.Row
import androidx.glance.layout.padding
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.unit.ColorProvider

@Composable
fun MaxMinTempWidget(
    maxTemp: Int,
    minTemp: Int,
    modifier: GlanceModifier,
    space: Dp,
    fontSize: TextUnit,
    color: ColorProvider,
    fontWeight: FontWeight
) {
    Row(modifier) {
        Row(
            horizontalAlignment = Alignment.Horizontal.Start,
            verticalAlignment = Alignment.Vertical.CenterVertically,
            modifier = GlanceModifier.padding(end = space)
        ) {
            Text(
                text = "H:",
                style = TextStyle(fontSize = fontSize, color = color, fontWeight = fontWeight)
            )
            Text(
                text = "$maxTemp°",
                style = TextStyle(fontSize = fontSize, color = color, fontWeight = fontWeight)
            )
        }

        Row(
            horizontalAlignment = Alignment.Horizontal.End,
            verticalAlignment = Alignment.Vertical.CenterVertically
        ) {
            Text(
                text = "L:",
                style = TextStyle(fontSize = fontSize, color = color, fontWeight = fontWeight)
            )
            Text(
                text = "$minTemp°",
                style = TextStyle(fontSize = fontSize, color = color, fontWeight = fontWeight)
            )
        }
    }
}