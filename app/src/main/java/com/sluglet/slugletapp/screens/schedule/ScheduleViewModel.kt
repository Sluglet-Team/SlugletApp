package com.sluglet.slugletapp.screens.schedule

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
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

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

    /**
     * Function for testing notification
     * set time in start times and change the date in call to schedule notification at time
     * Sunday ->1, Monday->2, ... Saturday ->7
     */
    fun addDummyStartTimes() {
        // Adding some dummy values to startTimes for testing
        startTimes.apply {
            add(LocalTime.parse("20:10"))
            add(LocalTime.parse("16:57"))
            add(LocalTime.parse("16:58"))
        }
        for (startTime in startTimes) {
            notificationService.scheduleNotificationAtTime(1,startTime.hour, startTime.minute)
        }
    }

    /**
     * Function to handle events when a date is clicked
     */
    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    /**
     * Helper function to get the day from courses
     */
    private fun getIntDayOfWeekFromString(day: String): Int {
        return when (day) {
            "M" -> 2
            "Tu" -> 3
            "W" -> 4
            "Th" -> 5
            "F" -> 6
            "S" ->7
            else -> 2 // Default day (Monday) represented as 2
        }
    }
    /**
     * Helper function to convert string to actual time
     */
    fun convertStringToLocalTime(timeString: String): LocalTime {
        val timeComponents = timeString.split(":")
        var hour = timeComponents[0].toInt()
        val minute = timeComponents[1].take(2).toInt()
        val isPM = timeString.takeLast(2).equals("PM", ignoreCase = true)

        if (isPM && hour < 12) {
            hour += 12
        } else if (!isPM && hour == 12) {
            hour = 0
        }

        return LocalTime(hour, minute)
    }

    /**
     * Main function that handles scheduling alarms for the courses,
     * Is called from ScheduleScreen.kt, needds the list of courses to be passed through as a parameter
     */
    fun updateStartTimesFromCourses(courses: List<CourseData>) {
        courses.forEach { course ->
            val courseTime = course.date_time.substringAfterLast(" ").split("-")
            val startTimeString = courseTime[0]
            val startTime = convertStringToLocalTime(startTimeString)
            val daysOfWeek = course.date_time.split(" ")[0]
            val days = daysOfWeek.split(Regex("(?=[A-Z])")).filter { it.isNotBlank() }
            days.forEach { day ->
                val dayOfWeek = getIntDayOfWeekFromString(day)
                // Scheduling notification for the given day and time
                notificationService.scheduleNotificationAtTime(dayOfWeek, startTime.hour, startTime.minute)
            }
        }
    }


    companion object {
        private val FALL_START = LocalDate(2023, 9, 28)
        private val FALL_END = LocalDate(2023, 12, 15)
    }

}