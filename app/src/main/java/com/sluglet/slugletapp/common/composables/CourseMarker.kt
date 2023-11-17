package com.sluglet.slugletapp.common.composables

import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.OSMaps.Marker
import com.sluglet.slugletapp.OSMaps.MarkerState
import com.sluglet.slugletapp.common.ext.smallSpacer
import com.sluglet.slugletapp.ui.theme.DarkBlue

// Composable for a map marker
@Composable
fun CourseMarker (
    course: CourseData,
    markerIcon: Drawable? = null
) {
    Marker(
        state = MarkerState(geoPoint = course.coord!!, rotation = 90f),
        title = "Title", // add title
        snippet = "Snippet", // add snippet
        icon = markerIcon
    ) {
        // Info Window
        Box(
            modifier = Modifier
                .smallSpacer()
                .background(color = DarkBlue, shape = RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Info Window Content
            Column(modifier = Modifier.smallSpacer()) {
                Text(text = course.course_name)
                Text(text = course.location, fontSize = 10.sp)
            }
        }
    }

}