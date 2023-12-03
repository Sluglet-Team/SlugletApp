package com.sluglet.slugletapp.model.service.impl

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.User
import com.sluglet.slugletapp.model.service.AccountService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider

class AccountServiceImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore) : AccountService {
    override val currentUserId: String
        get() = auth.currentUser?.uid.orEmpty()

    override val hasUser: Boolean
        get() = auth.currentUser != null

    override val currentUser: Flow<User>
        get() = callbackFlow {
            val listener =
                FirebaseAuth.AuthStateListener { auth ->
                    this.trySend(auth.currentUser?.let { User(uid = it.uid, email = it.email.toString()) } ?: User())
                }
            auth.addAuthStateListener(listener)
            awaitClose { auth.removeAuthStateListener(listener) }
        }

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
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.v("createAccount", "createAccount success")
                    val userMap = hashMapOf(
                        "email" to email,
                        "name" to "",
                        "uid" to auth.currentUser!!.uid,
                        "courses" to ArrayList<String>()
                    )
                    firestore.collection(USER_COLLECTION).document(auth.currentUser!!.uid).set(userMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.v("createAccount", "new account storage success")
                                Log.v("createAccount", "stored data at " + auth.currentUser!!.uid)
                            } else {
                                Log.v("createAccount", "new account storage failure")
                            }
                        }
                }
                else
                {
                    Log.v("createAccount", "createAccount failure")
                    Log.v("createAccount", "Email: -$email- Password: -$password-")
                }
            }
    }

    override suspend fun logIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).await()
    }

    override suspend fun addCourse(course: CourseData)
    {
        var userID = auth.currentUser!!.uid
        Log.v("addCourse", "Accessing Firestore User $userID")
        val userRef = firestore.collection(USER_COLLECTION).document(userID)
        userRef.get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.v("addCourse", "retrieved user data")
                    var userMap = task.result.data
                    Log.v("addCourse", "User Map for " + (userMap)!!["email"].toString())
                    val courses = ((userMap)!!["courses"] as ArrayList<String>)
                    courses.add(course.id)
                    (userMap)!!["courses"] = courses
                    firestore.collection(USER_COLLECTION).document(userID).set(userMap)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.v("addCourse", "store success")
                                Log.v("addCourse", "added " + course.id + " to $userID")
                            } else {
                                Log.v("addCourse", "store failure for $userID")
                            }
                        }
                }
                else
                {
                    Log.v("addCourse", "retrieveUserData failure")
                    Log.v("addCourse", "id: $userID")
                }
            }
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
    /**
     * Links the current account to the provided matching parameters
     *
     * @param email The email associated with the account to be linked to
     * @param password The password associated with the account to be linked to
     */
    override suspend fun linkAccounts(
        email: String,
        password: String,
    ) {
        val credential = EmailAuthProvider.getCredential(email, password)
        auth.currentUser!!.linkWithCredential(credential).await()
        firestore.collection(USER_COLLECTION).document(currentUserId).update("email", email).await()
    }

    override fun isUserAnonymous(): Boolean {
        val auth = FirebaseAuth.getInstance()
        val currUser = auth.currentUser
        if(currUser?.isAnonymous == true){
            return true
        }
        return false
    }

    companion object {
        private const val USER_COLLECTION = "users"
    }
}