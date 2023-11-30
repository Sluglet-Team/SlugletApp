package com.sluglet.slugletapp.OSMaps

import android.util.Log
import org.osmdroid.events.DelayedMapListener
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay

internal class MapPropertiesNode(
    val mapViewComposed: MapView,
    val mapListeners: MapListeners,
    private val cameraPositionState: CameraPositionState,
    overlayManagerState: OverlayManagerState
) : MapNode {

    private var delayedMapListener: DelayedMapListener? = null
    private var eventOverlay: MapEventsOverlay? = null

    init {
        overlayManagerState.setMap(mapViewComposed)
        cameraPositionState.setMap(mapViewComposed)
    }

    override fun onAttached() {
        // set zoom must come first
        mapViewComposed.controller.setZoom(cameraPositionState.zoom)
        mapViewComposed.controller.setCenter(cameraPositionState.geoPoint)


        delayedMapListener = DelayedMapListener(object : MapListener {
            override fun onScroll(event: ScrollEvent?): Boolean {
                val currentGeoPoint =
                    mapViewComposed.let { GeoPoint(it.mapCenter.latitude, it.mapCenter.longitude) }
                cameraPositionState.geoPoint = currentGeoPoint
                return false
            }

            override fun onZoom(event: ZoomEvent?): Boolean {
                val currentZoom = mapViewComposed.zoomLevelDouble
                cameraPositionState.zoom = currentZoom
                return false
            }
        }, 1000L)

        mapViewComposed.addMapListener(delayedMapListener)

        val eventsReceiver = object : MapEventsReceiver {
            override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                p?.let { mapListeners.onMapClick.invoke(it) }
                return true
            }

            override fun longPressHelper(p: GeoPoint?): Boolean {
                p?.let { mapListeners.onMapLongClick.invoke(it) }
                return true
            }
        }

        eventOverlay = MapEventsOverlay(eventsReceiver)

        mapViewComposed.overlayManager.add(eventOverlay)

        if (mapViewComposed.isLayoutOccurred) {
            mapListeners.onFirstLoadListener.invoke("")
        }
    }
    override fun onRemoved() {
        super.onRemoved()
        delayedMapListener?.let { mapViewComposed.removeMapListener(it) }
        eventOverlay?.let { mapViewComposed.overlayManager.remove(eventOverlay) }
    }
}