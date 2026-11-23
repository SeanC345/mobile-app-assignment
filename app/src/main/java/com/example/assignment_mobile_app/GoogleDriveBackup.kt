package com.example.assignment_mobile_app
import com.google.api.client.http.HttpRequestInitializer
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.drive.Drive
import android.widget.Toast
import com.google.api.client.http.ByteArrayContent
import android.content.Context
import android.app.Activity
import java.io.File

class GoogleDriveBackup(private val context: Context) {

    fun backup(file: File, accessToken: String) {

        val requestInitializer = HttpRequestInitializer { request ->
            request.headers.authorization = "Bearer $accessToken"
        }

        val driveService = Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            requestInitializer
        )
            .setApplicationName("Golf Course Finder")
            .build()

        val fileMetadata = com.google.api.services.drive.model.File()
        fileMetadata.name = "golf_courses.json"

        val fileContent = ByteArrayContent(
            "application/json",
            file.readBytes()
        )

        Thread {
            try {
                driveService.files()
                    .create(fileMetadata, fileContent)
                    .execute()

                (context as Activity).runOnUiThread {
                    Toast.makeText(
                        context,
                        "Backup successful",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } catch (e: Exception) {
                (context as Activity).runOnUiThread {
                    Toast.makeText(
                        context,
                        "Backup failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
    }

    fun restore(file: File, accessToken: String) {

        val requestInitializer = HttpRequestInitializer { request ->
            request.headers.authorization = "Bearer $accessToken"
        }

        val driveService = Drive.Builder(
            NetHttpTransport(),
            GsonFactory.getDefaultInstance(),
            requestInitializer
        )
            .setApplicationName("Golf Course Finder")
            .build()

        Thread {
            try {
                val result = driveService.files().list()
                    .setQ("name = 'golf_courses.json' and trashed = false")
                    .setFields("files(id, name)")
                    .execute()

                val driveFile = result.files.firstOrNull()

                if (driveFile != null) {
                    file.outputStream().use { outputStream ->
                        driveService.files()
                            .get(driveFile.id)
                            .executeMediaAndDownloadTo(outputStream)
                    }

                    AppData.golfCourses.load()

                    (context as Activity).runOnUiThread {
                        Toast.makeText(
                            context,
                            "Restore successful",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    (context as Activity).runOnUiThread {
                        Toast.makeText(
                            context,
                            "No backup found",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } catch (e: Exception) {
                (context as Activity).runOnUiThread {
                    Toast.makeText(
                        context,
                        "Restore failed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
    }
}