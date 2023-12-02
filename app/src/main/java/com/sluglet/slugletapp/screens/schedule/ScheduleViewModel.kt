package com.sluglet.slugletapp.screens.schedule

import android.Manifest
import android.os.Build
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.SavedStateHandle
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.sluglet.slugletapp.common.ext.hasNotificationPermission
import com.sluglet.slugletapp.model.CourseData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.NotificationService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.*

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val notificationService: NotificationService
) : SlugletViewModel(logService) {
    val userCourses = storageService.userCourses
    val currentDate = Clock.System.now()
    private var _selectedDate: MutableStateFlow<LocalDate?> = MutableStateFlow(null)
    val selectedDate = _selectedDate.asStateFlow()
    // TODO: add quarter start date and end date gathering via service
    val quarterStart = FALL_START
    val quarterEnd = FALL_END
    private val startTimes = mutableListOf<LocalTime>()

    fun addDummyStartTimes() {
        // Adding some dummy values to startTimes for testing
        startTimes.apply {
            add(LocalTime.parse("18:56"))
            add(LocalTime.parse("10:30"))
            add(LocalTime.parse("13:45"))
        }
        scheduleNotifications()
    }
    fun updateStartTimesFromCourses(courses: List<CourseData>) {
        startTimes.clear()
        courses.forEach { course ->
            val courseStartTime = course.date_time.split(" ")[1].split("-")[0].toLocalTime()
            startTimes.add(courseStartTime)
        }
        scheduleNotifications()
    }


    // FIXME(Tanuj): Notifications are showing, but I haven't done anything to handle
    //               time events, just testing to make sure they show.
    //               Maybe an init { } for this viewModel that creates an alarm
    //               for this time.  Idk exactly.
    fun scheduleNotifications() {

        for (startTime in startTimes) {
            scheduleNotificationForTime(startTime)
        }
//        launchCatching {
//            notificationService.showNotification()
//        }
    }

    private fun scheduleNotificationForTime(startTime: LocalTime) {
        notificationService.scheduleNotificationAtTime(startTime.hour, startTime.minute)
    }

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
        // FIXME(Tanuj): I put the notification call here to test
        //               Click on a date and confirm that it works the way you intended
        //               Then REMOVE this call, cause this is not intended functionality
       // notificationService.showNotification()
    }


    companion object {
        private val FALL_START = LocalDate(2023, 9, 28)
        private val FALL_END = LocalDate(2023, 12, 15)
    }

}