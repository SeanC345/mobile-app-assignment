package com.example.assignment_mobile_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_mobile_app.models.GolfCourseModel

class GolfCourseListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GolfCourseAdapter
    private lateinit var searchInput: EditText
    private lateinit var noResultsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_golf_course_list)

        recyclerView = findViewById(R.id.courseRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        searchInput = findViewById(R.id.searchInput)
        noResultsText = findViewById(R.id.noResultsText)

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

        searchInput.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                updateCourses()
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
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
        val searchText = searchInput.text.toString().trim()

        val filteredCourses = AppData.golfCourses.findAll().filter { course ->
            course.name.contains(searchText, ignoreCase = true) ||
                    course.location.contains(searchText, ignoreCase = true)
        }

        adapter.updateCourses(filteredCourses)

        if (filteredCourses.isEmpty()) {
            noResultsText.visibility = View.VISIBLE
        } else {
            noResultsText.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()

        if (::adapter.isInitialized){
            updateCourses()
        }
    }
}