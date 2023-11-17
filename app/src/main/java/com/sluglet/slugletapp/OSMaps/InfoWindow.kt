package com.sluglet.slugletapp.OSMaps

import android.view.View
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.infowindow.InfoWindow

class InfoWindow(view: View, mapView: MapView) : InfoWindow(view, mapView) {
    override fun onOpen(item: Any?) {
    }

    override fun onClose() {
    }
}