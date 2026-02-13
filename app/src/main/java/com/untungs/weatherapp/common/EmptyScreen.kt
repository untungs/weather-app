package com.untungs.weatherapp.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptyScreen(text: String) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(48.dp),
            textAlign = TextAlign.Center
        )
    }
}
