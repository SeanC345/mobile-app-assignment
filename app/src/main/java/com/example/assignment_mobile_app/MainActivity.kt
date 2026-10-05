package com.example.assignment_mobile_app
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val viewCoursesButton =
            findViewById<Button>(R.id.viewCoursesButton)

        val addCourseButton =
            findViewById<Button>(R.id.addCoursebutton)

        viewCoursesButton.setOnClickListener {
            val intent = Intent(this, GolfCourseListActivity::class.java)
            startActivity(intent)
        }

        addCourseButton.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }
    }
}