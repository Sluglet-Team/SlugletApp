package com.sluglet.slugletapp.common.composables

import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.OSMaps.Marker
import com.sluglet.slugletapp.OSMaps.MarkerState
import com.sluglet.slugletapp.OSMaps.rememberMarkerState
import com.sluglet.slugletapp.common.ext.smallSpacer
import org.osmdroid.util.GeoPoint

// Composable for a map marker
@Composable
fun CourseMarker (
    course: CourseData,
    markerIcon: Drawable? = null,
    onNavClick: (CourseData) -> Boolean
) {
    val context = LocalContext.current
    val geoPoint = GeoPoint(course.latitude, course.longitude)
    val markerState = rememberMarkerState(
        geoPoint = geoPoint,
        rotation = 90f
    )
    Marker(
        state = markerState,
        title = "Title", // add title
        snippet = "Snippet", // add snippet
        icon = markerIcon
    ) {
        // Info Window
        Box(
            modifier = Modifier
                .smallSpacer()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(10.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            // Info Window Content
            Column(modifier = Modifier.smallSpacer()) {
                Text(text = course.course_name)
                Text(text = course.location, fontSize = 12.sp)
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black),
                    onClick = {
                        if (!onNavClick(course))
                        {
                            Toast.makeText(
                                context,
                                "Routing Failed, make sure your internet connection is stable",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                ) {
                    Text(text = "Navigate Here")
                }
            }
        }
    }

}