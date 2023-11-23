package com.sluglet.slugletapp.screens.map

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.SavedStateHandleSaveableApi
import androidx.lifecycle.viewmodel.compose.saveable
import com.sluglet.slugletapp.OSMaps.CameraPositionState
import com.sluglet.slugletapp.OSMaps.CameraProperty
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.MapService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import javax.inject.Inject
import javax.inject.Singleton
@HiltViewModel
class MapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
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

    private val _viewState: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)
    val viewState = _viewState.asStateFlow()

    /* This function is responsible for updating the ViewState based
       on the event coming from the view */
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
    fun onMyLocationClick(geoPoint: GeoPoint) {
        _cameraState.value.animateTo(geoPoint)
    }


    // NOTE: removed markerlist because it isn't needed
    // userCourses can be used, and is automatically reflected
    // by changes in firestore so removing a marker would be when
    // a user removes a course.
}