package com.example.assignment_mobile_app

import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment_mobile_app.models.GolfCourseModel
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.CopyrightOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.config.Configuration

class MapListActivity : AppCompatActivity() {

    private lateinit var mapView: MapView

    private lateinit var courseName: TextView
    private lateinit var courseDetails: TextView
    private lateinit var courseImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_list)

        // Initialise views
        mapView = findViewById(R.id.mapList)
        courseName = findViewById(R.id.mapCourseName)
        courseDetails = findViewById(R.id.mapCourseDetails)
        courseImage = findViewById(R.id.mapCourseImage)

        // Configure OpenStreetMap
        Configuration.getInstance().userAgentValue = "GolfCourseFinder/1.0"
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Show map attribution
        mapView.overlays.add(CopyrightOverlay(this))

        // Back button
        findViewById<Button>(R.id.mapBackButton).setOnClickListener {
            finish()
        }

        // Load golf courses
        loadMarkers()
    }

    private fun loadMarkers() {

        val golfCourses = AppData.golfCourses.findAll()

        // Handle empty list
        if (golfCourses.isEmpty()) {
            courseName.text = "No Golf Courses"
            courseDetails.text = "Add a golf course to display it on the map."
            courseImage.setImageDrawable(null)
            return
        }

        golfCourses.forEach { course ->

            val position = GeoPoint(
                course.latitude,
                course.longitude
            )

            val marker = Marker(mapView)

            marker.position = position
            marker.title = course.name
            marker.snippet = course.location

            marker.setAnchor(
                Marker.ANCHOR_CENTER,
                Marker.ANCHOR_BOTTOM
            )

            marker.setOnMarkerClickListener { _, _ ->

                displayCourse(course)

                true
            }

            mapView.overlays.add(marker)
        }

        // Centre map on first golf course
        golfCourses.firstOrNull()?.let { first ->

            mapView.controller.setZoom(12.0)

            mapView.controller.setCenter(
                GeoPoint(
                    first.latitude,
                    first.longitude
                )
            )
        }

        mapView.invalidate()
    }

    private fun displayCourse(course: GolfCourseModel) {

        courseName.text = course.name

        courseDetails.text =
            "${course.location} • ${course.holes} Holes"

        if (course.imageUri != null) {
            courseImage.setImageURI(
                Uri.parse(course.imageUri)
            )
        } else {
            courseImage.setImageDrawable(null)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        mapView.onDetach()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()

        mapView.tileProvider.clearTileCache()
        mapView.invalidate()
    }
}