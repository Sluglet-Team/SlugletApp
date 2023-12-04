package com.sluglet.slugletapp.screens.schedule

import android.Manifest
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.himanshoe.kalendar.*
import com.himanshoe.kalendar.color.KalendarColor
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.day.KalendarDayKonfig
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.common.ext.hasLocationPermission
import com.sluglet.slugletapp.common.ext.hasNotificationPermission
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.ui.theme.WaveDarkOrange
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toLocalTime


// Use https://github.com/hi-manshu/Kalendar for documentation on using the library

@OptIn(ExperimentalPermissionsApi::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen (
    openScreen: (String) -> Unit,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    // Getting the current date
    val currentDate = viewModel.currentDate.toLocalDateTime(TimeZone.currentSystemDefault())
    Log.v("Date", "$currentDate")
    val userCourses = viewModel.userCourses.collectAsStateWithLifecycle(emptyList()).value
    Log.v("UserCourses", "$userCourses")
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle(currentDate.date)
    val context = LocalContext.current
    val quarterStart = viewModel.quarterStart
    val quarterEnd = viewModel.quarterEnd
    val monthsInYear = 12
    val kalColorList = List(monthsInYear) {
        KalendarColor(
            backgroundColor = Color.Transparent,
            dayBackgroundColor = WaveDarkOrange,
            headerTextColor = MaterialTheme.colorScheme.onBackground
        )
    }
    val kalendarColors = KalendarColors(
        color = kalColorList
    )
    val kalendarDayKonfig = KalendarDayKonfig(
        size = 56.dp,
        textSize = 16.sp,
        textColor = MaterialTheme.colorScheme.onSurface,
        selectedTextColor = Color.White
    )

    val events = KalendarEvents(userCourses.flatMap { course ->
        // Take only the first part of the string
        val daysOfWeek = course.date_time.split(" ")[0]
        val courseName = course.course_name
        val courseDetails = "${course.course_number} - ${course.location}"

        // Main call to schedule alarms for the notification
        viewModel.updateStartTimesFromCourses(userCourses)

        // Uncomment this to use the testing function for notification
        //viewModel.addDummyStartTimes()

        // Split the days by looking for capital letters
        val days = daysOfWeek.split(Regex("(?=[A-Z])")).filter { it.isNotBlank() }

        val eventsForCourse = days.flatMap { day ->
            val dayOfWeek = getDayOfWeekFromString(day)
            val currentDayOfWeek = currentDate.dayOfWeek
            val daysToAdd = (dayOfWeek.ordinal - currentDayOfWeek.ordinal + 7) % 7
            val weekIncrement = 7
            var date = currentDate.date.plus(daysToAdd, DateTimeUnit.DAY)

            val eventsList = mutableListOf<KalendarEvent>()

            // Create events for all occurrences of the specific weekday between
            // start of quarter to end of quarter
            while (date in quarterStart..quarterEnd) {
                eventsList.add(
                    KalendarEvent(
                        date = date,
                        eventName = courseName,
                        eventDescription = courseDetails
                    )
                )
                // Move to the next occurrence of the same weekday
                date = date.plus(weekIncrement, DateTimeUnit.DAY)
            }

            eventsList
        }

        eventsForCourse
    })

    ScheduleScreenContent(
        currentDate = currentDate.date,
        events = events,
        courseList = userCourses,
        selectedDate = selectedDate,
        onDateSelected = viewModel::onDateSelected,
        kalendarColors = kalendarColors,
        kalendarDayKonfig = kalendarDayKonfig
    )
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberMultiplePermissionsState(
            permissions = listOf(
                Manifest.permission.POST_NOTIFICATIONS,
                Manifest.permission.SCHEDULE_EXACT_ALARM
            )
        )
        LaunchedEffect(!context.hasNotificationPermission()) {
            permissionState.launchMultiplePermissionRequest()
        }
    }

}

/**
 * Helpfer function to get the day from the course string
 */
fun getDayOfWeekFromString(day: String): DayOfWeek {
    return when (day) {
        "M" -> DayOfWeek.MONDAY
        "Tu" -> DayOfWeek.TUESDAY
        "W" -> DayOfWeek.WEDNESDAY
        "Th" -> DayOfWeek.THURSDAY
        "F" -> DayOfWeek.FRIDAY
        else -> DayOfWeek.MONDAY // Default day, adjust as needed
    }
}
fun dayOfWeekParam(day: LocalDate?): String {
    if (day != null) {
        return when (day.dayOfWeek) {
            DayOfWeek.MONDAY -> "M"
            DayOfWeek.TUESDAY -> "Tu"
            DayOfWeek.WEDNESDAY -> "W"
            DayOfWeek.THURSDAY -> "Th"
            DayOfWeek.FRIDAY -> "F"
            else -> ""
        }
    }
    else {
        return ""
    }
}
/**
DisplayCourses takes in a List of user's classes in the CourseData class,
as the parameter courseList, as well as the day of the selected date on the
calendar, as the parameter day, and iterates through user's classes, parsing
through each course's dateTime value to find if the class is on that day. The
values that should be passed to day should be:
""   - For an unselected date, as this will show all courses the user has
"M"  - To show classes that have times on Monday
"Tu" - To show classes that have times on Tuesday
"W"  - To show classes that have times on Wednesday
"Th" - To show classes that have times on Thursday
"F"  - To show classes that have times on Friday
These values are in accordance to the symbols for days on the firebase csv,
so if these symbols for the days were to be changed, then the argument
standards for this function should also be changed.
 */
@Composable fun DisplayCourses (
    courses: List<CourseData>,
    day: String = ""
) {
    LazyColumn (
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(
            items = courses.filter {
                it.date_time.contains(day, ignoreCase = false)
            }
        ) {courseItem ->
            CourseBox(coursedata = courseItem, onAddClick = null, onMapClick = null)
        }

    }
}

@Composable
fun ScheduleScreenContent(
    currentDate: LocalDate,
    events:   KalendarEvents,
    courseList: List<CourseData>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    kalendarColors: KalendarColors = KalendarColors(emptyList()),
    kalendarDayKonfig: KalendarDayKonfig
) {

    Column(
        modifier = Modifier
    ) {
        Kalendar(
            currentDay = currentDate,
            kalendarType = KalendarType.Firey,
            events = events,
            kalendarColors = kalendarColors,
            kalendarDayKonfig = kalendarDayKonfig,
            modifier = Modifier
                .padding(5.dp)
                .clip(shape = RoundedCornerShape(10.dp)),
            onDayClick = { selectedDate, _ ->
                    onDateSelected(selectedDate)
            }
        )
        DisplayCourses(courses = courseList, day = dayOfWeekParam(selectedDate) )

    }
}