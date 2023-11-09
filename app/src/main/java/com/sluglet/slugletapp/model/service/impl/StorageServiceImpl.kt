package com.sluglet.slugletapp.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.User
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.StorageService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import android.util.Log

class StorageServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : StorageService {
    // TODO: get data from firestore
    override val courses: Flow<List<CourseData>>
        get() =
            firestore.collection(COURSE_COLLECTION).dataObjects()
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

    companion object {
        private const val COURSE_COLLECTION = "data"
        private const val USER_COLLECTION = "users"
    }
}