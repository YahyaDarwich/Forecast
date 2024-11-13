package com.example.forecast.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit

@Composable
fun AnimatedTextCounter(
    count: Int,
    suffixText: String? = null,
    animDuration: Int,
    modifier: Modifier,
    fontSize: TextUnit,
    fontWeight: FontWeight,
    color: Color
) {
    var oldCount by remember {
        mutableIntStateOf(count)
    }

    SideEffect { oldCount = count }

    Row(modifier) {
        for (i in count.toString().indices) {
            val oldChar = oldCount.toString().getOrNull(i)
            val newChar = count.toString()[i]

            val char = if (oldChar == newChar) oldChar else newChar

            val toIncrement = oldCount < count

            AnimatedContent(
                targetState = char,
                transitionSpec = {
                    if (toIncrement) slideInVertically(
                        animationSpec = tween(
                            durationMillis = animDuration,
                            easing = LinearEasing
                        )
                    ) { -it } togetherWith slideOutVertically(
                        animationSpec = tween(durationMillis = animDuration, easing = LinearEasing)
                    ) { it }
                    else slideInVertically(
                        animationSpec = tween(
                            durationMillis = animDuration,
                            easing = LinearEasing
                        )
                    ) { it } togetherWith slideOutVertically(
                        animationSpec = tween(durationMillis = animDuration, easing = LinearEasing)
                    ) { -it }
                },
                label = "animatedTextCounter"
            ) { targetChar ->
                Text(
                    text = targetChar.toString(),
                    color = color,
                    fontSize = fontSize,
                    fontWeight = fontWeight
                )
            }
        }

        if (!suffixText.isNullOrEmpty()) {
            Text(
                text = suffixText,
                color = color,
                fontSize = fontSize,
                fontWeight = fontWeight
            )
        }
    }
}