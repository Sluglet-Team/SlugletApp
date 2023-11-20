package com.sluglet.slugletapp.screens.map

import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.sluglet.slugletapp.OSMaps.CameraPositionState
import com.sluglet.slugletapp.OSMaps.CameraProperty
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.MapService
import com.sluglet.slugletapp.screens.SlugletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    logService: LogService,
    private val mapService: MapService
) : SlugletViewModel(logService) {
    // FIXME: Would be ideal to keep the state of the map
    //        so that when navigating away and coming back
    //        it doesn't revert to this starting state
    private var _cameraState = mutableStateOf(
        CameraPositionState(
            CameraProperty(
                // 36°59'44.5"N 122°03'35.5"W
                // Starting point and zoom for the map
                geoPoint = GeoPoint(36.99582810669116, -122.05824150361903),
                zoom = 15.0
            )
        )
    )
    val cameraState: State<CameraPositionState> = _cameraState
    var courseToDisplay = mapService.courseToDisplay
    private val _markerList : MutableStateFlow<MutableList<CourseData>> = MutableStateFlow(
        mutableListOf()
    )
    val markerList = _markerList.asStateFlow()

    fun addMarker(course : CourseData) {
        _markerList.value.add(course)
    }

    fun removeMarker(course : CourseData) {
        _markerList.value.remove(course)
    }
}