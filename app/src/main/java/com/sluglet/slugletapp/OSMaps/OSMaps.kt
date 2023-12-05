package com.sluglet.slugletapp.OSMaps

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.location.Location
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionContext
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCompositionContext
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sluglet.slugletapp.BuildConfig
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.awaitCancellation
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView as OSMapView
enum class ZoomButtonVisibility {
    ALWAYS, NEVER, SHOW_AND_FADEOUT
}
@Composable
fun OSMaps (
    userLocation: LatLng? = null,
    cameraPositionState: CameraPositionState = rememberCameraPositionState(),
    overlayManagerState: OverlayManagerState = rememberOverlayManagerState(),
    mapProperties: MapProperties = DefaultMapProperties,
    modifier: Modifier = Modifier,
    onMapClick: (GeoPoint) -> Unit = {},
    onMapLongClick: (GeoPoint) -> Unit = {},
    onFirstLoadListener: () -> Unit = {},
    onMapLoaded: () -> Unit = {},
    onMyLocationButtonClick: () -> Boolean = { false },
    onMyLocationClick: (GeoPoint) -> Unit = {},
    content: (@Composable () -> Unit)? = null,
) {
    val context = LocalContext.current
    val osMapView = remember { OSMapView(context) }
    val mapListeners = remember {
        MapListeners()
    }.also {
        it.onMapClick = onMapClick
        it.onMapLongClick = onMapLongClick
        it.onFirstLoadListener = {
            onFirstLoadListener.invoke()
        }
    }
    /*
    // Allow pinch zoom
    osMapView.setMultiTouchControls(true)
    // Set to UCSC
    osMapView.controller.animateTo(GeoPoint(36.99762806599007, -122.05596073804293))
    // Zoom
    osMapView.controller.setZoom(16.0)
     */
    // LifeCycle Control
    MapLifecycle(mapView = osMapView)

    // AndroidView needed since OSM is not compose compatible
    // ie this makes it interoperable
    // This is what composes the map.
    AndroidView(
        factory = {
            osMapView
        },
        modifier = modifier
    )
    // rememberUpdatedState and friends are used here to make these values observable to
    // the subcomposition without providing a new content function each recomposition
    /*
    val mapClickListeners = remember { MapClickListener() }.also {
        it.indoorStateChangeListener = indoorStateChangeListener
        it.onMapClick = onMapClick
        it.onMapLongClick = onMapLongClick
        it.onMapLoaded = onMapLoaded
        it.onMyLocationButtonClick = onMyLocationButtonClick
        it.onMyLocationClick = onMyLocationClick
        it.onPOIClick = onPOIClick
    }
    val currentLocationSource by rememberUpdatedState(locationSource)
    val currentCameraPositionState by rememberUpdatedState(cameraPositionState)
    val currentContentPadding by rememberUpdatedState(contentPadding)
    val currentUiSettings by rememberUpdatedState(uiSettings)
    val currentMapProperties by rememberUpdatedState(properties)

     */

    val parentComposition = rememberCompositionContext()
    val currentContent by rememberUpdatedState(content)

    LaunchedEffect(Unit) {
        disposingComposition {
            osMapView.newComposition(osMapView, parentComposition) {
                MapUpdater(
                    mapProperties = mapProperties,
                    mapListeners = mapListeners,
                    cameraPositionState = cameraPositionState,
                    overlayManagerState = overlayManagerState
                )
                currentContent?.invoke()
            }
        }
    }
}
private suspend inline fun disposingComposition(factory: () -> Composition) {
    val composition = factory()
    try {
        awaitCancellation()
    } finally {
        composition.dispose()
    }
}
private suspend inline fun OSMapView.newComposition(
    mapView: OSMapView,
    parent: CompositionContext,
    noinline content: @Composable () -> Unit
): Composition {
    return Composition(
        MapApplier(mapView), parent
    ).apply {
        setContent(content)
    }
}

/**
 * Registers lifecycle observers to the local [OSMapView].
 */
@Composable
private fun MapLifecycle(mapView: OSMapView) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(context, lifecycle, mapView) {
        val mapLifecycleObserver = mapView.lifecycleObserver()
        val callbacks = mapView.componentCallbacks()

        lifecycle.addObserver(mapLifecycleObserver)
        context.registerComponentCallbacks(callbacks)

        onDispose {
            lifecycle.removeObserver(mapLifecycleObserver)
            context.unregisterComponentCallbacks(callbacks)
        }
    }
}

private fun OSMapView.lifecycleObserver(): LifecycleEventObserver =
    LifecycleEventObserver { _, event ->
        when (event) {
            Lifecycle.Event.ON_CREATE -> this.onCreate(context)
            // Lifecycle.Event.ON_START -> this.onStart()
            Lifecycle.Event.ON_RESUME -> this.onResume()
            Lifecycle.Event.ON_PAUSE -> this.onPause()
            // Lifecycle.Event.ON_STOP -> this.onStop()
            Lifecycle.Event.ON_DESTROY -> this.onDetach()
            else -> {}
        }
    }

private fun OSMapView.componentCallbacks(): ComponentCallbacks =
    object : ComponentCallbacks {
        override fun onConfigurationChanged(config: Configuration) {}

        override fun onLowMemory() {
            this@componentCallbacks.onLowMemory()
        }
    }

private fun OSMapView.onLowMemory() {
    TODO("needs to check if app is about to run out of memory")
}

private fun OSMapView.onCreate(context: Context) {
    val instance = org.osmdroid.config.Configuration.getInstance()
    instance.load(context, context.getSharedPreferences("osm", Context.MODE_PRIVATE))
    instance.userAgentValue = BuildConfig.APPLICATION_ID
}
