package com.sluglet.slugletapp.screens.map

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import com.sluglet.slugletapp.model.service.MapService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLocationUseCase @Inject constructor(
    private val mapService: MapService
) {
    @RequiresApi(Build.VERSION_CODES.S)
    operator fun invoke(): Flow<LatLng?> = mapService.requestLocationUpdates()

}