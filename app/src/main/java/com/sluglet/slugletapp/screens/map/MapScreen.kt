package com.sluglet.slugletapp.screens.map

import android.content.ComponentCallbacks
import android.content.Context
import android.content.res.Configuration
import android.location.Location
import android.view.View
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Applier
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentManager
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.type.LatLng
import kotlinx.coroutines.awaitCancellation
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import org.osmdroid.views.MapView as OSMapView
import org.osmdroid.util.GeoPoint
import org.osmdroid.events.MapListener
import org.osmdroid.tileprovider.MapTileProviderBasic
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.TilesOverlay

@Composable
fun MapScreenView (
    openScreen: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel(),
    modifier: Modifier = Modifier,
    onMapClick: (LatLng) -> Unit = {},
    onMapLongClick: (LatLng) -> Unit = {},
    onMapLoaded: () -> Unit = {},
    onMyLocationButtonClick: () -> Boolean = { false },
    onMyLocationClick: (Location) -> Unit = {},
    content: (@Composable () -> Unit)? = null,
) {
    val context = LocalContext.current
    val osMapView = remember { OSMapView(context) }
    // Allow pinch zoom
    osMapView.setMultiTouchControls(true)
    // Set to UCSC
    osMapView.controller.animateTo(GeoPoint(36.99762806599007, -122.05596073804293))
    // Zoom
    osMapView.controller.setZoom(12.0)
    // LifeCycle Control
    MapLifecycle(mapView = osMapView)

    // AndroidView needed since OSM is not compose compatible
    // ie this makes it interoperable
    // This is what composes the map.
    AndroidView(
        factory = {
            osMapView
        },
        modifier = Modifier
            .padding(10.dp)
            .clip(RoundedCornerShape(10.dp))
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

    // TODO: This currently doesn't do anything
    LaunchedEffect(Unit) {
        disposingComposition {
            osMapView.newComposition(osMapView, parentComposition) {
                // TODO: Need a MapUpdater.kt to handle MapUpdates
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
            //Lifecycle.Event.ON_STOP -> this.onStop()
            // TODO: Implemement onDestroy()
            //Lifecycle.Event.ON_DESTROY -> this.onDestroy()
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
    org.osmdroid.config.Configuration.getInstance()
        .load(context, context.getSharedPreferences("osm", Context.MODE_PRIVATE))
}
