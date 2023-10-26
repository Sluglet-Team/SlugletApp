package com.sluglet.slugletapp.model.service.impl

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.firestore.ktx.dataObjects
import com.google.firebase.firestore.ktx.toObject
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.StorageService
import kotlinx.coroutines.flow.Flow
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
     override suspend fun getCourse(courseID: String): CourseData? =
        firestore.collection(COURSE_COLLECTION).document(courseID).get().await().toObject()




    companion object {
        private const val COURSE_COLLECTION = "data"
    }
}