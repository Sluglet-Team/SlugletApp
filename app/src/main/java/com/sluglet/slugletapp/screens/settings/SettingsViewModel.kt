package com.sluglet.slugletapp.screens.settings

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import com.sluglet.slugletapp.HOME_SCREEN
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel

@HiltViewModel
class SettingsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService
) : SlugletViewModel(logService) {
    // get all courses from Firestore
    // update for settings
    val courses = storageService.courses

    var userSearch by mutableStateOf("")

    fun updateSearch (searched: String) {
        userSearch = searched
    }

    // TODO(UNASSIGNED): implement the following functions
    fun onAddClick() {

    }
    fun onDeleteClick() {

    }
    fun onMapClick() {

    }

    fun onReturnClick(openScreen: (String) -> Unit) {
        openScreen(HOME_SCREEN)
    }
}