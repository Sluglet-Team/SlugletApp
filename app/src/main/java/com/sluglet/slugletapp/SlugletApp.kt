package com.sluglet.slugletapp

import android.content.res.Resources
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sluglet.slugletapp.common.composables.BottomNavBar
import com.sluglet.slugletapp.common.snackbar.SnackbarManager
import com.sluglet.slugletapp.model.BottomNavItem
import com.sluglet.slugletapp.screens.home.HomeScreen
import com.sluglet.slugletapp.screens.map.MapScreen
import com.sluglet.slugletapp.screens.search.SearchScreen
import com.sluglet.slugletapp.screens.settings.SettingsScreen
import com.sluglet.slugletapp.screens.sign_up.SignUpScreen
import com.sluglet.slugletapp.ui.theme.DarkMode
import com.sluglet.slugletapp.ui.theme.LightMode
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme
import kotlinx.coroutines.CoroutineScope
import com.sluglet.slugletapp.screens.schedule.ScheduleScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlugletApp () {
    SlugletAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val appState = rememberAppState()
            Scaffold (
                snackbarHost = {
                    SnackbarHost (
                        hostState = appState.snackbarHostState,
                        snackbar = {snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.primary)
                        }
                    )
                },
                bottomBar = {
                    BottomNavBar(
                        items = listOf(
                            BottomNavItem(
                                name = "Home",
                                route = HOME_SCREEN,
                                selectedIcon = Icons.Filled.Home,
                                unselectedIcon = Icons.Default.Home
                            ),
                            BottomNavItem(
                                name = "Search",
                                route = SEARCH_SCREEN,
                                selectedIcon = Icons.Filled.Search,
                                unselectedIcon = Icons.Default.Search
                            ),
                            BottomNavItem(
                                name = "Schedule",
                                route = SCHEDULE_SCREEN,
                                selectedIcon = Icons.Filled.DateRange,
                                unselectedIcon = Icons.Default.DateRange
                            ),
                            BottomNavItem(
                                name = "Map",
                                route = MAP_SCREEN,
                                selectedIcon = Icons.Filled.LocationOn,
                                unselectedIcon = Icons.Default.LocationOn
                            )
                        ),
                        navController = appState.navController,
                        onItemClick = { appState.navController.navigate(it.route) }
                    )
                }
            ) {innerPadding ->
                // Anything in here will fill the max size of the screen
                if (isSystemInDarkTheme()) {
                    DarkMode()
                }
                else LightMode()

                NavHost(
                    navController = appState.navController,
                    startDestination = HOME_SCREEN,
                    modifier = Modifier.padding(innerPadding)
                ) {
                    slugletGraph(appState)
                }
            }

        }
    }

}
@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(navController, snackbarManager, resources, coroutineScope) {
        SlugletAppState(navController, snackbarHostState, snackbarManager, resources, coroutineScope)
    }
@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

fun NavGraphBuilder.slugletGraph(appState: SlugletAppState) {
    composable(HOME_SCREEN) {
        HomeScreen(openScreen = { route -> appState.navigate(route) })
    }
    composable(SEARCH_SCREEN) {
        SearchScreen(openScreen = { route -> appState.navigate(route) })
    }
    composable(SCHEDULE_SCREEN) {
        ScheduleScreen(openScreen = { route -> appState.navigate(route) })
    }
    composable(SIGNUP_SCREEN) {
        SignUpScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }
    composable(SETTINGS_SCREEN) {
        SettingsScreen(
            restartApp = { route -> appState.clearAndNavigate(route) },
            openScreen = { route -> appState.navigate(route) }
        )
    }
    composable(MAP_SCREEN) {
        MapScreen(openScreen = { route -> appState.navigate(route) })
    }
}