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
    override suspend fun setContext(context: Context)
    {

    }
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
                Log.v("findRoute", "IO Exception")
                continuation.resume(ArrayList<GeoPoint>())
            }
            override fun onResponse(call: Call, response: Response)
            {
                Log.v("findRoute", response.toString())
                val routeInfo = (response.body?.string())
                if (routeInfo != null) {
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
                        Log.v("findRoute", "lon: " + longitude.toString() + " lat: " + latitude.toString())
                        coordArray.add(GeoPoint(latitude, longitude))
                        i += 1
                    }
                    Log.v("findRoute", coordArray.toString())
                    Log.v("findRoute", "Nav Complete")
                    continuation.resume(coordArray)
                }
            }
        })
        //continuation.resume(null)
    }
    suspend fun findRoute(start : GeoPoint, end : GeoPoint): ArrayList<GeoPoint>  = suspendCoroutine { continuation ->
        val startCoordString = start.longitude.toString() + "," + start.latitude.toString()
        val endCoordString = end.longitude.toString() + "," + end.latitude.toString()
        val apiCallUrl = NAV_URL + "api_key=" + NAV_KEY + "&start=" + startCoordString + "&end=" + endCoordString
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiCallUrl)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}
            override fun onResponse(call: Call, response: Response)
            {
                val routeInfo = response.body?.string()
                if (routeInfo != null) {
                    var jsonReader = JSONObject(routeInfo)
                    val jsonCoords : JSONArray
                    // features, geometry, coordinates
                    jsonReader = jsonReader.getJSONObject("features")
                    jsonReader = jsonReader.getJSONObject("geometry")
                    jsonCoords = jsonReader.getJSONArray("coordinates")
                    Log.v("findRoute", jsonCoords.toString())

                }
            }
        })
    }
}