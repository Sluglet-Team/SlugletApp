package com.sluglet.slugletapp.screens.schedule

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
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

// Use https://github.com/hi-manshu/Kalendar for documentation on using the library

@Composable
fun ScheduleScreen (
    openScreen: (String) -> Unit,
    viewModel: ScheduleViewModel = hiltViewModel()
) {
    // Getting the current date
    val currentInstant: Instant = Clock.System.now()
    val currentLocalDateTime: LocalDateTime = currentInstant.toLocalDateTime(TimeZone.currentSystemDefault())
    val currentDate: LocalDate = currentLocalDateTime.date

    // adding the events, still working on displaying it appropriately, currently shows up as dots below the dates
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
    ScheduleScreenContent(currentDate = currentDate, events = events)
}

@Composable
fun ScheduleScreenContent(
    currentDate: LocalDate,
    events:   KalendarEvents,
   // kalendarColors: KalendarColors
) {
    Column(

    ) {
        Kalendar(
            currentDay = currentDate,
            kalendarType = KalendarType.Firey,
            events = events,
           // kalendarColors = kalendarColors,
            modifier = Modifier
                .padding(10.dp)
                .clip(shape = RoundedCornerShape(10.dp))

        ) {

        }

        val testCourse = CourseData (
            courseNum = "CSE 115A",
            courseName = "Intro to Software Engineering",
            location = "Basking Auditorium 1",
            dateTime = "MWF 8:00am-9:00am",
            profName = "Julig"
        )
        CourseBox(coursedata = testCourse)
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

    ScheduleScreenContent(currentDate = currentDate, events = events)
}