package com.untungs.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.untungs.weatherapp.home.HomeNav
import com.untungs.weatherapp.home.homeGraph
import com.untungs.weatherapp.ui.component.WeatherAppBar
import com.untungs.weatherapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherApp()
        }
    }
}

@Composable
fun WeatherApp() {
    WeatherAppTheme {
        val navController = rememberNavController()
        var openSearch by remember { mutableStateOf(false) }
        var searchText by remember { mutableStateOf("") }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar ={
                    WeatherAppBar(
                        openSearch = openSearch,
                        searchText = searchText,
                        onTextChange = { searchText = it},
                        onCloseClicked = { openSearch = false },
                        onSearchClicked = { searchText = it },
                        onSearchTriggered = { openSearch = true }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = HomeNav.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    homeGraph()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme {
        WeatherApp()
    }
}