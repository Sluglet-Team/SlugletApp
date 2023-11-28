package com.sluglet.slugletapp.OSMaps

import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Polyline

internal class PolylineNode(
    private val mapView: MapView,
    val polyline: Polyline,
    var onPolylineClick: (Polyline) -> Unit
) : MapNode {

    override fun onRemoved() {
        super.onRemoved()
        mapView.overlayManager.remove(polyline)
    }

    fun setupListeners() {
        polyline.setOnClickListener { polyline, _, _ ->
            onPolylineClick.invoke(polyline)
            if (polyline.isInfoWindowOpen) {
                polyline.closeInfoWindow()
            } else {
                polyline.showInfoWindow()
            }
            true
        }
    }
}