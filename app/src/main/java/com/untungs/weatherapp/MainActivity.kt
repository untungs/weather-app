package com.untungs.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.untungs.weatherapp.feature.home.homeGraph
import com.untungs.weatherapp.feature.search.navigateToSearch
import com.untungs.weatherapp.feature.search.searchGraph
import com.untungs.weatherapp.feature.weather.navigateToWeatherDaily
import com.untungs.weatherapp.feature.weather.weatherGraph
import com.untungs.weatherapp.nav.Home
import com.untungs.weatherapp.nav.Search
import com.untungs.weatherapp.nav.WeatherDaily
import com.untungs.weatherapp.ui.component.AppBarState
import com.untungs.weatherapp.ui.component.LocalScaffoldPadding
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
            CompositionLocalProvider(LocalScaffoldPadding provides innerPadding) {
                NavHost(
                    navController = navController,
                    startDestination = Home,
                    modifier = Modifier.fillMaxSize()
                ) {
                    homeGraph(snackbarHostState) {
                        navController.navigateToWeatherDaily(it)
                    }
                    searchGraph {
                        navController.popBackStack<Search>(inclusive = true)
                        navController.navigateToWeatherDaily(it)
                    }
                    weatherGraph(appBarState, snackbarHostState)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    WeatherAppTheme { WeatherApp() }
}
