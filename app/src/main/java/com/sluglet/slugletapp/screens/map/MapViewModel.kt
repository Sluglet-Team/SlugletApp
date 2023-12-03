package com.sluglet.slugletapp.screens.map

import android.content.Context
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.sluglet.slugletapp.OSMaps.CameraPositionState
import com.sluglet.slugletapp.OSMaps.CameraProperty
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.MapService
import com.sluglet.slugletapp.model.service.NavService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import com.sluglet.slugletapp.screens.sign_up.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.osmdroid.bonuspack.routing.OSRMRoadManager
import org.osmdroid.bonuspack.routing.RoadManager
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val navService: NavService,
    private val mapService: MapService,
    private val getLocationUseCase: GetLocationUseCase,
    private val storageService: StorageService

) : SlugletViewModel(logService) {
    // FIXME(SPRINT 4): Would be ideal to keep the state of the map
    //        so that when navigating away and coming back
    //        it doesn't revert to this starting state
    //        Below is an attempt at doing this but is maybe not implemented right
    private var _cameraState: MutableStateFlow<CameraPositionState> = MutableStateFlow(
        CameraPositionState(
            CameraProperty(
                geoPoint = GeoPoint(36.99582810669116, -122.05824150361903),
                zoom = 15.5
            )
        )
    )
    val cameraState = _cameraState.asStateFlow()
    // FIXME(REMOVE): Previous impl used a more standard state handling mechanism
    //        See previous commits.
    // val cameraState: State<CameraPositionState> = _cameraState

    // Get the course to display from the map service
    // This only has a value if the map button on the search screen is clicked
    var courseToDisplay = mapService.courseToDisplay
    // Get user courses from firestore
    val userCourses = storageService.userCourses

    private var _currentPath = mutableStateOf(ArrayList<GeoPoint>())
    var currentPath : State<ArrayList<GeoPoint>> = _currentPath

    var pathDisplayed : CourseData? = null
    var currentLocation : LatLng? = null
    private var pathSetInProgress = false
    fun onNavClick(course : CourseData) : Boolean
    {
        if(currentLocation == null)
            return false
        if (pathDisplayed != null && pathDisplayed == course ) {
            clearPath()
            return true
        }
        val courseCoord = GeoPoint(course.latitude, course.longitude)
        //val myCoord = GeoPoint(36.99998,-122.06238)
        val myCoord = GeoPoint(currentLocation!!.latitude, currentLocation!!.longitude)
        return setPath(myCoord, courseCoord)
    }

    private fun setPath(start : GeoPoint, end : GeoPoint) : Boolean
    {
        Log.v("setPath", "SetPath Called")
        if (!pathSetInProgress)
        {
            pathSetInProgress = true
            launchCatching {
                _currentPath.value = navService.getRouteCoords(start, end)
                if(_currentPath.value.isEmpty()) {
                    // This should trigger a toast somehow
                }
                Log.v("setPath", currentPath.toString())
                pathSetInProgress = false
            }
            return true
        }
        return false
    }
    private fun clearPath()
    {
        _currentPath.value = ArrayList()
    }

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    /**
     * This function is responsible for updating the ViewState based
     * on the event coming from the view
     *
     * @param event the Permission event to handle
     */
    fun handle(event: PermissionEvent) {
        when (event) {
            PermissionEvent.Granted -> {
                viewModelScope.launch {
                    getLocationUseCase.invoke().collect() { location ->
                        _viewState.value = ViewState.Success(location)
                    }
                }
            }

            PermissionEvent.Revoked -> {
                _viewState.value = ViewState.RevokedPermissions
            }
        }
    }

    /**
     * This function handles clicking of MyLocation Icon on the map
     * On click, it animates to geoPoint
     *
     * @param geoPoint the geoPoint of the user's location
     */
    fun onMyLocationClick(geoPoint: GeoPoint) {
        _cameraState.value.animateTo(geoPoint)
    }


    // NOTE: removed markerlist because it isn't needed
    // userCourses can be used, and is automatically reflected
    // by changes in firestore so removing a marker would be when
    // a user removes a course.
}