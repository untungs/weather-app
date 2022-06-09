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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.untungs.weatherapp.feature.home.*
import com.untungs.weatherapp.feature.search.*
import com.untungs.weatherapp.ui.component.WeatherAppBar
import com.untungs.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        val currentBackstack by navController.currentBackStackEntryAsState()
        val openSearch by derivedStateOf { currentBackstack?.destination?.route == SearchDestination.route }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar = {
                    WeatherAppBar (
                        openSearch = openSearch,
                        onCloseClicked = { navController.popBackStack() },
                        onSearchClicked = { navController.navigateToSearch(it) },
                        onSearchTriggered = { navController.navigateToSearch() }
                    )
                }
            ) { innerPadding ->
                NavHost(
                    navController = navController,
                    startDestination = HomeDestination.route,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    homeGraph()
                    searchGraph {  }
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