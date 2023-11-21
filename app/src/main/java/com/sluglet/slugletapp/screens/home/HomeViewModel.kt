package com.sluglet.slugletapp.screens.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.sluglet.slugletapp.SETTINGS_SCREEN
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.User
import com.sluglet.slugletapp.model.service.AccountService
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
): SlugletViewModel(logService) {
    val courses = MutableStateFlow<List<CourseData>>(emptyList())

    init {
        viewModelScope.launch {
            try {
                //FIXME temp data: val userData = storageService.retrieveUserData(accountService.currentUserId)
                //val userData = storageService.retrieveUserData("UJ4GiIyQ5vuwKKvpTOhL")

                val listOfCourses = ArrayList<String>()
                listOfCourses.add("aqWMc7dO68Gxe8gickMS")
                listOfCourses.add("09nihTtaL2PLdkMACxnF")
                listOfCourses.add("7ZCCuQL6pyYi8lNLJURd")

                val userData = User(
                    email = "alecnavapanich@gmail.com",
                    name = "Alec Navapanich",
                    uid = "xtTyD1FBo6MXqaf9r1mhDqZFUNP2",
                    courses = listOfCourses
                )

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
    fun onSettingsClick(openScreen: (String) -> Unit) {
        openScreen(SETTINGS_SCREEN)
    }

}