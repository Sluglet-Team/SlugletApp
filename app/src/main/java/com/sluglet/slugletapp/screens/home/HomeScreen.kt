package com.sluglet.slugletapp.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.common.composables.SearchBox
import com.sluglet.slugletapp.model.CourseData
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
        viewModel = viewModel,
        //TODO get current firebase user
    )
}

@Composable
fun HomeScreenContent(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel, // Inject your HomeViewModel here
) {
    var courses by remember { mutableStateOf<List<CourseData>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }

    // Fetch courses when the screen is first created
    LaunchedEffect(key1 = true) {
        viewModel.getUserCourses(
            onSuccess = { retrievedCourses ->
                courses = retrievedCourses
            },
            onError = { errorMessage ->
                error = errorMessage
            }
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        // Display error message if there's an error
        error?.let { errorMessage ->
            Text(
                text = errorMessage,
                color = Color.Red,
                modifier = Modifier.padding(16.dp)
            )
        }

        // LazyColumn with courses
        LazyColumn(
            state = rememberLazyListState(),
            verticalArrangement = Arrangement.spacedBy(15.dp)
        ) {
            items(courses) { courseItem ->
                CourseBox(coursedata = courseItem)
            }
        }
    }
}