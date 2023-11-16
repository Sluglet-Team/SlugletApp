package com.sluglet.slugletapp.screens.map

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.sluglet.slugletapp.OSMaps.CameraPositionState
import com.sluglet.slugletapp.OSMaps.CameraProperty
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.screens.SlugletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    logService: LogService,

) : SlugletViewModel(logService) {
    private var _cameraState = mutableStateOf(
        CameraPositionState(
            CameraProperty(
                // 36°59'44.5"N 122°03'35.5"W
                geoPoint = GeoPoint(36.99582810669116, -122.05824150361903),
                zoom = 15.0
            )
        )
    )
    val cameraState: State<CameraPositionState> = _cameraState


}