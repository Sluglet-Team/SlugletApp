package com.sluglet.slugletapp.screens.map

import android.Manifest
import android.provider.Settings
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.res.ResourcesCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.maps.model.LatLng
import com.sluglet.slugletapp.OSMaps.OSMaps
import com.sluglet.slugletapp.R
import com.sluglet.slugletapp.common.composables.CourseMarker
import com.sluglet.slugletapp.resources
import com.sluglet.slugletapp.common.ext.hasLocationPermission
import com.sluglet.slugletapp.common.ext.smallSpacer
import org.osmdroid.util.GeoPoint


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen (
    openScreen: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    // Gets state from the view model for the camera position
    val cameraPositionState = viewModel.cameraState
    // Collect as state with lifecycle means that anytime the state of the flow changes
    // the value will change and cause recomposition
    val userCourses by viewModel.userCourses.collectAsStateWithLifecycle(emptyList())
    val courseToDisplay = viewModel.courseToDisplay.collectAsStateWithLifecycle(null).value
    val viewState by viewModel.viewState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    // Use this modifier to modify the look of the map
    val mapModifier = Modifier
        .padding(start = 10.dp, top = 10.dp, end = 10.dp)
        .clip(RoundedCornerShape(10.dp))
        .shadow(elevation = 10.dp)

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )

    LaunchedEffect(!context.hasLocationPermission()) {
        permissionState.launchMultiplePermissionRequest()
    }

    when {
        permissionState.allPermissionsGranted -> {
            LaunchedEffect(Unit) {
                viewModel.handle(PermissionEvent.Granted)
            }
        }

        permissionState.shouldShowRationale -> {
            RationaleAlert(onDismiss = { }) {
                permissionState.launchMultiplePermissionRequest()
            }
        }

        !permissionState.allPermissionsGranted && !permissionState.shouldShowRationale -> {
            LaunchedEffect(Unit) {
                viewModel.handle(PermissionEvent.Revoked)
            }
        }
    }

    with(viewState) {
        when (this) {
            ViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            ViewState.RevokedPermissions -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Location permissions required")
                    Button(
                        onClick = {
                            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                            startActivity(context, intent, null)
                        },
                        enabled = !context.hasLocationPermission()
                    ) {
                        if (context.hasLocationPermission()) CircularProgressIndicator(
                            modifier = Modifier.size(14.dp),
                            color = Color.White
                        )
                        else Text(
                            text = "Click Here To Go To Settings",
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            is ViewState.Success -> {
                val currentLoc =
                    LatLng(
                        location?.latitude ?: 0.0,
                        location?.longitude ?: 0.0
                    )
                Box {
                    OSMaps(
                        userLocation = currentLoc,
                        cameraPositionState = cameraPositionState.value,
                        modifier = mapModifier
                    ) {
                        // Each course in User Profile
                        userCourses.forEach { course ->
                            CourseMarker(
                                course = course,
                                latitude = course.latitude,
                                longitude = course.longitude,
                                markerIcon = ResourcesCompat.getDrawable(
                                    resources(),
                                    R.drawable.edu_map_pin_red,
                                    null
                                )
                            )
                        }
                        // Course to display is null until map button clicked on search screen
                        if (courseToDisplay != null) {
                            CourseMarker(
                                course = courseToDisplay,
                                latitude = courseToDisplay.latitude,
                                longitude = courseToDisplay.longitude,
                                markerIcon = ResourcesCompat.getDrawable(
                                    resources(),
                                    R.drawable.edu_map_pin_green,
                                    null
                                )
                            )
                        }
                        // User Location
                        CourseMarker(
                            latitude = currentLoc.latitude,
                            longitude = currentLoc.longitude,
                            markerIcon = ResourcesCompat.getDrawable(
                                resources(),
                                R.drawable.user_loc_map_pin,
                                null
                            )
                        )
                    }
                    Icon(
                        painter = painterResource(id = R.drawable.my_loc_button),
                        tint = MaterialTheme.colorScheme.primary,
                        contentDescription = "Animate to User Loc",
                        modifier = Modifier
                            .clickable {
                                viewModel.onMyLocationClick(
                                    GeoPoint(
                                        currentLoc.latitude,
                                        currentLoc.longitude
                                    )
                                )
                            }
                            .align(Alignment.BottomEnd)
                            .smallSpacer()
                            .padding(end = 10.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun RationaleAlert(onDismiss: () -> Unit, onConfirm: () -> Unit) {

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties()
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "We need location permissions to use this app",
                )
                Spacer(modifier = Modifier.height(24.dp))
                TextButton(
                    onClick = {
                        onConfirm()
                        onDismiss()
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text("OK")
                }
            }
        }
    }
}
