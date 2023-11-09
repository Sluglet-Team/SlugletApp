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
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
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

/**
 * Retrieves a user document from Firestore based on the provided user ID.
 *
 * @param uid The unique identifier of the user whose document is to be retrieved.
 * @param onSuccess Callback function to be invoked when the user document is successfully retrieved.
 *                  Receives a userDoc as a parameter, which is the retrieved DocumentSnapshot.
 * @param onError Callback function to be invoked when an error occurs during the document retrieval.
 *                Receives an error message (String) as a parameter.
  */
fun getUserDocument(uid: String, onSuccess: (DocumentSnapshot) -> Unit, onError: (String) -> Unit) {
    // Retrieve firestore instance and reference to user document
    val firestore = Firebase.firestore
    val userDocRef = firestore.collection("users").document(uid)

    // Asynchronously fetch user doc using provided uid
    userDocRef.get().addOnSuccessListener { userDoc ->
        // Success callback invoked with the retrieved snapshot
        if(userDoc != null && userDoc.exists()) {
            onSuccess(userDoc)
        } else {
            onError("User document not found.")
        }
    }.addOnFailureListener { exception ->
        onError("Error fetching user document: ${exception.message ?: "Unknown error"}")
    }
}

/**
 * Retrieves course data associated with a specified courseId.
 *
 * @param courseId The unique identifier that identifies a certain course in Firestore
 * @param firestore An instance of firestore for database operations
 * @param onSuccess A callback function called when the course data is successfully retrieved.
 *                  Receives the retrieved CourseData object as a parameter.
 * @param onError A callback function called when an error occurs during data retrieval.
 *                  Receives an error message (String) as a parameter.
 */
fun getCourseData(courseId: String, firestore: FirebaseFirestore, onSuccess: (CourseData) -> Unit, onError: (String) -> Unit) {
    val courseDocRef = firestore.collection("courses").document(courseId)

    // Asynchronously fetch course doc with provided course ID
    courseDocRef.get().addOnSuccessListener { courseSnapshot ->
        // Convert the snapshot to a CourseData object
        val courseData = courseSnapshot.toObject<CourseData>()
        if(courseData != null) {
            onSuccess(courseData)
        } else {
            onError("Course document not found.")
        }
    }.addOnFailureListener { exception ->
        onError("Error fetching course document: ${exception.message ?: "Unknown error"}")
    }
}

/**
 * Retrieves the courses associated with a specified user ID.
 *
 * Combines getUserDocument and getCourseData to retrieve a user's courses as a list of CourseData.
 *
 * @param uid the given user id whose courses are to be retrieved.
 * @param onSuccess Callback function invoked when course retrieval is successful.
 *                  Receives a List of CourseData as a parameter.
 * @param onError Callback function invoked when an error occurs during course retrieval.
 *                  Receives an error message (String) as a parameter.
 */
    fun getUserCourses(uid: String, onSuccess: (List<CourseData>) -> Unit, onError: (String) -> Unit) {
        val firestore = Firebase.firestore

        // Retrieve the document for user with provided uid
        getUserDocument(uid, { userDocument ->
            // Extract the list of course ID's from the user document.
            val userCourses = userDocument.get("userCourses") as List<String>?
            // Check if user has any courses
            if(userCourses != null) {
                // Initialize a list of CourseData and keep track of number of courses left
                val courses = mutableListOf<CourseData>()
                var coursesToFetch = userCourses.size

                // Check if there are no courses left to fetch
                if(coursesToFetch == 0) {
                    onSuccess(emptyList())
                } else {
                    // Iterate through every courseId
                    for(courseId in userCourses) {
                        // Add the CourseData of the courseId to the List
                        getCourseData(courseId, firestore, { courseData ->
                            courses.add(courseData)
                            coursesToFetch--
                            if(coursesToFetch == 0) {
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