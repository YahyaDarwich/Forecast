package com.example.forecast.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.forecast.R
import com.example.forecast.model.LocalNames
import com.example.forecast.model.Location
import com.example.forecast.ui.components.LocationCell
import com.example.forecast.ui.components.SearchBar
import com.example.forecast.ui.theme.ForecastTheme

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    topPadding: Dp = 0.dp,
    weatherSearchUiState: WeatherSearchUiState,
    onSearch: (String) -> Unit = {},
    onLocationClick: (String, String) -> Unit = { lat: String, long: String -> },
    colors: List<Color>
) {
    var isInputHasFocus by remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                color = if (isInputHasFocus) Color.Black.copy(0.8f)
                else Color.Transparent
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        if (isInputHasFocus) {
            Card(
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                when (weatherSearchUiState) {
                    is WeatherSearchUiState.Loading -> Box(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(text = "Enter a location name...", color = Color.White)
                    }

                    is WeatherSearchUiState.Success -> {
                        LazyColumn(
                            modifier = Modifier
                                .padding(top = topPadding)
                        ) {
                            items(weatherSearchUiState.searchResult) {
                                LocationCell(
                                    data = it,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp, vertical = 5.dp)
                                        .height(55.dp)
                                        .clickable {
                                            onLocationClick(
                                                it.lat.toString(),
                                                it.lon.toString()
                                            )

                                            isInputHasFocus = false
                                        }
                                )
                            }
                        }
                    }

                    else -> Box(
                        modifier = Modifier.padding(10.dp)
                    ) {
                        Text(text = "Something wrong", color = Color.White)
                    }
                }
            }
        }

        SearchBar(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .padding(bottom = 15.dp),
            iconSize = 30.dp,
            backgroundColor = colors[0],
            onValueChange = onSearch,
            onInputState = { isInputHasFocus = it }
        )
    }
}

val previewLocations =
    listOf(
        Location("Tripoli k", 13.47867, 15.5367573, "LB", "North Governorate", LocalNames("LB")),
        Location("Tripoli", 13.47867, 15.5367573, "LB", "North Governorate", LocalNames("LB")),
        Location("Tripoli tall", 13.47867, 15.5367573, "LB", "North Governorate", LocalNames("LB")),
        Location("Tripoli k", 13.47867, 15.5367573, "LB", "North Governorate", LocalNames("LB"))
    )

@Preview
@Composable
private fun PreviewSearchScreen() {
    ForecastTheme {
        SearchScreen(
            weatherSearchUiState = WeatherSearchUiState.Success(previewLocations), colors = listOf(
                colorResource(id = R.color.haze_background_day),
                colorResource(id = R.color.haze_day_1)
            )
        )
    }
}