package com.example.assignment_mobile_app
import android.widget.Button
import android.widget.LinearLayout
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment_mobile_app.models.GolfCourseModel

class AddEditActivity : AppCompatActivity() {

    private lateinit var nameInput: EditText
    private lateinit var locationInput: EditText
    private lateinit var holesInput: EditText

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        createUserInterface()

        editingId = intent.getLongExtra("id", -1L)

        if(editingId != -1L){
            loadExistingCourse(editingId!!)
        }
    }

    private fun createUserInterface(){
        val root = LinearLayout(this).apply{
            orientation = LinearLayout.VERTICAL
            setPadding(32,32,32,32)
        }
        nameInput = EditText(this).apply{
            hint = "Golf Course Name"
        }

        locationInput = EditText(this).apply{
            hint = "Location"
        }

        holesInput = EditText(this).apply{
            hint = "Number of Holes"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER
        }
        val saveButton = Button(this).apply{
            text = "Save"
            setOnClickListener {
                saveCourse()
            }
        }
        val cancelButton = Button(this).apply{
            text = "Cancel"

            setOnClickListener {
                finish()
            }
        }
        root.addView(nameInput)
        root.addView(locationInput)
        root.addView(holesInput)
        root.addView(saveButton)
        root.addView(cancelButton)

        setContentView(root)
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