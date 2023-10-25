package com.sluglet.slugletapp.model

import com.google.firebase.firestore.DocumentId

/*
This is a data class which is kind of like a struct in C
It will likely be used to store data retrieved from the database
It is used in CourseBox composables to display info about classes
Eventually, these will be stored in a Lazy Column that will represent
...all classes from the UCSC site.
 */
data class CourseData(
    // gets the id of a specific course in the database
    @DocumentId val id: String = "",
    var courseNum: String = "",
    var courseName: String = "",
    var profName: String = "",
    var dateTime: String = "",
    var location: String = ""
)
