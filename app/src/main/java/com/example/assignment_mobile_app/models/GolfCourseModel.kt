package com.example.assignment_mobile_app.models

data class GolfCourseModel (
    var id: Long = 0L,
    var name: String = "",
    var location: String = "",
    var holes: Int = 18,
    var latitude: Double = 0.0,
    var longitude: Double = 0.0,
    var imageUri: String? = null
) {
    fun validate() {
        require(name.isNotBlank()) { "Course name cannot be blank"}
        require(location.isNotBlank()) { "Location cannot be blank"}
        require(holes == 9 || holes == 18) { "Course must have 9 or 18 holes"}
    }
}