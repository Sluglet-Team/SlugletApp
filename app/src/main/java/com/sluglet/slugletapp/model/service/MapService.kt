package com.sluglet.slugletapp.model.service

import androidx.compose.runtime.MutableState
import com.sluglet.slugletapp.model.CourseData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

interface MapService {
    val NO_ENTRY: Double
    val course: MutableStateFlow<CourseData?>
    val courseToDisplay: StateFlow<CourseData?>
    suspend fun update(course: CourseData)
}