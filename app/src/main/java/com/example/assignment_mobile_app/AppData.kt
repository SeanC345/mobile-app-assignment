package com.example.assignment_mobile_app

import android.content.Context
import com.example.assignment_mobile_app.models.GolfCourseMemStore
import java.io.File

object AppData {

    lateinit var golfCourses: GolfCourseMemStore
        private set

    lateinit var golfCoursesFile: File
        private set

    fun initialize(context: Context) {
        golfCoursesFile = File(
            context.filesDir,
            "golf_courses.json"
        )

        golfCourses = GolfCourseMemStore(golfCoursesFile)
    }
}