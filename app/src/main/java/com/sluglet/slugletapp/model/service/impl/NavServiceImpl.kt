package com.sluglet.slugletapp.model.service.impl

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.sluglet.slugletapp.BuildConfig
import com.sluglet.slugletapp.R
import com.sluglet.slugletapp.common.snackbar.SnackbarManager
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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.encodeToString
import org.json.JSONObject
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Callback
import okhttp3.Call
import okhttp3.Response
import okio.IOException
import org.json.JSONArray

class NavServiceImpl @Inject constructor(): NavService {
    private val NAV_URL = "https://api.openrouteservice.org/v2/directions/foot-walking?"
    private val NAV_KEY = "5b3ce3597851110001cf6248ca6dfdea681b4927b17493a3a45cb427"
    private val DIRECTION_INDEX = 0

    override suspend fun getRouteCoords(start : GeoPoint, end : GeoPoint): ArrayList<GeoPoint>  = suspendCoroutine { continuation ->
        Log.v("NavService", "Starting Nav")
        val startCoordString = start.longitude.toString() + "," + start.latitude.toString()
        val endCoordString = end.longitude.toString() + "," + end.latitude.toString()
        val apiCallUrl = NAV_URL + "api_key=" + NAV_KEY + "&start=" + startCoordString + "&end=" + endCoordString
        val client = OkHttpClient()
        val routeRequest = Request.Builder()
            .url(apiCallUrl)
            .build()

        client.newCall(routeRequest).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                SnackbarManager.showMessage(R.string.routing_connection_error)
                Log.v("NavService", "Failed to connect to routing service")
                continuation.resume(ArrayList<GeoPoint>())
            }
            override fun onResponse(call: Call, response: Response)
            {
                Log.v("NavService", response.toString())
                val routeInfo = (response.body?.string())
                if (routeInfo != null) { // TODO Add a check for no valid route
                    val coordArray = geoJsonToArrayList(routeInfo)
                    Log.v("NavService", "Nav Complete")
                    continuation.resume(coordArray)
                }
            }
        })
        //continuation.resume(null)
    }
    /**
     * Converts API request response into a form usable by the program
     *
     * @param routeInfo The url of the GeoJSON object
     */
    fun geoJsonToArrayList(routeInfo : String) : ArrayList<GeoPoint>{
        var jsonObj = JSONObject(routeInfo)
        var jsonArray = jsonObj.getJSONArray("features")
        jsonObj = jsonArray.getJSONObject(DIRECTION_INDEX)
        jsonObj = jsonObj.getJSONObject("geometry")
        jsonArray = jsonObj.getJSONArray("coordinates")
        Log.v("findRoute", jsonArray.toString())
        val arrayLength = jsonArray.length()
        var i = 0
        val coordArray = ArrayList<GeoPoint>()
        while(i < arrayLength)
        {
            val coords = jsonArray.getString(i).split(',')
            val longitude = (coords[0].trim(']','[')).toDouble()
            val latitude = (coords[1].trim(']','[')).toDouble()
            coordArray.add(GeoPoint(latitude, longitude))
            i += 1
        }
        return coordArray
    }
}

