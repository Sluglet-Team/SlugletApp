package com.sluglet.slugletapp.screens.search

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sluglet.slugletapp.model.CourseData

/*
A composable that renders the Search Screen
Uses a CourseBox composable along with a SearchTextField
 */
@Composable
fun Search (
    openScreen: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    // this should get all the courses from the DB
    // IDK if this will actually do that
    val courses = viewModel.courses.collectAsStateWithLifecycle(emptyList())

    SearchScreenContent(
        courses = courses.value,
        onAddClick = ,
        onMapClick = ,
        openScreen = openScreen)

}

@Composable
fun SearchScreenContent (
    modifier: Modifier = Modifier,
    courses: List<CourseData>,
    // FIXME: the following two  take the wrong arguments
    onAddClick: ((String) -> Unit) -> Unit,
    onMapClick: ((String) -> Unit) -> Unit,
    openScreen: (String) -> Unit
) {

}