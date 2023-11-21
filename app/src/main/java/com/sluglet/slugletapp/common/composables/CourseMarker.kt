package com.sluglet.slugletapp.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Text
import com.sluglet.slugletapp.model.CourseData
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.MarkerState

// Composable for a map marker
@Composable
fun CourseMarker (
    course: CourseData
) {
    Marker(

        state = MarkerState(geoPoint = course.coord!!, rotation = 90f),
        title = "Title", // add title
        snippet = "Snippet" // add snippet
    ) {

        // Info Window
        Column(
            modifier = Modifier
                .size(100.dp)
                .background(color = Color.Gray, shape = RoundedCornerShape(7.dp)),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Info Window Content
            Text(text = course.course_name)
            Text(text = course.location, fontSize = 10.sp)
        }
    }

}
