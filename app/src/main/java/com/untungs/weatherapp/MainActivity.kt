package com.untungs.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.untungs.weatherapp.feature.home.*
import com.untungs.weatherapp.feature.search.*
import com.untungs.weatherapp.feature.weather.WeatherDailyDestination
import com.untungs.weatherapp.feature.weather.navigateToWeatherDaily
import com.untungs.weatherapp.feature.weather.weatherGraph
import com.untungs.weatherapp.ui.component.AppBarState
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
        val route = currentBackstack?.destination?.route
        val appBarState = if (route == SearchDestination.route) {
            AppBarState.Search(
                onCloseClicked = navController::popBackStack,
                onSearchClicked = navController::navigateToSearch
            )
        } else {
            val title = when (route) {
                WeatherDailyDestination.route -> currentBackstack?.arguments
                    ?.getString(WeatherDailyDestination.city).orEmpty()
                else -> stringResource(id = R.string.app_name)
            }
            AppBarState.Default(
                title = title,
                hasBackStack = route != HomeDestination.route,
                onBackClicked = navController::navigateUp,
                onSearchTriggered = navController::navigateToSearch
            )
        }

        val scaffoldState = rememberScaffoldState()

        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                WeatherAppBar(appBarState)
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = HomeDestination.route,
                modifier = Modifier.padding(innerPadding)
            ) {
                homeGraph()
                searchGraph {
                    navController.popBackStack(SearchDestination.route, true)
                    navController.navigateToWeatherDaily(it)
                }
                weatherGraph(scaffoldState.snackbarHostState)
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