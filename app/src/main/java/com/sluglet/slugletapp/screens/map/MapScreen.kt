package com.sluglet.slugletapp.screens.map

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.sluglet.slugletapp.OSMaps.OSMaps


@Composable
fun MapScreen (
    openScreen: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    OSMaps (

    ) {

    }
}