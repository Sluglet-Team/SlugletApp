package com.sluglet.slugletapp.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun CourseBox(
    course: CourseData,
    modifier: Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(color = Color.White, shape = RoundedCornerShape(size = 20.dp))
    ) {
        Column {
            // Course Prefix and Number
            CourseText(
                modifier = Modifier,
                text = course.courseNum,
                style = MaterialTheme.typography.displayLarge,
                color = Color.Black)
            CourseText(
                modifier = Modifier,
                text = course.courseName,
                style = MaterialTheme.typography.displaySmall,
                color = Color.Gray)
        }
    }
}

@Preview (showBackground = true)
@Composable
fun CourseBoxPreview (
) {
    val test = CourseData(
        courseNum = "CSE 115A",
        courseName = "Introduction to Software Engineering",
        profName = "Richard Julig",
        dateTime = "MWF 8:00am - 9:05am",
        location = "Baskin Auditorium 1"
    )
    CourseBox(course = test, modifier = Modifier)
}