package com.example.assignment_mobile_app

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment_mobile_app.models.GolfCourseModel

class GolfCourseAdapter(
    private var courses: List<GolfCourseModel>,
    private val onEdit: (GolfCourseModel) -> Unit,
    private val onDelete: (GolfCourseModel) -> Unit
) : RecyclerView.Adapter<GolfCourseAdapter.CourseViewHolder>(){

    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val nameText: TextView = view.findViewById(R.id.nameText)
        val locationText: TextView = view.findViewById(R.id.locationText)
        val holesText: TextView = view.findViewById(R.id.holesText)

        val editButton: Button = view.findViewById(R.id.editButton)
        val deleteButton: Button = view.findViewById(R.id.deleteButton)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CourseViewHolder {
    val view = LayoutInflater.from(parent.context)
        .inflate(R.layout.item_golf_course,parent,false)

    return GolfCourseAdapter.CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]

        holder.nameText.text = course.name
        holder.locationText.text = course.location
        holder.holesText.text = "${course.holes} Holes"

        holder.editButton.setOnClickListener {
            onEdit(course)
        }

        holder.deleteButton.setOnClickListener {
            onDelete(course)
        }
    }

    override fun getItemCount(): Int {
        return courses.size
    }

    fun updateCourses(newCourses: List<GolfCourseModel>){
        courses = newCourses
        notifyDataSetChanged()
    }
}