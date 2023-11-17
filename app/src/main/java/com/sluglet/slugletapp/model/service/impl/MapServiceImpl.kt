package com.sluglet.slugletapp.model.service.impl

import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.service.MapService
import javax.inject.Inject

class MapServiceImpl @Inject constructor(): MapService {
    override val CourseToDisplay: CourseData = CourseData()
}