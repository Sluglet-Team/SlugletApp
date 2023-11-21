package com.sluglet.slugletapp.OSMaps

import androidx.compose.runtime.CompositionContext
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

internal class MarkerNode(
    val mapView: MapView,
    val markerState: MarkerState,
    val marker: Marker,
    var onMarkerClick: (Marker) -> Boolean
) : MapNode {

    override fun onAttached() {
        markerState.marker = marker
    }

    override fun onRemoved() {
        markerState.marker = null
        marker.remove(mapView)
    }

    fun setupListeners() {
        marker.setOnMarkerClickListener { marker, _ ->
            val click = onMarkerClick.invoke(marker)
            if (marker.isInfoWindowShown) {
                marker.closeInfoWindow()
            } else {
                marker.showInfoWindow()
            }
            click
        }
    }
}