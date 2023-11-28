package com.sluglet.slugletapp.OSMaps

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.currentComposer
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay

@Composable
internal fun MapUpdater(
    mapProperties: MapProperties,
    mapListeners: MapListeners,
    cameraPositionState: CameraPositionState,
    overlayManagerState: OverlayManagerState
) {
    val mapViewComposed = (currentComposer.applier as MapApplier).mapView

    ComposeNode<MapPropertiesNode, MapApplier>(factory = {
        MapPropertiesNode(mapViewComposed, mapListeners, cameraPositionState, overlayManagerState)
    }, update = {

        set(mapProperties.mapOrientation) { mapViewComposed.mapOrientation = it }
        set(mapProperties.isMultiTouchControls) { mapViewComposed.setMultiTouchControls(it) }
        set(mapProperties.minZoomLevel) { mapViewComposed.minZoomLevel = it }
        set(mapProperties.maxZoomLevel) { mapViewComposed.maxZoomLevel = it }
        set(mapProperties.isFlingEnable) { mapViewComposed.isFlingEnabled = it }
        set(mapProperties.isUseDataConnection) { mapViewComposed.setUseDataConnection(it) }
        set(mapProperties.isTilesScaledToDpi) { mapViewComposed.isTilesScaledToDpi = it }
        set(mapProperties.tileSources) { if (it != null) mapViewComposed.setTileSource(it) }
        set(mapProperties.overlayManager) { if (it != null) mapViewComposed.overlayManager = it }

        set(mapProperties.isEnableRotationGesture) {
            val rotationGesture = RotationGestureOverlay(mapViewComposed)
            rotationGesture.isEnabled = it
            mapViewComposed.overlayManager.add(rotationGesture)
        }

        set(mapProperties.zoomButtonVisibility) {
            val visibility = when (it) {
                ZoomButtonVisibility.ALWAYS -> CustomZoomButtonsController.Visibility.ALWAYS
                ZoomButtonVisibility.SHOW_AND_FADEOUT -> CustomZoomButtonsController.Visibility.SHOW_AND_FADEOUT
                ZoomButtonVisibility.NEVER -> CustomZoomButtonsController.Visibility.NEVER
            }

            mapViewComposed.zoomController.setVisibility(visibility)
        }
    })
}