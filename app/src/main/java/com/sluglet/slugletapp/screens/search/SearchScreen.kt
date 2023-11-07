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
    viewModel: SearchViewModel = hiltViewModel() // FIXME(CAMDEN): This line breaks the app
) {
    // this should get all the courses from the DB
    // IDK if this will actually do that
    // FIXME: Isn't getting courses atm
    // val courses = viewModel.courses.collectAsStateWithLifecycle(emptyList())
    val test = CourseData (
        courseName = "Intro to Soft",
        courseNum = "CSE 115A",
        location = "Aud 1",
        dateTime = "MWF 8-9am",
        profName = "Julig"
    )
    val test2 = CourseData (
        courseName = "Intro to Anth",
        courseNum = "ANTH 101",
        location = "Aud 1",
        dateTime = "MWF 8-9am",
        profName = "Julig"
    )
    var testList = mutableListOf<CourseData>()
    for (i in 1..100) {
        testList.add(test)
    }
    testList.add(test2)

    SearchScreenContent(
        courses = testList,
        userSearch = viewModel.userSearch,
        onSearchChange = { viewModel.updateSearch(it) }
    )

}

@Composable
fun SearchScreenContent (
    modifier: Modifier = Modifier,
    courses: List<CourseData>,
    onSearchChange: (String) -> Unit,
    userSearch: String
    // FIXME: the following two take the wrong arguments
    /*
    onAddClick: ((String) -> Unit) -> Unit, wrong
    onMapClick: ((String) -> Unit) -> Unit, wrong
    openScreen: (String) -> Unit
     */
) {
    // TODO(CAMDEN): Need a column with search at the top
    // with a LazyColumn underneath with all the courses
    Column (modifier = Modifier

    ) {
        SearchBox(
            onSearchChange = onSearchChange,
            userSearch = userSearch
        )
        // LazyColumn with courses
        LazyColumn (
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(
                items = courses.filter {
                    it.courseNum.contains(userSearch.trim(), ignoreCase = true)
                }
            ) { courseItem ->
                CourseBox(coursedata = courseItem)
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
        courseNum = "CSE 115A",
        courseName = "Intro to Software Engineering",
        location = "Basking Auditorium 1",
        dateTime = "MWF 8:00am-9:00am",
        profName = "Julig"
    )
    for (i in 1..10) {
        testList.add(testCourse)
    }
    SlugletAppTheme {
        SearchScreenContent(
            courses = testList,
            onSearchChange = { },
            userSearch = ""
        )
    }


}