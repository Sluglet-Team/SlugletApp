package com.sluglet.slugletapp.screens.home

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.screens.search.SearchScreenContent
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme

/**
 * Composable that renders Home screen
 */
@Composable
fun HomeScreen(
    openScreen: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val courses = viewModel.courses.collectAsStateWithLifecycle(emptyList())
    HomeScreenContent(
        courses = courses,
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    courses: State<List<CourseData>>,
) {
    var error by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
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
            items(courses.value) { courseItem ->
                CourseBox(coursedata = courseItem, onAddClick = null)
            }
        }

    }
}