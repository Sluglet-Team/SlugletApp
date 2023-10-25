package com.sluglet.slugletapp.screens.search

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService

@HiltViewModel
class SearchViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService
){

}