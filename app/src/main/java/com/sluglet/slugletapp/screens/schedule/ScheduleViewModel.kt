package com.sluglet.slugletapp.screens.schedule

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.datetime.*

@HiltViewModel
class ScheduleViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService
) : SlugletViewModel(logService) {
    val userCourses = storageService.userCourses
    val currentDate = Clock.System.now()
    private var _selectedDate: MutableStateFlow<LocalDate?> = MutableStateFlow(null)
    val selectedDate = _selectedDate.asStateFlow()
    // TODO: add quarter start date and end date gathering via service
    val quarterStart = FALL_START
    val quarterEnd = FALL_END

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    companion object {
        private val FALL_START = LocalDate(2023, 9, 28)
        private val FALL_END = LocalDate(2023, 12, 15)
    }

}