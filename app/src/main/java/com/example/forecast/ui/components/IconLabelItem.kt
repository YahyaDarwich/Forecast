package com.example.forecast.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun IconLabelItem(
    label: String,
    fontSize: TextUnit,
    textColor: Color = Color.White,
    iconRes: Int,
    iconDesc: String,
    fontWeight: FontWeight? = null,
    modifier: Modifier = Modifier,
    iconModifier: Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(iconRes),
            contentDescription = iconDesc,
            contentScale = ContentScale.Crop,
            modifier = iconModifier
        )

        Text(text = label, fontSize = fontSize, color = textColor, fontWeight = fontWeight)
    }
}