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

    fun onReturnClick(openScreen: (String) -> Unit) {
        openScreen(HOME_SCREEN)
    }
}