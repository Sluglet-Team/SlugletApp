package com.sluglet.slugletapp.model.service

import android.content.Context
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

interface NavService {
    /**
     * Retrieves a list of coordinates that when connected form a walking path between
     * two coordinates
     *
     * @param start GeoPoint representing the start of the path
     * @param end GeoPoint representing the end of the path
     */
    suspend fun getRouteCoords(start: GeoPoint, end: GeoPoint): ArrayList<GeoPoint>
}