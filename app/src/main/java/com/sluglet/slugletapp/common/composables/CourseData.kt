package com.sluglet.slugletapp.common.composables

/*
This is a data class which is kind of like a struct in C
It will likely be used to store data retrieved from the database
It is used in CourseBox composables to display info about classes
Eventually, these will be stored in a Lazy Column that will represent
...all classes from the UCSC site.
 */
data class CourseData(
    var courseNum: String = "NULL",
    var courseName: String = "NULL",
    var profName: String = "NULL",
    var dateTime: String = "NULL",
    var location: String = "NULL"
)
