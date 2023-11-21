package com.sluglet.slugletapp.screens.home

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope

//TODO: Migrate functionality using these to AccountService
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException

import com.sluglet.slugletapp.SETTINGS_SCREEN
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.User
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
): SlugletViewModel(logService) {
    private val showError = mutableStateOf(false)
    val courses = MutableStateFlow<List<CourseData>>(emptyList())

    init {
        // Initialize the ViewModel asynchronously
        initializeViewModel()
    }

    private fun initializeViewModel() {
        viewModelScope.launch {
            // Call createAnonymousAccount when initializing the ViewModel
            if(!accountService.hasUser) {
                try {
                    createAnonymousAccount()
                    var anonymousUser = User()
                    anonymousUser = anonymousUser.copy(
                        email = "",
                        name = "",
                        uid = accountService.currentUserId,
                        courses = ArrayList<String>()
                    )
                    storageService.storeUserData(anonymousUser)

                    Log.v("AnonymousAuth", "User signed in anonymously")
                } catch (ex: FirebaseAuthException){
                    showError.value = true
                    throw ex
                }
            } else {
                val user = accountService.currentUserId

                if(isUserAnonymous()){
                    Log.v("AnonymousAuth", "User is signed in ANONYMOUSLY. uid: $user")
                } else {
                    Log.v("AnonymousAuth", "User is signed in. uid: $user")
                }
            }

            try {
                val userData = storageService.retrieveUserData(accountService.currentUserId)

                Log.v("UserData", "userData: $userData")
                if(userData != null) {
                    val courseDataList = mutableListOf<CourseData>()
                    for(courseId in userData.courses) {
                        storageService.getCourseData(courseId,
                            onSuccess = { courseData ->
                                courseDataList.add(courseData)
                                courses.value = courseDataList
                            }, onError = { error ->
                                Log.e("UserCourses", "Error fetching course data: $error")
                            })
                    }
                }
            } catch (e: Exception) {
                Log.e("UserData", "Error fetching user data: ${e.message}")
            }
        }
    }
    fun isUserAnonymous(): Boolean {
        val auth = FirebaseAuth.getInstance()
        val currUser = auth.currentUser
        if(currUser?.isAnonymous == true){
            return true
        }
        return false
    }

    private suspend fun createAnonymousAccount() {
        accountService.createAnonymousAccount()
    }
    fun onSettingsClick(openScreen: (String) -> Unit) {
        openScreen(SETTINGS_SCREEN)

}