package com.sluglet.slugletapp.model.service.impl

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Looper
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import com.sluglet.slugletapp.common.ext.hasLocationPermission
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.MapService
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

// Singleton declares that any instance of this class is shared
// Is needed for this class since the value of courseToDiplay is shared between
// composables, the values need to change for all composables.
@Singleton
class MapServiceImpl @Inject constructor(
    private val context: Context,
    private val locationClient: FusedLocationProviderClient
): MapService {
    // TODO: Maybe should hold state for the map here?
    //       That way changes to the state hold when the screen is navigated away and back?
    override val course: MutableStateFlow<CourseData?> = MutableStateFlow(null)
    override val courseToDisplay = course.asStateFlow()
    override val NO_ENTRY = 0.0

    @SuppressLint("MissingPermission")
    @RequiresApi(Build.VERSION_CODES.S)
    override suspend fun requestLocationUpdates(): Flow<LatLng?> = callbackFlow {

        if (!context.hasLocationPermission()) {
            trySend(null)
            return@callbackFlow
        }

        val request = LocationRequest.Builder(10000L)
            .setIntervalMillis(10000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.lastOrNull()?.let {
                    trySend(LatLng(it.latitude, it.longitude))
                }
            }
        }

        locationClient.requestLocationUpdates(
            request,
            locationCallback,
            Looper.getMainLooper()
        )

        awaitClose {
            locationClient.removeLocationUpdates(locationCallback)
        }
    }

    override suspend fun requestCurrentLocation(): Flow<LatLng?> {
        TODO("Not Implemented")
    }
    override suspend fun update(course: CourseData) {
        this.course.value = course
        Log.v("In MapService", "${courseToDisplay.value}")
    }
}