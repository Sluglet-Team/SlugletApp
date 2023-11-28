package com.sluglet.slugletapp.model.service

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationService {
    fun requestLocationUpdates(): Flow<LatLng?>
    suspend fun requestCurrentLocation(): Flow<LatLng?>
}