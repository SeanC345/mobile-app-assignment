package com.example.assignment_mobile_app

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker

class MapPickerActivity : AppCompatActivity() {

    private lateinit var map: MapView
    private var selectedPoint: GeoPoint? = null
    private var marker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Configuration.getInstance().userAgentValue = "GolfCourseFinder/1.0"

        setContentView(R.layout.activity_map_picker)

        val viewOnly = intent.getBooleanExtra("viewOnly", false)
        val confirmButton = findViewById<Button>(R.id.confirmLocationButton)


        map = findViewById(R.id.map)
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)

        val lat = intent.getDoubleExtra("latitude", 52.245954)
        val lon = intent.getDoubleExtra("longitude", -7.139539)
        val start = GeoPoint(lat, lon)

        map.controller.setZoom(18.0)
        map.controller.setCenter(start)

        setSelectedPoint(start)
        showExistingMarkers()

        if (!viewOnly) {
            val receiver = object : MapEventsReceiver {

                override fun singleTapConfirmedHelper(point: GeoPoint): Boolean {
                    setSelectedPoint(point)
                    return true
                }

                override fun longPressHelper(point: GeoPoint): Boolean {
                    setSelectedPoint(point)
                    return true
                }
            }

            map.overlays.add(MapEventsOverlay(receiver))
        }
        if (viewOnly) {
            confirmButton.text = "Return"

            confirmButton.setOnClickListener {
                finish()
            }
        } else {
            confirmButton.text = "Use This Location"

            confirmButton.setOnClickListener {
                val point = selectedPoint ?: return@setOnClickListener

                val result = intent
                    .putExtra("latitude", point.latitude)
                    .putExtra("longitude", point.longitude)

                setResult(Activity.RESULT_OK, result)
                finish()
            }
        }
    }

    private fun setSelectedPoint(point: GeoPoint) {
        selectedPoint = point

        if (marker == null) {
            marker = Marker(map).apply {
                setAnchor(
                    Marker.ANCHOR_CENTER,
                    Marker.ANCHOR_BOTTOM
                )

                icon = getDrawable(android.R.drawable.star_big_on)
            }

            map.overlays.add(marker)
        }

        marker?.position = point

        val markerTitle = intent.getStringExtra("title")

        marker?.title = if (markerTitle.isNullOrBlank()) {
            "Selected Golf Course"
        } else {
            markerTitle
        }

        map.invalidate()
    }

    private fun showExistingMarkers() {
        val golfCourses = AppData.golfCourses.findAll()

        golfCourses.forEach { course ->
            val point = GeoPoint(
                course.latitude,
                course.longitude
            )

            val courseMarker = Marker(map).apply {
                position = point
                title = course.name
                snippet = course.location

                setAnchor(
                    Marker.ANCHOR_CENTER,
                    Marker.ANCHOR_BOTTOM
                )
            }

            map.overlays.add(courseMarker)
        }

        map.invalidate()
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        map.onPause()
        super.onPause()
    }
}