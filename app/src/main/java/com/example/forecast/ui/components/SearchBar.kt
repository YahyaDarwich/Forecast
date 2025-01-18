package com.example.forecast.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.forecast.ui.theme.ForecastTheme

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    iconSize: Dp = 30.dp,
    onValueChange: (String) -> Unit = {},
    backgroundColor: Color,
    onInputState: (Boolean) -> Unit = {}
) {
    var searchText by remember {
        mutableStateOf("")
    }

    var showInput by remember {
        mutableStateOf(false)
    }

    val focusRequester = remember {
        FocusRequester()
    }

    val focusManager = LocalFocusManager.current

    val interaction = remember {
        MutableInteractionSource()
    }

    LaunchedEffect(key1 = showInput) {
        if (showInput) {
            focusRequester.requestFocus()
        }

        onInputState(showInput)
    }

    BackHandler(enabled = showInput) {
        searchText = ""
        onValueChange("")
        showInput = false
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .wrapContentWidth()
                .clip(RoundedCornerShape(50.dp))
                .background(color = backgroundColor)
                .clickable(interaction, null) {
                    showInput = !showInput
                },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            AnimatedVisibility(
                visible = showInput,
                enter = expandHorizontally(animationSpec = tween(200)),
                exit = shrinkHorizontally(animationSpec = tween(200))
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        onValueChange(it)
                    },
                    modifier = Modifier
                        .padding(start = 16.dp, end = 20.dp)
                        .focusRequester(focusRequester),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        focusManager.clearFocus()
                        searchText = ""
                        showInput = false
                    }),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color.Transparent,
                        unfocusedBorderColor = Color.Transparent,
                        focusedTextColor = Color.White,
                        cursorColor = Color.White
                    ),
                    trailingIcon = {
                        if (searchText.isNotBlank() && searchText.length > 2) {
                            IconButton(onClick = {
                                focusManager.clearFocus()
                                searchText = ""
                                onValueChange("")
                                showInput = false
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "close search",
                                    tint = Color.White
                                )
                            }
                        }
                    },
                    placeholder = { Text(text = "Search...", color = Color.White) },
                    singleLine = true,
                    maxLines = 1
                )
            }

            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                modifier = Modifier
                    .padding(8.dp)
                    .size(iconSize),
                tint = Color.White
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSearchBar() {
    ForecastTheme {
        SearchBar(
            modifier = Modifier,
            backgroundColor = Color.DarkGray,
        )
    }
}