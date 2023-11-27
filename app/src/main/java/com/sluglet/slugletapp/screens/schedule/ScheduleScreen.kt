package com.sluglet.slugletapp.screens.schedule

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.himanshoe.kalendar.*
import com.himanshoe.kalendar.color.KalendarColor
import com.himanshoe.kalendar.color.KalendarColors
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.model.CourseData
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.Month
import kotlinx.datetime.TimeZone
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime


// Use https://github.com/hi-manshu/Kalendar for documentation on using the library

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen (
    openScreen: (String) -> Unit,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    // Getting the current date
    val currentDate = viewModel.currentDate.toLocalDateTime(TimeZone.UTC)
    Log.v("Date", "$currentDate")
    val userCourses = viewModel.userCourses.collectAsStateWithLifecycle(emptyList()).value
    Log.v("UserCourses", "$userCourses")
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val quarterStart = viewModel.quarterStart
    val quarterEnd = viewModel.quarterEnd

    // adding the events, still working on displaying it appropriately, currently shows up as dots below the dates
    // Assuming testList is a List<CourseData> containing course information

    val events = KalendarEvents(userCourses.flatMap { course ->
        val daysOfWeek = course.date_time.split(" ")[0] // Take only the first part of the string
        val courseName = course.course_name
        val courseDetails = "${course.course_number} - ${course.location}"

        // Split the days by looking for capital letters
        val days = daysOfWeek.split(Regex("(?=[A-Z])")).filter { it.isNotBlank() }

        val eventsForCourse = days.flatMap { day ->
            val dayOfWeek = getDayOfWeekFromString(day)
            val currentDayOfWeek = currentDate.dayOfWeek
            val daysToAdd = (dayOfWeek.ordinal - currentDayOfWeek.ordinal + 7) % 7

            var date = currentDate.date.plus(daysToAdd, DateTimeUnit.DAY)

            val eventsList = mutableListOf<KalendarEvent>()

            // Create events for all occurrences of the specific weekday between January 5th and June 15th
            while (date in quarterStart..quarterEnd) {
                eventsList.add(
                    KalendarEvent(
                        date = date,
                        eventName = courseName,
                        eventDescription = courseDetails
                    )
                )
                date = date.plus(7, DateTimeUnit.DAY) // Move to the next occurrence of the same weekday
            }

            eventsList
        }

        eventsForCourse
    })

    // broken crashes the app, working on it
    val kalendarColors = KalendarColors(
        listOf(
            KalendarColor(
                backgroundColor = Color.Blue, // Background color for selected dates
                dayBackgroundColor = Color.White, // Background color for regular days
                headerTextColor = Color.Black // Text color for the calendar header
            )
            // Add more KalendarColor objects as needed
        )
    )
    // val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    ScheduleScreenContent(
        currentDate = currentDate.date,
        events = events,
        testList = userCourses,
        selectedDate = selectedDate,
        onDateSelected = viewModel::onDateSelected
    ) // Pass selectedDate here
}

@RequiresApi(Build.VERSION_CODES.O)
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

@RequiresApi(Build.VERSION_CODES.O)
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

TODO: implement user's course List as argument to eliminate hard coding of user's classes (ask max p)
 */
@Composable fun DisplayCourses (
    courses: List<CourseData>,
    day: String = ""
) {
    LazyColumn (
        state = rememberLazyListState(),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        items(
            items = courses.filter {
                it.date_time.contains(day, ignoreCase = false)
            }
        ) {courseItem ->
            CourseBox(coursedata = courseItem)
        }

    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreenContent(
    currentDate: LocalDate,
    events:   KalendarEvents,
    testList: List<CourseData>,
    selectedDate: LocalDate?,
    onDateSelected: (LocalDate) -> Unit,
    kalendarColors: KalendarColors = KalendarColors(emptyList())
) {

    Column(
        modifier = Modifier
    ) {
        Kalendar(
            currentDay = currentDate,
            kalendarType = KalendarType.Firey,
            events = events,
            // kalendarColors = kalendarColors,
            modifier = Modifier
                .padding(5.dp)
                .clip(shape = RoundedCornerShape(10.dp)),
            onDayClick = { clickedDate, _ ->
                onDateSelected(clickedDate) // Invoke the callback with the clicked date
            }
        ) {

        }

        DisplayCourses(courses = testList, day = dayOfWeekParam(selectedDate) )

    }
}