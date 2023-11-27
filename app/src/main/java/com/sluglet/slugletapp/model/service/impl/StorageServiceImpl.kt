package com.sluglet.slugletapp.model.service.impl

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.StorageService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {
    // TODO: get data from firestore
    override val courses: Flow<List<CourseData>>
        get() =
            firestore.collection(COURSE_COLLECTION).dataObjects()

    // Get user specific courses as a Flow List
    @OptIn(ExperimentalCoroutinesApi::class)
    override val userCourses: Flow<List<CourseData>>
        get() =
            // Grab the current user
            auth.currentUser.flatMapLatest {user ->
                Log.v("UID", "${user.uid}")
                var userRef: DocumentSnapshot? = null
                try {
                    userRef = firestore.collection(USER_COLLECTION).document(user.uid).get().await()
                } catch (e: Exception) {
                    // Error thrown by firestore, likely quota exceeded
                    Log.e("Error: override val userCourses", "$e")
                }
                // If firestore call succeeded, this should not be null
                if (userRef != null) {
                    // Get their courses, cast to any Array type
                    val courses = userRef.get(USER_COURSES) as ArrayList<*>
                    // If no courses, return an empty list, can't call firestore with empty list
                    if (courses.isEmpty()) {
                        emptyFlow()
                    }
                    // Else return the list of courses as a list of CourseData objects --> .dataObjects()
                    else {
                        // This only listens to course collection which is why no recomposition occurs
                        // when changing USER_COLLECTION.  Not ideal but not sure there is a work around.
                        firestore.collection(COURSE_COLLECTION).whereIn(FieldPath.documentId(), courses).dataObjects()
                    }
                }
                // Return an empty flow, worst case scenario
                else {
                    emptyFlow()
                }

            }
     override suspend fun getCourse(courseID: String): CourseData? =
        firestore.collection(COURSE_COLLECTION).document(courseID).get().await().toObject()




    companion object {
        private const val USER_COLLECTION = "users"
        private const val COURSE_COLLECTION = "courses2023"
        private const val USER_COURSES = "courses"
        private const val TEST_COURSE_COLLECTION3 = "courses3"
    }
}