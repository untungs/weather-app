package com.untungs.weatherapp.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.untungs.weatherapp.ui.component.LocalScaffoldPadding

@Composable
fun EmptyScreen(
    text: String,
    successText: String = "",
    state: LoadingUiState<*> = LoadingUiState.Unknown
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(LocalScaffoldPadding.current)
            .imePadding()
    ) {
        if (state is LoadingUiState.Loading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            val text = when (state) {
                is LoadingUiState.Error -> state.message
                is LoadingUiState.Success<*> -> successText
                LoadingUiState.Unknown -> text
            }
            Text(
                text = text,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(48.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}
