package com.sluglet.slugletapp.model.service

import kotlinx.coroutines.flow.Flow
import com.sluglet.slugletapp.model.CourseData
import com.sluglet.slugletapp.model.User

interface StorageService {
    // list of courses
    val courses: Flow<List<CourseData>>

    // get specific course
    suspend fun getCourse(courseID: String): CourseData?
    suspend fun storeUserData(user: User)
    suspend fun retrieveUserData(id: String): User?
    /*
    // FIXME: I don't think we need these since user shouldn't be able to manipulate courses
    suspend fun save(course: CourseData): String
    suspend fun update(course: CourseData)
    suspend fun delete(courseId: String)
     */
}
