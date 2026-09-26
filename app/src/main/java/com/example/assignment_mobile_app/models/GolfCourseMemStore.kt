package com.example.assignment_mobile_app.models

import java.util.concurrent.atomic.AtomicLong

class GolfCourseMemStore : GolfCourseStore {

    private val courses = ArrayList<GolfCourseModel>()
    private val lastId = AtomicLong(0L)

    override fun findAll(): List<GolfCourseModel> {
        return courses
    }

    override fun create(course: GolfCourseModel) {
        course.id = lastId.incrementAndGet()
        courses.add(course)
    }

    override fun update(course: GolfCourseModel): Boolean{
        val foundCourse = findOne(course.id)

        return if (foundCourse != null){
            foundCourse.name = course.name
            foundCourse.location = course.location
            foundCourse.holes = course.holes
            true
        } else {
            false
        }
    }

    override fun delete(id:Long): Boolean {
        val foundCourse = findOne(id)

        return if (foundCourse != null) {
            courses.remove(foundCourse)
            true
        } else{
            false
        }
    }

    override fun findOne(id: Long): GolfCourseModel? {
        return courses.find { course -> course.id == id}
    }
}