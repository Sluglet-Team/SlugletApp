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
import com.himanshoe.kalendar.Kalendar
import com.himanshoe.kalendar.KalendarType
import com.himanshoe.kalendar.color.KalendarColors
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.model.CourseData
import kotlinx.datetime.LocalDate
import java.time.LocalDateTime

@Composable
fun ScheduleScreen (

) {

}

@Composable
fun ScheduleScreenContent(

) {
    Column(

    ) {
        Kalendar(
            currentDay = null,
            kalendarType = KalendarType.Firey,
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
    ScheduleScreenContent()
}