package com.sluglet.slugletapp.screens.map

import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.screens.SlugletViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    logService: LogService,

) : SlugletViewModel(logService) {

}