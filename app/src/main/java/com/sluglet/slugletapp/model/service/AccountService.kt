package com.sluglet.slugletapp.model.service

import com.google.firebase.auth.FirebaseUser
import com.sluglet.slugletapp.model.User
import kotlinx.coroutines.flow.Flow

interface AccountService {
    val currentUserId: String
    val hasUser: Boolean

    suspend fun authenticate(email: String, password: String)
    suspend fun sendRecoveryEmail(email: String)
    suspend fun createAnonymousAccount()
    suspend fun createAccount(email: String, password: String)
    suspend fun logIn(email: String, password: String)
    suspend fun deleteAccount()
    suspend fun signOut()


}