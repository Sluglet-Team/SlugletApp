package com.sluglet.slugletapp.screens.search

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.AccountService
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import android.util.Log
import com.sluglet.slugletapp.MAP_SCREEN
import com.sluglet.slugletapp.model.service.MapService

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    mapService: MapService,
    private val storageService: StorageService,
    private val accountService: AccountService
) : SlugletViewModel(logService) {
    // get all courses from Firestore
    val courses = storageService.courses
    // What the user inputs into search bar
    var userSearch by mutableStateOf("")

    var courseToDisplay = mapService.CourseToDisplay

    // Updates the UI to reflect user input into search bar
    fun updateSearch (searched: String) {
        userSearch = searched
    }

    // TODO(UNASSIGNED): implement the following functions
    fun onAddClick(data: CourseData) {
        launchCatching {
            accountService.addCourse(data)
        }
    }
    fun onDeleteClick() {

    }
    fun onMapClick(openScreen: (String) -> Unit, data: CourseData) {
        courseToDisplay = data
        openScreen(MAP_SCREEN)
    }

}