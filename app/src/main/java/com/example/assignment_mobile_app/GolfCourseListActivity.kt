package com.example.assignment_mobile_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_mobile_app.models.GolfCourseModel

class GolfCourseListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GolfCourseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_golf_course_list)

        recyclerView = findViewById(R.id.courseRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = GolfCourseAdapter(
            AppData.golfCourses.findAll(),
            onEdit = { course -> editCourse(course) },
            onDelete = { course -> deleteCourse(course) }
        )
        recyclerView.adapter = adapter

        val returnButton = findViewById<Button>(R.id.returnButton)
        returnButton.setOnClickListener {
            finish()
        }
    }
    private fun editCourse(course: GolfCourseModel) {
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("id", course.id)
        startActivity(intent)
    }

    private fun deleteCourse(course: GolfCourseModel){
        AppData.golfCourses.delete(course.id)
        updateCourses()
    }

    private fun updateCourses() {
        adapter.updateCourses(AppData.golfCourses.findAll())
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized){
            updateCourses()
        }
    }
}