package com.sluglet.slugletapp.model.service.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.sluglet.slugletapp.model.User
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.StorageService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore) : AccountService {

    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {}
        //    val listener =
       //         FirebaseAuth.AuthStateListener { auth ->
        //            this.trySend(auth.currentUser?.let { User(it.uid, it.isAnonymous) } ?: User())
        //        }
         //   auth.addAuthStateListener(listener)
        //    awaitClose { auth.removeAuthStateListener(listener) }
        //}

    override suspend fun authenticate(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun sendRecoveryEmail(email: String) {
        auth.sendPasswordResetEmail(email).await()
    }

    override suspend fun createAnonymousAccount() {
        auth.signInAnonymously().await()
    }

    override suspend fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password).await()
        val user = User(email = email, name = "", uid = (auth.currentUser!!).uid, classes = emptyList())
        firestore.collection(AccountServiceImpl.USER_COLLECTION).document(user.uid).set(user)
    }

    override suspend fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun deleteAccount() {
        auth.currentUser!!.delete().await()
    }

    override suspend fun signOut() {
        if (auth.currentUser!!.isAnonymous) {
            auth.currentUser!!.delete()
        }
        auth.signOut()

        // Sign the user back in anonymously.
        createAnonymousAccount()
    }
    companion object {
        private const val LINK_ACCOUNT_TRACE = "linkAccount"
        private const val COURSE_COLLECTION = "data"
        private const val USER_COLLECTION = "testCollection"
    }
}