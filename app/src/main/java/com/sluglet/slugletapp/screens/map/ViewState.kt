package com.sluglet.slugletapp.screens.map

import com.google.android.gms.maps.model.LatLng

sealed interface ViewState{
    object Loading : ViewState
    data class Success(val location: LatLng?) : ViewState
    object RevokedPermissions : ViewState
}