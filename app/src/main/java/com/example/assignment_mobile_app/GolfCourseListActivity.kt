package com.example.assignment_mobile_app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Spinner
import android.widget.TextView
import android.widget.AdapterView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_mobile_app.models.GolfCourseModel

class GolfCourseListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: GolfCourseAdapter
    private lateinit var searchInput: EditText
    private lateinit var noResultsText: TextView
    private lateinit var filterSpinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_golf_course_list)

        val rootLayout = findViewById<View>(R.id.courseListLayout)
        val originalTopPadding = rootLayout.paddingTop

        ViewCompat.setOnApplyWindowInsetsListener(rootLayout) {view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(
                view.paddingLeft,
                originalTopPadding + systemBars.top,
                view.paddingRight,
                view.paddingBottom
            )

            insets
        }

        recyclerView = findViewById(R.id.courseRecyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)

        searchInput = findViewById(R.id.searchInput)
        noResultsText = findViewById(R.id.noResultsText)
        filterSpinner = findViewById(R.id.filterSpinner)

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

        filterSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ){
                updateCourses()
            }
            override fun onNothingSelected(parent: AdapterView<*>?){

            }
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
        val searchText = searchInput.text.toString().trim()
        val selectedFilter = filterSpinner.selectedItem.toString()

        val filteredCourses = AppData.golfCourses.findAll().filter { course ->

            val matchesSearch =
            course.name.contains(searchText, ignoreCase = true) ||
                    course.location.contains(searchText, ignoreCase = true)

            val matchesFilter =
                selectedFilter == "All Courses" ||
                        (selectedFilter == "9 holes" && course.holes == 9) ||
                        (selectedFilter == "18 holes" && course.holes == 18)

            matchesSearch && matchesFilter
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