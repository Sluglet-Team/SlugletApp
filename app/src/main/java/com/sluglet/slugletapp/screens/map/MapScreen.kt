package com.sluglet.slugletapp.screens.map

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sluglet.slugletapp.OSMaps.OSMaps
import com.sluglet.slugletapp.R
import com.sluglet.slugletapp.common.composables.CourseMarker
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.resources
import com.sluglet.slugletapp.screens.search.SearchViewModel
import kotlinx.coroutines.flow.emptyFlow
import org.osmdroid.util.GeoPoint


@Composable
fun MapScreen (
    openScreen: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    // Gets state from the view model for the camera position
    val cameraPositionState = viewModel.cameraState.value
    val courseToDisplay = viewModel.courseToDisplay.collectAsStateWithLifecycle(null).value
    val markerList = viewModel.markerList.collectAsStateWithLifecycle(emptyList()).value

    // Use this modifier to modify the look of the map
    val mapModifier = Modifier
        .padding(start = 10.dp, top = 10.dp, end = 10.dp)
        .clip(RoundedCornerShape(10.dp))
        .shadow(elevation = 10.dp)
    // FIXME: Remove
    val test = CourseData (
        course_number = "CSE 115A",
        course_name = "Intro to Software Engineering",
        location = "Baskin Auditorium 1",
        date_time = "MWF 8:00am-9:00am",
        prof_name = "Julig",
        latitude = 36.99924676842543,
        longitude = -122.060734332498
    )
    viewModel.addMarker(test)
    if (courseToDisplay != null) {
        viewModel.addMarker(courseToDisplay)
    }
    OSMaps (
        cameraPositionState = cameraPositionState,
        modifier = mapModifier
    ) {
        markerList.forEach { course->
            CourseMarker(
                course = course,
                markerIcon = ResourcesCompat.getDrawable(resources(), R.drawable.edu_map_pin, null)
            )
        }
    }
}