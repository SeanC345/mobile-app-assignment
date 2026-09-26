package com.example.assignment_mobile_app.models

data class GolfCourseModel (
    var id: Long = 0L,
    var name: String = "",
    var location: String = "",
    var holes: Int = 18
)