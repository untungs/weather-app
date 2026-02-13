package com.untungs.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.untungs.weatherapp.feature.home.*
import com.untungs.weatherapp.feature.search.*
import com.untungs.weatherapp.feature.weather.navigateToWeatherDaily
import com.untungs.weatherapp.feature.weather.weatherGraph
import com.untungs.weatherapp.nav.Home
import com.untungs.weatherapp.nav.Search
import com.untungs.weatherapp.nav.WeatherDaily
import com.untungs.weatherapp.ui.component.AppBarState
import com.untungs.weatherapp.ui.component.WeatherAppBar
import com.untungs.weatherapp.ui.theme.WeatherAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { WeatherApp() }
    }
}

@Composable
fun WeatherApp() {
    WeatherAppTheme {
        val navController = rememberNavController()
        val currentBackstack by navController.currentBackStackEntryAsState()
        val currentDestination = currentBackstack?.destination
        val context = LocalContext.current

        val appBarState = remember(currentDestination, currentBackstack) {
            AppBarState(
                title =
                    when {
                        currentDestination?.hasRoute<WeatherDaily>() == true -> {
                            currentBackstack
                                ?.toRoute<WeatherDaily>()
                                ?.city
                                .orEmpty()
                        }

                        currentDestination?.hasRoute<Home>() == true -> {
                            context.getString(R.string.app_name)
                        }

                        else -> ""
                    },
                hasBackStack = currentDestination?.hasRoute<Home>() == false,
                openSearch = currentDestination?.hasRoute<Search>() == true
            )
        }

        val snackbarHostState = remember { SnackbarHostState() }

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                WeatherAppBar(
                    state = appBarState,
                    onBackClicked = navController::navigateUp,
                    onSearchOpened = { navController.navigateToSearch() },
                    onSearchClosed = navController::popBackStack,
                    onSearchTriggered = { navController.navigateToSearch(it) }
                )
            }
        ) { innerPadding ->
            NavHost(
                navController = navController,
                startDestination = Home,
                modifier = Modifier.padding(innerPadding)
            ) {
                homeGraph(snackbarHostState) { navController.navigateToWeatherDaily(it) }
                searchGraph {
                    navController.popBackStack<Search>(inclusive = true)
                    navController.navigateToWeatherDaily(it)
                }
                weatherGraph(appBarState, snackbarHostState)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme { WeatherApp() }
}
