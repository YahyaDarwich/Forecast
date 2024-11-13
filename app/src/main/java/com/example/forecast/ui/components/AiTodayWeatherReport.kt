package com.example.forecast.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.forecast.R
import com.example.forecast.ui.screens.customShimmerEffect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiTodayWeatherReport(
    modifier: Modifier,
    isLoading: Boolean,
    weatherReport: String,
    cornerRadius: Dp,
    backgroundColor: Color,
    loadingColor: Color,
    collapsedMaxLines: Int
) {
    val interactionSource = remember {
        MutableInteractionSource()
    }

    var isExpanded by remember {
        mutableStateOf(false)
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(cornerRadius),
        modifier = modifier.clickable(indication = null, interactionSource = interactionSource) {
            if (!isLoading) isExpanded = !isExpanded
        },
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_large))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconLabelItem(
                    label = "AI Weather Report",
                    fontSize = 14.sp,
                    iconRes = R.drawable.ai_report,
                    iconDesc = "Ai today weather report",
                    fontWeight = FontWeight.SemiBold,
                    iconModifier = Modifier
                        .size(22.dp)
                        .padding(end = 5.dp)
                )

                if (!isLoading) {
                    Image(
                        painter = painterResource(
                            id = if (isExpanded) R.drawable.baseline_keyboard_arrow_up_24
                            else R.drawable.baseline_keyboard_arrow_down_24
                        ),
                        contentDescription = null,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable { isExpanded = !isExpanded },
                        contentScale = ContentScale.Crop
                    )
                }
            }

            if (isLoading) {
                LazyColumn(
                    modifier = Modifier.height(70.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    items(3) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(if (it == 2) 0.5f else 1f)
                                .height(20.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .customShimmerEffect(
                                    colors = listOf(
                                        loadingColor.copy(0.5f),
                                        loadingColor.copy(0.5f),
                                        loadingColor.copy(0.5f),
                                        loadingColor
                                    ),
                                    fromTopStart = false, animationDuration = 1400
                                )
                        )
                    }
                }
            } else {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .align(Alignment.Start)
                        .animateContentSize(),
                    fontSize = 14.sp,
                    overflow = TextOverflow.Ellipsis,
                    text = weatherReport,
                    textAlign = TextAlign.Start,
                    maxLines = if (isExpanded) Int.MAX_VALUE else collapsedMaxLines
                )
            }
        }
    }
}