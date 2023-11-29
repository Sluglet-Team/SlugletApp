package com.sluglet.slugletapp.model.service.impl

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.sluglet.slugletapp.BuildConfig
import com.sluglet.slugletapp.model.service.MapService
import com.sluglet.slugletapp.model.service.NavService
import dagger.Provides
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.Road
import org.osmdroid.bonuspack.routing.RoadManager
import org.osmdroid.util.GeoPoint
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class NavServiceImpl @Inject constructor(): NavService {
    val roadManager = OSRMRoadManager(Application(), BuildConfig.APPLICATION_ID)
    override suspend fun setContext(context: Context)
    {

    }
    override suspend fun getRoadCoords(points: ArrayList<GeoPoint>, context: Context): ArrayList<GeoPoint>  = suspendCoroutine { continuation ->
        val roadManager = OSRMRoadManager(Application(), BuildConfig.APPLICATION_ID)
        roadManager.setMean(OSRMRoadManager.MEAN_BY_FOOT)
        Log.v("NavService", "Starting Nav")
        val road : Road = roadManager.getRoad(points)
        Log.v("NavService", "Road Found")
        continuation.resume(road.mRouteHigh)
    }
}