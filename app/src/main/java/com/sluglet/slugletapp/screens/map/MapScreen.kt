package com.sluglet.slugletapp.screens.map

import android.util.Log
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sluglet.slugletapp.BuildConfig
import com.sluglet.slugletapp.OSMaps.OSMaps
import com.sluglet.slugletapp.OSMaps.Polyline
import com.sluglet.slugletapp.R
import com.sluglet.slugletapp.common.composables.CourseMarker
import com.sluglet.slugletapp.model.service.NavService
import com.sluglet.slugletapp.resources
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import kotlin.coroutines.suspendCoroutine


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
        modifier = mapModifier,
        onFirstLoadListener = { }
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

        val start = GeoPoint(-122.06085,36.99467,) // East Baskin
        val end = GeoPoint(-122.06511,36.953,) // Earth & Marine
        viewModel.setPath(start, end)

        if (viewModel.currentPath != null)
        {
            Log.v("mapScreen", viewModel.currentPath.toString())
            Polyline(viewModel.currentPath!!) {

            }
        }
        //val initRoute = road.mRouteHigh
        //Polyline(
        //    initRoute
        //) {}
    }
}