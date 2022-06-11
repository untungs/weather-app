package com.untungs.weatherapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.untungs.weatherapp.R

sealed interface AppBarState {
    data class Default(
        val title: String,
        val hasBackStack: Boolean,
        val onBackClicked: () -> Unit,
        val onSearchTriggered: () -> Unit
    ) : AppBarState

    data class Search(
        val onCloseClicked: () -> Unit,
        val onSearchClicked: (String) -> Unit
    ) : AppBarState
}

@Composable
fun WeatherAppBar(state: AppBarState) {
    when (state) {
        is AppBarState.Default -> DefaultAppBar(state)
        is AppBarState.Search -> SearchAppBar(state)
    }
}

@Composable
fun DefaultAppBar(state: AppBarState.Default) {
    TopAppBar(
        title = { Text(text = state.title) },
        navigationIcon = if (state.hasBackStack) {
            {
                IconButton(onClick = state.onBackClicked) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }
        } else {
            null
        },
        actions = {
            IconButton(
                onClick = state.onSearchTriggered
            ) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search Icon",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
fun SearchAppBar(state: AppBarState.Search) {
    var text by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colors.primary
    ) {
        TextField(modifier = Modifier
            .fillMaxWidth(),
            value = text,
            onValueChange = { text = it },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = "Search city here",
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    onClick = {}
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search Icon",
                        tint = Color.White
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = state.onCloseClicked
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close Icon",
                        tint = Color.White
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    state.onSearchClicked(text)
                }
            ),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                cursorColor = Color.White.copy(alpha = ContentAlpha.medium)
            ))
    }
}

@Preview
@Composable
fun DefaultAppBarPreview() {
    DefaultAppBar(
        AppBarState.Default(
            title = stringResource(id = R.string.app_name),
            hasBackStack = true,
            onBackClicked = {},
            onSearchTriggered = {}
        )
    )
}

@Preview
@Composable
fun SearchAppBarPreview() {
    SearchAppBar(AppBarState.Search(onCloseClicked = {}, onSearchClicked = {}))
}