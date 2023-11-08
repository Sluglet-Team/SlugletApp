package com.sluglet.slugletapp.screens.search

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService
) : SlugletViewModel(logService) {
    // get all courses from Firestore
    val courses = storageService.courses

    // What the user inputs into search bar
    var userSearch by mutableStateOf("")

    // Updates the UI to reflect user input into search bar
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

}