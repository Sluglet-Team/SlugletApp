package com.sluglet.slugletapp.screens.home

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sluglet.slugletapp.model.CourseData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
): SlugletViewModel(logService) {
    val courses = MutableStateFlow<List<CourseData>>(emptyList())

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
    private fun getCourseData(courseId: String, firestore: FirebaseFirestore, onSuccess: (CourseData) -> Unit, onError: (String) -> Unit) {
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
     * Uses firestore instance and getCourseData to retrieve a user's courses as a list of CourseData.
     *
     * @param uid the given user id whose courses are to be retrieved.
     * @param onSuccess Callback function invoked when course retrieval is successful.
     *                  Receives a List of CourseData as a parameter.
     * @param onError Callback function invoked when an error occurs during course retrieval.
     *                  Receives an error message (String) as a parameter.
     */
    fun getUserCourses(onSuccess: (List<CourseData>) -> Unit, onError: (String) -> Unit) {
        val firestore = Firebase.firestore
        val currentUser = Firebase.auth.currentUser

        // Check if the user is authenticated
        if (currentUser != null) {
            //TODO test with valid auth
            val uid = currentUser.uid

            // Retrieve the user document directly using the current user's UID
            val userDocRef = firestore.collection("users").document(uid)

            userDocRef.get().addOnSuccessListener { userDocument ->
                // Extract the list of course IDs from the user document.
                val userCourses = userDocument.get("userCourses") as List<String>?

                // Check if user has any courses
                if (userCourses != null) {
                    // Initialize a list of CourseData and keep track of the number of courses left
                    val courses = mutableListOf<CourseData>()
                    var coursesToFetch = userCourses.size

                    // Check if there are no courses left to fetch
                    if (coursesToFetch == 0) {
                        onSuccess(emptyList())
                    } else {
                        // Iterate through every courseId
                        for (courseId in userCourses) {
                            // Add the CourseData of the courseId to the List
                            getCourseData(courseId, firestore, { courseData ->
                                courses.add(courseData)
                                coursesToFetch--
                                if (coursesToFetch == 0) {
                                    onSuccess(courses)
                                }
                            }, { error ->
                                // TODO handle errors
                                onError(error)
                            })
                        }
                    }
                } else {
                    onSuccess(emptyList())
                }
            }.addOnFailureListener { exception ->
                onError("Error fetching user document: ${exception.message ?: "Unknown error"}")
            }
        } else {
            onError("User not authenticated.")
        }
    }
}