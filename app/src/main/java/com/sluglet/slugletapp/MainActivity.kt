package com.sluglet.slugletapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.common.composables.SearchTextField
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.screens.search.Search
import com.sluglet.slugletapp.screens.search.SearchScreenContent
import com.sluglet.slugletapp.screens.search.SearchViewModel
import com.sluglet.slugletapp.ui.theme.DarkMode
import com.sluglet.slugletapp.ui.theme.LightMode
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SlugletAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Anything in here will fill the max size of the screen
                    if (isSystemInDarkTheme()) {
                        DarkMode()
                    }
                    else LightMode()
                }
                // val appState = rememberAppState()

                // FIXME(CAMDEN): Testing to END needs removed eventually
                val test = CourseData(
                    courseNum = "CSE 115A",
                    courseName = "Introduction to Software Engineering",
                    profName = "Richard Julig",
                    dateTime = "MWF 8:00am - 9:05am",
                    location = "Baskin Auditorium 1"
                )
                val test2 = CourseData (
                    courseNum = "ANTH 101",
                    courseName = "Anthropology",
                    profName = "Some Prof",
                    dateTime = "MWF 8:00am - 9:05am",
                    location = "Somewhere"
                )
                var testList = mutableListOf<CourseData>()
                for (i in 1..100) {
                    testList.add(test)
                }
                testList.add(test2)
                SearchScreenContent(courses = testList, onSearchChange = {}, userSearch = "")
                // FIXME(CAMDEN): END
            }
        }
    }
}