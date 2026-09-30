package com.example.assignment_mobile_app
import android.os.Bundle
import android.content.Intent
import android.view.Gravity
import android.widget.Button
import android.widget.TextView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var listLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()
    }

    override fun onResume() {
        super.onResume()

        if (::listLayout.isInitialized) {
            displayGolfCourses()
        }
    }

    private fun createUserInterface() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
        }
        val title = TextView(this).apply {
            text = "Golf Course Finder"
            textSize = 28F
            gravity = Gravity.CENTER
        }

        val addButton = Button(this).apply {
            text = "Add Golf Course"
            setOnClickListener {
                val intent = Intent(
                    this@MainActivity,
                    AddEditActivity::class.java
                )
                startActivity(intent)
            }
        }

        listLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
        }
        root.addView(title)
        root.addView(addButton)
        root.addView(listLayout)

        setContentView(root)
    }

    private fun displayGolfCourses() {
        listLayout.removeAllViews()

        val courses = AppData.golfCourses.findAll()

        if (courses.isEmpty()) {
            val emptyText = TextView(this).apply {
                text = "No golf courses yet."
                textSize = 18F
                setPadding(0, 40, 0, 40)
            }
            listLayout.addView(emptyText)

            return

        }

        for (course in courses) {
            val courseLayout = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                setPadding(0, 20, 0, 20)
            }
            val courseName = TextView(this).apply {
                text = "${course.id}: ${course.name}"
                textSize = 20f
            }
            val courseLocation = TextView(this).apply {
                text = "Location: ${course.location}"
                textSize = 16F
            }
            val courseHoles = TextView(this).apply {
                text = "Holes: ${course.holes}"
                textSize = 14F
            }
            val editButton = Button(this).apply{
                text = "Edit"
                setOnClickListener {
                    val intent = Intent(
                        this@MainActivity,
                        AddEditActivity::class.java
                    )
                    intent.putExtra("id", course.id)
                    startActivity(intent)
                }
            }
            val deleteButton = Button(this).apply{
                text = "Delete"
                setOnClickListener {
                    AppData.golfCourses.delete(course.id)
                    displayGolfCourses()
                }
            }
            courseLayout.addView(courseName)
            courseLayout.addView(courseLocation)
            courseLayout.addView(courseHoles)
            courseLayout.addView(editButton)
            courseLayout.addView(deleteButton)

            listLayout.addView(courseLayout)
        }
    }
}
