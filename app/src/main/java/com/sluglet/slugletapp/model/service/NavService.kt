package com.sluglet.slugletapp.model.service

import android.content.Context
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint

interface NavService {
    suspend fun setContext(context: Context)
    suspend fun getRouteCoords(start: GeoPoint, end: GeoPoint): ArrayList<GeoPoint>?
}