package com.example.assignment_mobile_app.main

import com.example.assignment_mobile_app.models.GolfCourseMemStore
import com.example.assignment_mobile_app.models.GolfCourseModel

val store = GolfCourseMemStore()

fun main() {
    println("=== Golf Course Finder ===")

    var input: Int

    do {
        input = menu()

        when (input) {
            1 -> addGolfCourse()
            2 -> listGolfCourses()
            3 -> updateGolfCourse()
            4 -> deleteGolfCourse()
            5 -> searchGolfCourse()
            0 -> println("\nExiting Golf Course Finder. Goodbye!")
            else -> println("\nInvalid option. Please try again.")
        }
    } while (input != 0)
}

fun menu(): Int {
    println("\n------------------")
    println(" GOLF COURSE FINDER")
    println("-------------------")
    println("1. Add Golf Course")
    println("2. List all golf courses")
    println("3. Update a golf course")
    println("4. Delete a golf course")
    println("5. Search golf course by ID")
    println("0. Exit")
    print("\nEnter option: ")

    return readlnOrNull()?.toIntOrNull() ?: -1
}

fun addGolfCourse() {
    println("\n--- Add Golf Course ---")

    print("Enter Course Name: ")
    val name = readlnOrNull()?.trim().orEmpty()

    print("Enter Location: ")
    val location = readlnOrNull()?.trim().orEmpty()

    print("Enter Number of holes: ")
    val holes = readlnOrNull()?.toIntOrNull() ?: 18

    if (name.isNotEmpty()) {
        val course = GolfCourseModel(
            name = name,
            location = location,
            holes = holes
        )

        store.create(course)

        println("Golf Course added successfully with ID: ${course.id}")
    } else {
        println("Course name cannot be empty. Creation cancelled.")
    }
}