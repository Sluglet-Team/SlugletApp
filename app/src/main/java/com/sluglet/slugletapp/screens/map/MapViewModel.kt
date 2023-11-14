package com.sluglet.slugletapp.screens.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.screens.SlugletViewModel
import com.sluglet.slugletapp.screens.sign_up.SignUpUiState
import com.utsman.osmandcompose.rememberCameraState
import dagger.hilt.android.lifecycle.HiltViewModel
import org.osmdroid.util.GeoPoint
import javax.inject.Inject
import com.utsman.osmandcompose.OpenStreetMap
import com.utsman.osmandcompose.Marker
import com.utsman.osmandcompose.MarkerState
import com.utsman.osmandcompose.CameraState
import com.utsman.osmandcompose.CameraProperty
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import com.google.android.gms.measurement.api.AppMeasurementSdk.ConditionalUserProperty


@HiltViewModel
class MapViewModel @Inject constructor(
    logService: LogService,

) : SlugletViewModel(logService) {
    private var _cameraState = mutableStateOf<CameraState>(
        CameraState(
            CameraProperty(
                geoPoint = GeoPoint(36.9905, -122.0584),
                zoom = 16.0
            )
        )
    )
    val cameraState: State<CameraState> = _cameraState

    val markerList : MutableList<CourseData> = mutableStateListOf<CourseData>()

    fun addMarker(course : CourseData) {
        markerList.add(course)
    }

    fun removeMarker(course : CourseData) {
        markerList.remove(course)
    }



}