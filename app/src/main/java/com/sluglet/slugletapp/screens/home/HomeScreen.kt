package com.sluglet.slugletapp.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val userCourses = viewModel.courses.collectAsStateWithLifecycle(emptyList())

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        LazyColumn {
            items(userCourses.value) { courseItem ->
                CourseBox(coursedata = courseItem)
            }
        }
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
                        //TODO handle errors
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