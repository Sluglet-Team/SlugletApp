package com.sluglet.slugletapp.model.service.impl

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.User
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
    // Get all courses
    override val courses: Flow<List<CourseData>>
        get() =
            firestore.collection(COURSE_COLLECTION).dataObjects()

    // Get user specific courses as a Flow List
    @OptIn(ExperimentalCoroutinesApi::class)
    override val userCourses: Flow<List<CourseData>>
        get() =
            // Grab the current user
            auth.currentUser.flatMapLatest {user ->
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
    override suspend fun storeUserData(user: User)
    {
        val userMap = hashMapOf(
            "email" to user.email,
            "name" to user.name,
            "uid" to user.uid,
            "courses" to user.courses
        )
        firestore.collection(USER_COLLECTION).document(user.uid).set(userMap)
            .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.v("storeUserData", "storeUserData success")
                Log.v("storeUserData", "stored data at " + user.uid)
            } else {
                Log.v("storeUserData", "storeUserData failure for user " + user.uid)
            }
        }
    }

    override suspend fun retrieveUserData(id: String): User?
        {
            Log.v("retrieveUserData", "Accessing Firestore User $id")
            //TODO: Make this function draw from local values if available
            val userRef = firestore.collection(USER_COLLECTION).document(id)
            var user : User? = null
            userRef.get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.v("retrieveUserData", "retrieveUserData success")
                        val doc = task.result
                        Log.v("retrieveUserData", "User Map for " + (doc.data)!!["email"].toString())
                        val courses = ((doc.data)!!["courses"] as ArrayList<String>)
                        var i = 0
                        for (course in courses)
                        {
                            Log.v("retrieveUserData", "Course $i: $course")
                            i += 1
                        }
                        user = User(
                            email = (doc.data)!!["email"].toString(),
                            name = (doc.data)!!["name"].toString(),
                            uid = (doc.data)!!["uid"].toString(),
                            courses = courses
                        )
                    }
                    else
                    {
                        Log.v("retrieveUserData", "retrieveUserData failure")
                        Log.v("retrieveUserData", "id: $id")
                    }
                }
            return user
        }

    /**
     * Retrieves course data associated with a specified courseId as a CourseData object.
     *
     * @param courseId The unique identifier that identifies a certain course in Firestore
     * @param firestore An instance of firestore for database operations
     * @param onSuccess A callback function called when the course data is successfully retrieved.
     *                  Receives the retrieved CourseData object as a parameter.
     * @param onError A callback function called when an error occurs during data retrieval.
     *                  Receives an error message (String) as a parameter.
     */
    override suspend fun getCourseData(
        courseId: String,
        onSuccess: (CourseData) -> Unit,
        onError: (String) -> Unit)
        {
            // Get reference to the document of the passed courseId
            val courseDocRef = firestore.collection("courses").document(courseId)

            // Cast the document to type CourseData
            courseDocRef.get().addOnSuccessListener { courseSnapshot ->
                val courseData = courseSnapshot.toObject<CourseData>()
                if(courseData != null){
                    onSuccess(courseData)
                } else {
                    onError("Course document not found.")
                }
            }.addOnFailureListener { exception ->
                onError("Error fetching course document: ${exception.message ?: "Unknown error"}")
            }
        }



    companion object {
        private const val USER_COLLECTION = "users"
        private const val COURSE_COLLECTION = "courses2023"
        private const val USER_COURSES = "courses"
        private const val TEST_COURSE_COLLECTION3 = "courses3"
    }
}