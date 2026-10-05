package com.example.assignment_mobile_app
import android.widget.Button
import android.widget.LinearLayout
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment_mobile_app.models.GolfCourseModel

class AddEditActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var holesInput: EditText
    private lateinit var formTitle: TextView

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_add_edit)

        nameInput = findViewById(R.id.nameInput)
        locationInput = findViewById(R.id.locationInput)
        holesInput = findViewById(R.id.holesInput)

        val saveButton = findViewById<Button>(R.id.saveButton)
        val cancelButton = findViewById<Button>(R.id.cancelButton)

        saveButton.setOnClickListener {
            saveCourse()
        }

        cancelButton.setOnClickListener {
            finish()
        }

        formTitle = findViewById(R.id.formTitle)

        editingId = intent.getLongExtra("id", -1L)

        if(editingId != -1L){
            formTitle.text = "Edit Golf Course"
            loadExistingCourse(editingId!!)
        }
    }


    private fun loadExistingCourse(id: Long){
        val course = AppData.golfCourses.findOne(id)

        if(course == null) {
            Toast.makeText(
                this,
                "Golf Course not found",
                Toast.LENGTH_SHORT
            ).show()

            finish()
            return
        }

        nameInput.setText(course.name)
        locationInput.setText(course.location)
        holesInput.setText(course.holes.toString())
    }

    private fun saveCourse() {
        val name = nameInput.text.toString().trim()
        val location = locationInput.text.toString().trim()

        if (name.isEmpty()) {
            nameInput.error = "Course name is required"
            return
        }
        val holes = holesInput.text.toString().toIntOrNull()

        if(holes == null){
            holesInput.error = "Enter a valid number"
            return
        }

        if (editingId == null || editingId == -1L) {
            val course = GolfCourseModel(
                name = name,
                location = location,
                holes = holes
            )
            AppData.golfCourses.create(course)

            Toast.makeText(
                this,
                "Golf Course created",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val course = GolfCourseModel(
                id = editingId!!,
                name = name,
                location = location,
                holes = holes
            )
            AppData.golfCourses.update(course)

            Toast.makeText(
                this,
                "Golf Course Updated",
                Toast.LENGTH_SHORT
            ).show()
        }
        finish()
    }
}