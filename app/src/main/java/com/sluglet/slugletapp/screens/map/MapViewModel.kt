package com.sluglet.slugletapp.screens.map

import androidx.compose.runtime.mutableStateOf
import com.sluglet.slugletapp.OSMaps.CameraPositionState
import com.sluglet.slugletapp.OSMaps.CameraProperty
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.screens.SlugletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    logService: LogService,

) : SlugletViewModel(logService) {


}