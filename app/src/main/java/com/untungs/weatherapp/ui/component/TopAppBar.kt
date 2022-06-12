package com.untungs.weatherapp.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.untungs.weatherapp.common.Function
import kotlinx.coroutines.job

class AppBarState(
    val title: String,
    val hasBackStack: Boolean,
    val openSearch: Boolean
) {
    var action by mutableStateOf<Pair<ImageVector, Function>?>(null)
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

@Composable
fun DefaultAppBar(
    state: AppBarState,
    onBackClicked: Function,
    onSearchOpened: Function
) {
    TopAppBar(
        title = { Text(text = state.title) },
        navigationIcon = if (state.hasBackStack) {
            {
                IconButton(onClick = onBackClicked) {
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
            state.action?.let {
                IconButton(
                    onClick = it.second
                ) {
                    Icon(
                        imageVector = it.first,
                        contentDescription = "Action",
                        tint = Color.White
                    )
                }
            }
            IconButton(
                onClick = onSearchOpened
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
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        color = MaterialTheme.colors.primary
    ) {
        val customTextSelectionColors = TextSelectionColors(
            handleColor = MaterialTheme.colors.onPrimary,
            backgroundColor = MaterialTheme.colors.onPrimary.copy(alpha = 0.4f)
        )

        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelectionColors) {
            TextField(modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
                value = text,
                onValueChange = { text = it },
                placeholder = {
                    Text(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        text = "Search",
                        color = Color.White
                    )
                },
                textStyle = TextStyle(
                    fontSize = MaterialTheme.typography.subtitle1.fontSize,
                    color = MaterialTheme.colors.onPrimary
                ),
                singleLine = true,
                leadingIcon = {
                    IconButton(
                        modifier = Modifier
                            .alpha(ContentAlpha.medium),
                        onClick = { onSearchClicked(text) }
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
                        onClick = {
                            if (text.isNotBlank()) text = "" else onCloseClicked()
                        },
                        modifier = Modifier.padding(end = 4.dp)
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
                        onSearchClicked(text)
                    }
                ),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = MaterialTheme.colors.primary,
                    cursorColor = Color.White
                        .copy(alpha = ContentAlpha.medium),
                    unfocusedIndicatorColor = MaterialTheme.colors.primary
                )
            )
        }
    }
}
//
//@Preview
//@Composable
//fun DefaultAppBarPreview() {
//    DefaultAppBar(
//        AppBarState.Default(
//            title = stringResource(id = R.string.app_name),
//            hasBackStack = true,
//            onBackClicked = {},
//            onSearchTriggered = {}
//        )
//    )
//}
//
//@Preview
//@Composable
//fun SearchAppBarPreview() {
//    SearchAppBar(AppBarState.Search(onCloseClicked = {}, onSearchClicked = {}))
//}