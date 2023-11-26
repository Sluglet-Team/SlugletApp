package com.sluglet.slugletapp.screens.map

import com.google.android.gms.maps.model.LatLng
import com.sluglet.slugletapp.model.service.LocationService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val locationService: LocationService
) {
    operator fun invoke(): Flow<LatLng?> = locationService.requestLocationUpdates()
}