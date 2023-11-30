package com.sluglet.slugletapp.screens.schedule

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

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
    private val accountService: AccountService
) : SlugletViewModel(logService) {
    // get all courses from Firestore
    val courses = storageService.courses

    var userSearch by mutableStateOf("")

    fun updateSearch (searched: String) {
        userSearch = searched
    }

    // TODO(UNASSIGNED): implement the following functions
    fun onAddClick() {

    }
    fun onDeleteClick(data: CourseData) {
        launchCatching {
            accountService.removeCourse(data)
        }
    }
    fun onMapClick() {

    }

}