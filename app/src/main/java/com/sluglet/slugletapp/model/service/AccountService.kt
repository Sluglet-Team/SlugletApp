package com.sluglet.slugletapp.model.service

import android.util.Log
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseUser
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.User
import com.sluglet.slugletapp.model.service.impl.StorageServiceImpl
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    val currentUser: Flow<User>

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun createAccount(email: String, password: String)
    suspend fun logIn(email: String, password: String)
    suspend fun addCourse(course: CourseData)
    suspend fun deleteAccount()
    suspend fun signOut()
    suspend fun linkAccounts(email: String, password: String): Boolean


}