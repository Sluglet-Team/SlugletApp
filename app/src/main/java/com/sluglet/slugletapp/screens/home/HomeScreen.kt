package com.sluglet.slugletapp.screens.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sluglet.slugletapp.SETTINGS_SCREEN
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.screens.search.SearchScreenContent
import com.sluglet.slugletapp.screens.settings.SettingsScreen
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * Composable that renders Home screen
 */
@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val courses = viewModel.userCourses.collectAsStateWithLifecycle(emptyList())
    HomeScreenContent(
        courses = courses.value,
        onSettingsClick = viewModel::onSettingsClick,
        openScreen = openScreen
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    courses: List<CourseData>,
    onSettingsClick: (((String) -> Unit) -> Unit)?,
    openScreen: (String) -> Unit = {}
) {
    var error by remember { mutableStateOf<String?>(null) }


    Scaffold (
        containerColor = Color.Transparent
    ) {
        Column (
            modifier = modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { /*avoiding the build yelling at me*/ },
                actions = {
                    SettingsGear(
                        onSettingsClick = onSettingsClick,
                        openScreen = openScreen
                    )
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
            )
            error?.let { errorMessage ->
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
            }

            LazyColumn(
                state = rememberLazyListState(),
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                items(courses) {courseItem ->
                    CourseBox(coursedata = courseItem, onAddClick = null)
                }
            }

        }
    }
}

@Composable
fun SettingsGear (
    onSettingsClick: (((String) -> Unit) -> Unit)?,
    openScreen: (String) -> Unit = {}
) {
    Icon(
        Icons.Filled.Settings,
        contentDescription = "settings",
        modifier = Modifier
            .clickable {
                if (onSettingsClick != null) {
                    onSettingsClick(openScreen)
                }
            }
    )
}