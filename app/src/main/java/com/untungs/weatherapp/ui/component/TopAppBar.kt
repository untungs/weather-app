package com.untungs.weatherapp.ui.component

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.untungs.weatherapp.common.Function
import com.untungs.weatherapp.ui.theme.WeatherAppTheme
import kotlinx.coroutines.job

class AppBarState(
    val title: String,
    val hasBackStack: Boolean,
    val openSearch: Boolean
) {
    var action by mutableStateOf<(@Composable () -> Unit)?>(null)
}

@Composable
fun WeatherAppBar(
    state: AppBarState,
    onBackClicked: Function,
    onSearchOpened: Function,
    onSearchClosed: Function,
    onSearchTriggered: (String) -> Unit
) {
    if (state.openSearch) {
        SearchAppBar(onSearchClosed, onSearchTriggered)
    } else {
        DefaultAppBar(state, onBackClicked, onSearchOpened)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DefaultAppBar(
    state: AppBarState,
    onBackClicked: Function,
    onSearchOpened: Function
) {
    TopAppBar(
        title = { Text(text = state.title) },
        navigationIcon = {
            if (state.hasBackStack) {
                IconButton(onClick = onBackClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        },
        actions = {
            state.action?.invoke()
            IconButton(onClick = onSearchOpened) {
                Icon(imageVector = Icons.Filled.Search, contentDescription = "Search Icon")
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar(
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        this.coroutineContext.job.invokeOnCompletion {
            focusRequester.requestFocus()
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f),
        modifier = Modifier.fillMaxWidth()
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.statusBars)
                .height(64.dp) // Match TopAppBar height
                .focusRequester(focusRequester),
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(
                    modifier = Modifier.alpha(0.6f),
                    text = "Search",
                    style = MaterialTheme.typography.headlineSmall
                )
            },
            textStyle = MaterialTheme.typography.headlineSmall,
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier.alpha(0.6f),
                    onClick = { onSearchClicked(text) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotBlank()) text = "" else onCloseClicked()
                    },
                    modifier = Modifier.padding(end = 4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = { onSearchClicked(text) }),
            colors =
                TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onSurface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                ),
            shape = RectangleShape
        )
    }
}

@Preview
@Composable
fun DefaultAppBarPreview() {
    WeatherAppTheme {
        DefaultAppBar(
            AppBarState(title = "Weather App", hasBackStack = true, openSearch = false),
            onBackClicked = {},
            onSearchOpened = {}
        )
    }
}

@Preview
@Composable
fun SearchAppBarPreview() {
    WeatherAppTheme {
        SearchAppBar(onCloseClicked = {}, onSearchClicked = {})
    }
}
