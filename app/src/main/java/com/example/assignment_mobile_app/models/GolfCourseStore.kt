package com.example.assignment_mobile_app.models

interface GolfCourseStore {
    fun findAll(): List<GolfCourseModel>
    fun create(course: GolfCourseModel)
    fun update(course: GolfCourseModel): Boolean
    fun delete(id: Long): Boolean
    fun findOne(id:Long): GolfCourseModel?
}