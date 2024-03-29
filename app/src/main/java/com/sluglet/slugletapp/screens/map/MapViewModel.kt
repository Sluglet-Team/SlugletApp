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
import com.sluglet.slugletapp.R.string as AppText
import com.sluglet.slugletapp.common.snackbar.SnackbarManager
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
    private var _cameraState: MutableStateFlow<CameraPositionState> = MutableStateFlow(
        CameraPositionState(
            CameraProperty(
                geoPoint = GeoPoint(36.99582810669116, -122.05824150361903),
                zoom = 15.5
            )
        )
    )
    val cameraState = _cameraState.asStateFlow()

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    var courseToDisplay = mapService.courseToDisplay
    val userCourses = storageService.userCourses

    private var _currentPath = mutableStateOf(ArrayList<GeoPoint>())
    var currentPath : State<ArrayList<GeoPoint>> = _currentPath

    var currentLocation : LatLng? = null
    private var pathSetInProgress = false
    /**
     * Triggers when a map marker's navigation button is clicked
     *
     * @param course The CourseData for the clicked marker
     */
    fun onNavClick(course : CourseData)
    {
        if(currentLocation == null) {
            SnackbarManager.showMessage(AppText.location_error)
        }
        else {
            val courseCoord = GeoPoint(course.latitude, course.longitude)
            val myCoord = GeoPoint(currentLocation!!.latitude, currentLocation!!.longitude)
            setPath(myCoord, courseCoord)
        }
    }
    /**
     * Sets the currentPath variable to a path returned by NavService
     *
     * @param start GeoPoint representing the start of the path
     * @param end GeoPoint representing the end of the path
     * @return A bool that indicates whether the function was successful
     */
    private fun setPath(start : GeoPoint, end : GeoPoint)
    {
        Log.v("setPath", "SetPath Called")
        if (!pathSetInProgress)
        {
            pathSetInProgress = true
            launchCatching {
                _currentPath.value = navService.getRouteCoords(start, end)
                if(_currentPath.value.isEmpty()) {
                    SnackbarManager.showMessage(AppText.no_route)                }
                else
                    SnackbarManager.showMessage(AppText.navigation_success)

                Log.v("setPath", currentPath.toString())

                pathSetInProgress = false
            }
        }
    }
    /**
     * Clears the current path
     */
    private fun clearPath()
    {
        _currentPath.value = ArrayList()
    }

    /**
     * This function is responsible for updating the ViewState based
     * on the event coming from the view
     *
     * @param event the Permission event to handle
     */
    fun handle(event: PermissionEvent) {
        when (event) {
            PermissionEvent.Granted -> {
                launchCatching {
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
}