package com.sluglet.slugletapp.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.common.composables.SearchBox
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme

/*
A composable that renders the Search Screen
Uses a CourseBox composable along with a SearchTextField
 */
@Composable
fun SearchScreen (
    openScreen: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    // Gets courses from firestore courses collection
    val courses = viewModel.courses.collectAsStateWithLifecycle(emptyList())

    // Sets the content for the screen
    SearchScreenContent(
        courses = courses.value.sortedBy { it.course_number },
        userSearch = viewModel.userSearch,
        onSearchChange = { viewModel.updateSearch(it) },
        onAddClick = viewModel::onAddClick,
        onMapClick = viewModel::onMapClick,
        openScreen = openScreen
    )

}

@Composable
fun SearchScreenContent (
    modifier: Modifier = Modifier,
    courses: List<CourseData>,
    onSearchChange: (String) -> Unit,
    userSearch: String,
    onAddClick: ((CourseData) -> Unit)?,
    onMapClick: (((String) -> Unit, CourseData) -> Boolean)?,
    openScreen: (String) -> Unit = {}
) {
    // with a LazyColumn underneath with all the courses
    Column (modifier = Modifier

    ) {
        // Search Bar at the top
        SearchBox(
            onSearchChange = onSearchChange,
            userSearch = userSearch
        )
        // LazyColumn with courses
        LazyColumn (
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            // For each item, filter if there is input, display CourseBox for each item
            items(
                items = courses.filter {
                    it.course_number.contains(userSearch.trim(), ignoreCase = true)
                            || it.course_name.contains(userSearch.trim(), ignoreCase = true)
                }
            ) { courseItem ->
                CourseBox(
                    coursedata = courseItem,
                    buttonType = "ADD",
                    onAddClick = onAddClick,
                    onRemoveClick = null,
                    hasMapButton = true,
                    onMapClick = onMapClick,
                    openScreen = openScreen
                )
            }
        }
    }
}
@Preview
@Composable
fun SearchPreview (

) {
    val testList = mutableListOf<CourseData>()
    val testCourse = CourseData (
        course_number = "CSE 115A",
        course_name = "Intro to Software Engineering",
        location = "Basking Auditorium 1",
        date_time = "MWF 8:00am-9:00am",
        prof_name = "Julig"
    )
    for (i in 1..10) {
        testList.add(testCourse)
    }
    SlugletAppTheme {
        SearchScreenContent(
            courses = testList,
            onSearchChange = { },
            userSearch = "",
            onAddClick = null,
            onMapClick = null
        )
    }


}