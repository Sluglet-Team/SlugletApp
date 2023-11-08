package com.sluglet.slugletapp

import android.content.res.Resources
import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.android.gms.tasks.Task
import com.sluglet.slugletapp.common.composables.BottomNavBar
import com.sluglet.slugletapp.common.snackbar.SnackbarManager
import com.sluglet.slugletapp.model.BottomNavItem
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.screens.search.SearchScreen
import com.sluglet.slugletapp.screens.search.SearchScreenContent
import com.sluglet.slugletapp.ui.theme.DarkMode
import com.sluglet.slugletapp.ui.theme.LightMode
import com.sluglet.slugletapp.ui.theme.SlugletAppTheme
import kotlinx.coroutines.CoroutineScope

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlugletApp () {
    SlugletAppTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {

            val appState = rememberAppState()
            val snackbarHostState = remember { SnackbarHostState() }
            val navController = rememberNavController()
            Scaffold (
                snackbarHost = {
                    SnackbarHost (
                        hostState = snackbarHostState,
                        snackbar = {snackbarData ->
                            Snackbar(snackbarData, contentColor = MaterialTheme.colorScheme.primary)
                        }
                    )
                },
                bottomBar = {
                    BottomNavBar(
                        items = listOf(
                            BottomNavItem(
                                name = "Search",
                                route = SEARCH_SCREEN,
                                selectedIcon = Icons.Filled.Search,
                                unselectedIcon = Icons.Default.Search
                            ),
                            // FIXME(CAMDEN): This crashes the app becuase there is not screen for this
                            BottomNavItem(
                                name = "Schedule",
                                route = SCHEDULE_SCREEN,
                                selectedIcon = Icons.Filled.DateRange,
                                unselectedIcon = Icons.Default.DateRange
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
                    startDestination = SEARCH_SCREEN,
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
    snackbarManager: SnackbarManager = SnackbarManager,
    resources: Resources = resources(),
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) =
    remember(navController, snackbarManager, resources, coroutineScope) {
        SlugletAppState(navController, snackbarManager, resources, coroutineScope)
    }
@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}

fun NavGraphBuilder.slugletGraph(appState: SlugletAppState) {
    composable(SEARCH_SCREEN) {
        SearchScreen(openScreen = { route -> appState.navigate(route) })
    }
}


fun getUserDocument(uid: String, onSuccess: (DocumentSnapshot) -> Unit, onError: (String) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()
    val userDocRef = firestore.collection("users").document(uid)

    userDocRef.get().addOnCompleteListener(OnCompleteListener { task ->
        if(task.isSuccessful) {
            val userDocument = task.result
            if(userDocument != null && userDocument.exists()){
                onSuccess(userDocument)
            } else {
                onError("User document not found.")
            }
        } else {
            onError("Error fetching user document: ${task.exception?.message}")
        }
    })
}

fun getCourseData(courseId: String, firestore: FirebaseFirestore, onSuccess: (CourseData) -> Unit, onError: (String) -> Unit) {
    val courseDocRef = firestore.collection("courses").document(courseId)

    courseDocRef.get().addOnCompleteListener(OnCompleteListener { courseTask ->
        if(courseTask.isSuccessful) {
            val courseSnapshot = courseTask.result
            val courseData = courseSnapshot?.toObject(CourseData::class.java)
            if(courseData != null) {
                onSuccess(courseData)
            }
        } else {
            onError("Error fetching course document: ${courseTask.exception?.message}")
        }
    })
}

// Combines the above modular functions
fun getUserCourses(uid: String, onSuccess: (List<CourseData>) -> Unit, onError: (String) -> Unit) {
    val firestore = FirebaseFirestore.getInstance()

    getUserDocument(uid, { userDocument ->
        val userCourses = userDocument.get("userCourses") as List<String>?
        if(userCourses != null){
            val courses = mutableListOf<CourseData>()
            var coursesToFetch = userCourses.size

            if(coursesToFetch == 0){
                onSuccess(emptyList())
            } else {
                for(courseId in userCourses){
                    getCourseData(courseId, firestore, { courseData ->
                        courses.add(courseData)
                        coursesToFetch--
                        if(coursesToFetch == 0){
                            onSuccess(courses)
                        }
                    }, {error ->
                        onError(error)
                    })
                }
            }
        } else {
            onSuccess(emptyList())
        }
    }, {error ->
        onError(error)
    })
}