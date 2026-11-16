package com.example.assignment_mobile_app.models

import java.util.concurrent.atomic.AtomicLong
import java.io.File
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken

class GolfCourseMemStore(private val file: File) : GolfCourseStore {

    private val courses = ArrayList<GolfCourseModel>()
    private val lastId = AtomicLong(0L)

    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    init {
        load()
    }

    private fun load() {
        if (!file.exists()) return

        val type = object : TypeToken<ArrayList<GolfCourseModel>>() {}.type

        val loaded: ArrayList<GolfCourseModel> =
            gson.fromJson(file.readText(), type) ?: arrayListOf()

        courses.clear()
        courses.addAll(loaded)

        lastId.set(courses.maxOfOrNull { it.id } ?: 0L)
    }

    private fun save() {
        file.parentFile?.mkdirs()
        file.writeText(gson.toJson(courses))
    }

    override fun findAll(): List<GolfCourseModel> {
        return courses
    }

    override fun create(course: GolfCourseModel) {
        course.validate()
        course.id = lastId.incrementAndGet()
        courses.add(course)
        save()
    }

    override fun update(course: GolfCourseModel): Boolean{
        course.validate()
        val foundCourse = findOne(course.id)

        return if (foundCourse != null){
            foundCourse.name = course.name
            foundCourse.location = course.location
            foundCourse.holes = course.holes
            foundCourse.latitude = course.latitude
            foundCourse.longitude = course.longitude
            foundCourse.imageUri = course.imageUri
            save()
            true
        } else {
            false
        }
    }

    override fun delete(id:Long): Boolean {
        val foundCourse = findOne(id)

        return if (foundCourse != null) {
            courses.remove(foundCourse)
            save()
            true
        } else{
            false
        }
    }

    override fun findOne(id: Long): GolfCourseModel? {
        return courses.find { course -> course.id == id}
    }
}