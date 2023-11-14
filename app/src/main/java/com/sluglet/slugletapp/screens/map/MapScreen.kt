package com.sluglet.slugletapp.screens.map

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.location.Location
import android.view.View
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Applier
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.type.LatLng
import com.sluglet.slugletapp.common.composables.CourseBox
import com.sluglet.slugletapp.common.composables.CourseMarker
import com.sluglet.slugletapp.common.composables.SearchBox
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.screens.search.SearchViewModel
import com.sluglet.slugletapp.screens.sign_up.SignUpScreenContent
import com.utsman.osmandcompose.CameraState
import kotlinx.coroutines.awaitCancellation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.osmdroid.views.MapView as OSMapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.events.MapListener
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.rememberCameraState
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.Polyline


/*
A composable that renders the Search Screen
Uses a CourseBox composable along with a SearchTextField
 */
@Composable
fun MapScreen (
    openScreen: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val testCourse = CourseData (
        course_number = "CSE 115A",
        course_name = "Intro to Software Engineering",
        location = "Basking Auditorium 1",
        date_time = "MWF 8:00am-9:00am",
        prof_name = "Julig",
        coord = GeoPoint(36.9905, -122.0584)
    )
    viewModel.addMarker(testCourse)
    // Sets the content for the screen
    MapScreenContent(
        markerList = viewModel.markerList,
        cameraState = viewModel.cameraState.value,
    )

}
/*
Viewmodel Functions Needed:
getCameraState
setCameraState
getMarkers
setMarkers
onClick
*/
@Composable
fun MapScreenContent (
    modifier : Modifier = Modifier,
    markerList : MutableList<CourseData>,
    cameraState: CameraState
) {
    // Create Map
    OpenStreetMap(
        cameraState = cameraState
    ) {
        // Create Markers
        val iter = markerList.iterator()
        iter.forEach {
            CourseMarker(course = it)
        }
    }
}

