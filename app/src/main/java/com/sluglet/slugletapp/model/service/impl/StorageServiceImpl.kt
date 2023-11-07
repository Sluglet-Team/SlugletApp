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
            "classes" to user.classes
        )
        firestore.collection(USER_COLLECTION).document(user.uid).set(userMap).await()
    }
    override suspend fun retrieveUserData(id: String)
    {
        firestore.collection(USER_COLLECTION).document(id).get().await().toObject<User>()
    }

    companion object {
        private const val COURSE_COLLECTION = "data"
        private const val USER_COLLECTION = "users"
    }
}