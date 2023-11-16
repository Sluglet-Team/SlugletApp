package com.sluglet.slugletapp.screens.map

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import com.sluglet.slugletapp.OSMaps.CameraPositionState
import com.sluglet.slugletapp.OSMaps.OSMaps
import com.sluglet.slugletapp.OSMaps.rememberCameraPositionState
import org.osmdroid.util.GeoPoint


@Composable
fun MapScreen (
    openScreen: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val cameraPositionState = viewModel.cameraState.value
    OSMaps (
        cameraPositionState = cameraPositionState
    ) {

    }
}