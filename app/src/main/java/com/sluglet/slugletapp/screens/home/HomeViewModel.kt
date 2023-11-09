package com.sluglet.slugletapp.screens.home

import androidx.compose.runtime.*
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.sluglet.slugletapp.model.CourseData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.sluglet.slugletapp.model.service.LogService
import com.sluglet.slugletapp.model.service.StorageService
import com.sluglet.slugletapp.screens.SlugletViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    logService: LogService,
    private val storageService: StorageService,
): SlugletViewModel(logService) {
    val courses = MutableStateFlow<List<CourseData>>(emptyList())
    fun onDeleteClick(){

    }
}