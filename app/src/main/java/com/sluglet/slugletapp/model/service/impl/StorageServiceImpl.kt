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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import javax.inject.Inject


class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : StorageService {
    /**
     * Gets all courses as a flow.  Any changes to the query with .dataObjects()
     * will be emitted to the flow.
     * For example, if a course is added to the collection, the flow will be updated.
     */
    override val courses: Flow<List<CourseData>>
        get() =
            firestore.collection(COURSE_COLLECTION).dataObjects()

    /**
     * Obtains the current users courses as flow.  Any changes to the query with .dataObjects()
     * will be posted to the flow and can be collected as state within a view
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    override val userCourses: Flow<List<CourseData>>
        get() =
            auth.currentUser.flatMapLatest {user ->
                var userRef: DocumentSnapshot? = null
                try {
                    userRef = firestore.collection(USER_COLLECTION).document(user.uid).get().await()
                } catch (e: Exception) {
                    Log.e("Error: override val userCourses", "$e")
                }
                if (userRef != null) {
                    val courses = userRef.get(USER_COURSES) as ArrayList<*>?
                    if (courses.isNullOrEmpty()) {
                        emptyFlow()
                    }
                    else {
                        firestore.collection(COURSE_COLLECTION).whereIn(FieldPath.documentId(), courses).dataObjects()
                    }
                }
                else {
                    emptyFlow()
                }

            }

    /**
     * Gets a single course form the course collection
     * @param courseID The unique reference to the course in the collection.
     */
    override suspend fun getCourse(courseID: String): CourseData? =
        firestore.collection(COURSE_COLLECTION).document(courseID).get().await().toObject()

    /**
     * Stores user data into the users document in firestore.
     * If the user doc does not exist, it will be created as per firestore documentation
     * of set().
     * @param user The user object referencing the current user
     */
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

    override suspend fun retrieveUserData(id: String): User? = suspendCoroutine { continuation ->
            Log.v("retrieveUserData", "Accessing Firestore User $id")
            //TODO: Make this function draw from local values if available
            val userRef = firestore.collection(USER_COLLECTION).document(id)

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
                        val user = User(
                            email = (doc.data)!!["email"].toString(),
                            name = (doc.data)!!["name"].toString(),
                            uid = (doc.data)!!["uid"].toString(),
                            courses = courses
                        )
                        continuation.resume(user)
                    }
                    else
                    {
                        Log.v("retrieveUserData", "retrieveUserData failure")
                        Log.v("retrieveUserData", "id: $id")
                        continuation.resume(null)
                    }
                }
        }

    /**
     * Retrieves course data associated with a specified courseId as a CourseData object.
     *
     * @param courseId The unique identifier that identifies a certain course in Firestore
     * @param onSuccess A callback function called when the course data is successfully retrieved.
     *                  Receives the retrieved CourseData object as a parameter.
     * @param onError A callback function called when an error occurs during data retrieval.
     *                  Receives an error message (String) as a parameter.
     */
    override suspend fun getCourseData(
        courseId: String,
        onSuccess: (CourseData) -> Unit,
        onError: (String) -> Unit
    ) {
            // Get reference to the document of the passed courseId
            val courseDocRef = firestore.collection(COURSE_COLLECTION).document(courseId)

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

    /**
     * Deletes a Users document from Firestore. This should only be done after account deletion
     * within a launchCatching block to ensure that the account is deleted before deleting the
     * document.
     *
     * @param userId The unique userId referencing the document to be deleted. This must be obtained
     * before the account is deleted.
     */
    override suspend fun deleteUser(userId: String) {
        firestore.collection(USER_COLLECTION).document(userId).delete().await()
    }

    companion object {
        private const val USER_COLLECTION = "users"
        private const val COURSE_COLLECTION = "courses2023"
        private const val USER_COURSES = "courses"
        private const val TEST_COURSE_COLLECTION3 = "courses3"
    }
}