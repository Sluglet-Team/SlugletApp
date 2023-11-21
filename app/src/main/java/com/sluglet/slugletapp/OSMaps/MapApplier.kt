package com.sluglet.slugletapp.OSMaps

import androidx.compose.runtime.AbstractApplier
import org.osmdroid.views.MapView as OSMapView
internal interface MapNode {
    fun onAttached() {}
    fun onRemoved() {}
}

private object MapNodeRoot : MapNode

internal class MapApplier(
    val mapView: OSMapView,
) : AbstractApplier<MapNode>(MapNodeRoot) {

    private val decorations = mutableListOf<MapNode>()

    init {
        // attachClickListeners()
    }

    override fun onClear() {
        mapView.overlayManager.clear()
    }

    override fun insertBottomUp(index: Int, instance: MapNode) {
        decorations.add(index, instance)
        instance.onAttached()
    }

    override fun insertTopDown(index: Int, instance: MapNode) {
        // insertBottomUp is preferred
    }

    override fun move(from: Int, to: Int, count: Int) {
        decorations.move(from, to, count)
    }

    override fun remove(index: Int, count: Int) {
        repeat(count) {
            decorations[index + it].onRemoved()
        }
        decorations.remove(index, count)
    }

    internal fun invalidate() = mapView.postInvalidate()
}
/*

    private fun attachClickListeners() {
        map.addCircleClickListener {
            decorations.nodeForCircle(it)
                ?.onCircleClick
                ?.invoke(it)
        }
        /*
        map.addGGroundOverlayClickListener {
            decorations.nodeForGroundOverlay(it)
                ?.onGroundOverlayClick
                ?.invoke(it)
        }

         */
        map.addPolygonClickListener {
            decorations.nodeForPolygon(it)
                ?.onPolygonClick
                ?.invoke(it)
        }
        map.addPolylineClickListener {
            decorations.nodeForPolyline(it)
                ?.onPolylineClick
                ?.invoke(it)
        }

        // Marker
        map.addMarkerClickListener { marker ->
            decorations.nodeForMarker(marker)
                ?.onMarkerClick
                ?.invoke(marker)
                ?: false
        }
        /*
        map.addWindowClickListener { marker ->
            decorations.nodeForMarker(marker)
                ?.onInfoWindowClick
                ?.invoke(marker)
        }
        map.addInfoWindowCloseListener { marker ->
            decorations.nodeForMarker(marker)
                ?.onInfoWindowClose
                ?.invoke(marker)
        }
        map.setOnInfoWindowLongClickListener { marker ->
            decorations.nodeForMarker(marker)
                ?.onInfoWindowLongClick
                ?.invoke(marker)
        }
        map.addMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(marker: Marker) {
                val markerDragState =
                    decorations.nodeForMarker(marker)?.markerDragState
                markerDragState?.dragState = DragState.DRAG
            }

            override fun onMarkerDragEnd(marker: Marker) {
                val markerDragState =
                    decorations.nodeForMarker(marker)?.markerDragState
                markerDragState?.dragState = DragState.END
            }

            override fun onMarkerDragStart(marker: Marker) {
                val markerDragState =
                    decorations.nodeForMarker(marker)?.markerDragState
                markerDragState?.dragState = DragState.START
            }
        })
        map.setInfoWindowAdapter(
            ComposeInfoWindowAdapter(
                mapView,
                markerNodeFinder = { decorations.nodeForMarker(it) }
            )
        )
    }
         */
}

private fun MutableList<MapNode>.nodeForCircle(circle: Circle): CircleNode? =
    first { it is CircleNode && it.circle == circle } as? CircleNode

private fun MutableList<MapNode>.nodeForMarker(marker: Marker): MarkerNode? =
    first { it is MarkerNode && it.marker == marker } as? MarkerNode

private fun MutableList<MapNode>.nodeForPolygon(polygon: Polygon): PolygonNode? =
    first { it is PolygonNode && it.polygon == polygon } as? PolygonNode

private fun MutableList<MapNode>.nodeForPolyline(polyline: Polyline): PolylineNode? =
    first { it is PolylineNode && it.polyline == polyline } as? PolylineNode

private fun MutableList<MapNode>.nodeForGroundOverlay(
    groundOverlay: GroundOverlay
): GroundOverlayNode? =
    first { it is GroundOverlayNode && it.groundOverlay == groundOverlay } as? GroundOverlayNode


     */
