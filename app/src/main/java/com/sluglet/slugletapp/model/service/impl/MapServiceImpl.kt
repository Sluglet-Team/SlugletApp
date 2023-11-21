package com.sluglet.slugletapp.model.service.impl

import android.util.Log
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.MapService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

// Singleton declares that any instance of this class is shared
// Is needed for this class since the value of courseToDiplay is shared between
// composables, the values need to change for all composables.
@Singleton
class MapServiceImpl @Inject constructor(): MapService {
    // TODO: Maybe should hold state for the map here?
    //       That way changes to the state hold when the screen is navigated away and back?
    override val course: MutableStateFlow<CourseData?> = MutableStateFlow(null)
    override val courseToDisplay = course.asStateFlow()
    override val NO_ENTRY = 0.0
    override suspend fun update(course: CourseData) {
        this.course.value = course
        Log.v("In MapService", "${courseToDisplay.value}")
    }
}