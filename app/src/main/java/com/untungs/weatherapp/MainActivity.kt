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
import androidx.navigation.compose.rememberNavController
import com.untungs.weatherapp.feature.home.*
import com.untungs.weatherapp.feature.search.SearchDestination
import com.untungs.weatherapp.feature.search.SearchScreen
import com.untungs.weatherapp.feature.search.SearchViewModel
import com.untungs.weatherapp.feature.search.searchGraph
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
//        val currentBackstack by navController.currentBackStackEntryAsState()
        var openSearch by remember { mutableStateOf(false) }

        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Scaffold(
                topBar ={
                    WeatherAppBar (
                        openSearch = openSearch,
                        onCloseClicked = {
                            navController.navigate(HomeDestination.route)
                            openSearch = false
                                         },
                        onSearchClicked = { navController.navigate("${SearchDestination.route}/$it") },
                        onSearchTriggered = { openSearch = true }
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