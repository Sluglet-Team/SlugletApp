package com.sluglet.slugletapp.screens.map

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
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
import com.sluglet.slugletapp.resources
/*
A composable that renders the Search Screen
Uses a CourseBox composable along with a SearchTextField
 */
@Composable
fun MapScreen (
    openScreen: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    // Gets state from the view model for the camera position
    val cameraPositionState = viewModel.cameraState
    // Collect as state with lifecycle means that anytime the state of the flow changes
    // the value will change and cause recomposition
    val userCourses by viewModel.userCourses.collectAsStateWithLifecycle(emptyList())
    val courseToDisplay = viewModel.courseToDisplay.collectAsStateWithLifecycle(null).value

    // Use this modifier to modify the look of the map
    val mapModifier = Modifier
        .padding(start = 10.dp, top = 10.dp, end = 10.dp)
        .clip(RoundedCornerShape(10.dp))
        .shadow(elevation = 10.dp)

    OSMaps (
        cameraPositionState = cameraPositionState,
        modifier = mapModifier
    ) {
        userCourses.forEach { course->
            CourseMarker(
                course = course,
                markerIcon = ResourcesCompat.getDrawable(resources(), R.drawable.edu_map_pin_red, null)
            )
        }
        // Course to display is null until map button clicked on search screen
        if (courseToDisplay != null) {
            CourseMarker(
                course = courseToDisplay,
                markerIcon = ResourcesCompat.getDrawable(resources(), R.drawable.edu_map_pin_green, null)
            )
        }
    }
}
