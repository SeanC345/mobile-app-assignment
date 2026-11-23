package com.example.assignment_mobile_app
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.identity.AuthorizationClient
import com.google.android.gms.auth.api.identity.AuthorizationRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Scope
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : AppCompatActivity() {
    private val driveAuthorizationLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartIntentSenderForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                Toast.makeText(
                    this,
                    "Google Drive access granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Google Drive access cancelled",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        val viewCoursesButton =
            findViewById<Button>(R.id.viewCoursesButton)

        val addCourseButton =
            findViewById<Button>(R.id.addCoursebutton)

        val viewMapButton =
            findViewById<Button>(R.id.viewMapButton)

        val backupButton =
            findViewById<Button>(R.id.backupButton)

        viewCoursesButton.setOnClickListener {
            val intent = Intent(this, GolfCourseListActivity::class.java)
            startActivity(intent)
        }

        addCourseButton.setOnClickListener {
            val intent = Intent(this, AddEditActivity::class.java)
            startActivity(intent)
        }

        viewMapButton.setOnClickListener {
            val intent = Intent(this, MapListActivity::class.java)
            startActivity(intent)
        }

        backupButton.setOnClickListener {
            requestDriveAccess()
        }

        AppData.initialize(this)
    }

    private fun requestDriveAccess() {
        val authorizationClient: AuthorizationClient =
            Identity.getAuthorizationClient(this)

        val driveScope =
            Scope("https://www.googleapis.com/auth/drive.file")

        val request = AuthorizationRequest.builder()
            .setRequestedScopes(listOf(driveScope))
            .build()

        authorizationClient.authorize(request)
            .addOnSuccessListener { result ->
                if (result.hasResolution()) {
                    val intentSenderRequest =
                        IntentSenderRequest.Builder(
                            result.pendingIntent!!.intentSender
                        ).build()

                    driveAuthorizationLauncher.launch(intentSenderRequest)
                } else {
                    Toast.makeText(
                        this,
                        "Google Drive access granted",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(
                    this,
                    "Google Drive access failed",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
}