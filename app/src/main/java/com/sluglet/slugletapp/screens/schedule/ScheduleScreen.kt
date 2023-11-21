package com.sluglet.slugletapp.screens.schedule

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarEvent
import com.himanshoe.kalendar.KalendarEvents
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColor
import com.himanshoe.kalendar.color.KalendarColors
import com.himanshoe.kalendar.ui.component.header.KalendarHeader
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.model.CourseData
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.toLocalTime
import com.himanshoe.kalendar.*
import kotlinx.datetime.DateTimeUnit



// Use https://github.com/hi-manshu/Kalendar for documentation on using the library

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreen (
    openScreen: (String) -> Unit,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    // Getting the current date
    val currentDate = LocalDate(2024, 1, 5) // Starting date, adjust as needed
//    val currentInstant: Instant = Clock.System.now()
//    val currentLocalDateTime: LocalDateTime = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    //val currentDate: LocalDate = currentLocalDateTime.date

    val testCourse = CourseData (
        courseNum = "CSE 115A",
        courseName = "Intro to Software Engineering",
        location = "Basking Auditorium 1",
        dateTime = "MWF 8:00am-9:00am",
        profName = "Julig"
    )

    val testCourse2 = CourseData(
        courseNum = "JPN 103",
        courseName = "Advanced Japanese",
        location = "Oakes Academy 222",
        dateTime = "MWF 4:00pm-5:05pm",
        profName = "Hoshi"
    )

    val testCourseTTh = CourseData(
        courseNum = "LING 100",
        courseName = "Phonetic World Langs",
        location = "Soc Sci 075",
        dateTime = "TuTh 11:40am-1:15pm",
        profName = "Rysling"
    )

    var testList = mutableListOf<CourseData>()
    testList.add(testCourse)
    testList.add(testCourse2)
    testList.add(testCourseTTh)


    val januaryFifth = LocalDate(2024, Month.JANUARY, 5)
    val juneLast = LocalDate(2024, Month.JUNE, 15)

    // adding the events, still working on displaying it appropriately, currently shows up as dots below the dates
    // Assuming testList is a List<CourseData> containing course information

    val events = KalendarEvents(testList.flatMap { course ->
        val daysOfWeek = course.dateTime.split(" ")[0] // Take only the first part of the string
        val courseName = course.courseName
        val courseDetails = "${course.courseNum} - ${course.location}"

        // Split the days by looking for capital letters
        val days = daysOfWeek.split(Regex("(?=[A-Z])")).filter { it.isNotBlank() }


        val eventsForCourse = days.flatMap { day ->
            val dayString = day.toString()
            val dayOfWeek = getDayOfWeekFromString(dayString)
            val currentDayOfWeek = currentDate.dayOfWeek
            val daysToAdd = (dayOfWeek.ordinal - currentDayOfWeek.ordinal + 7) % 7

            var date = currentDate.plus(daysToAdd, DateTimeUnit.DAY)

            val eventsList = mutableListOf<KalendarEvent>()

            // Create events for all occurrences of the specific weekday between January 5th and June 15th
            while (date in januaryFifth..juneLast) {
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
    val selectedDate = remember { mutableStateOf<LocalDate?>(null) }
    ScheduleScreenContent(
        currentDate = currentDate,
        events = events,
        testList = testList,
        selectedDate = selectedDate, // Pass selectedDate here
        onDateSelected = { clickedDate ->
            selectedDate.value = clickedDate // Update the selected date
        }
    )
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScheduleScreenContent(
    modifier: Modifier = Modifier,
    currentDate: LocalDate,
    events:   KalendarEvents,
    testList: List<CourseData>,
    selectedDate: MutableState<LocalDate?>,
    onDateSelected: (LocalDate) -> Unit
   // kalendarColors: KalendarColors
) {
    val selectedEvents = remember { mutableStateOf<List<KalendarEvent>>(emptyList()) }
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
        DisplayCourses(courses = testList, day = dayOfWeekParam(selectedDate.value) )
    }
}


/*
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
            CourseBox(coursedata = courseItem, onAddClick = null)
        }

    }
}


@Preview(showBackground = true)
@Composable fun ScheduleScreenPreview(

) {
    val currentInstant: Instant = Clock.System.now()
    val currentLocalDateTime: LocalDateTime = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    val currentDate: LocalDate = currentLocalDateTime.date
    val events = KalendarEvents(
        listOf(
            KalendarEvent(
                date = LocalDate(2023, 11, 1),
                eventName = "class1",
                eventDescription = ""

            ),
            KalendarEvent(
                date = LocalDate(2023, 11, 7),
                eventName = "class2",
                eventDescription = ""
            ),
            // Add more events as needed
        )
    )


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

    // ScheduleScreenContent(currentDate = currentDate, events = events)
}


/*
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

TODO: find Kalendar state to be able to pass day of the week to eliminate hard coding (ask tanuj)
// ok i feel this one is like impossible istg ive been working on this nonstop and i still cant figure it out
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
            CourseBox(coursedata = courseItem, onAddClick = null, onMapClick = null)
        }

    }
}

//val SelectedDate = {selectedDay: LocalDate -> selectedDay.dayOfWeek}
