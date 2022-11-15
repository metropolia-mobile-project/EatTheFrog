package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.constants.PROFILE_IMAGE_KEY
import com.metropolia.eatthefrog.constants.SHARED_PREF_KEY
import com.metropolia.eatthefrog.constants.USERNAME_KEY
import com.metropolia.eatthefrog.database.InitialDB
import org.apache.commons.io.IOUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.*

class ProfileScreenViewModel(application: Application): AndroidViewModel(application) {

    private val dir = application.applicationContext.filesDir.absolutePath
    private val database = InitialDB.get(application)
    private val app = application
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        SHARED_PREF_KEY, Context.MODE_PRIVATE)

    fun getClosedTasks() = database.taskDao().getClosedTasks()
    fun getActiveTasks() = database.taskDao().getActiveTasks()
    fun getFrogsEaten() = database.taskDao().getFrogsEaten()
    fun getTotalTaskCount() = database.taskDao().getTotalTaskCount()

    /**
     * Saves the image uri of the chosen profile picture to the shared preferences
     */

    private fun savePreferences(imageUri: String) {
        with (sharedPreferences.edit()) {
            putString(PROFILE_IMAGE_KEY, imageUri)
            apply()
        }
    }

    /**
     * Loads the uri of the current saved profile picture from shared preferences
     */

    fun loadProfilePicture() : String? {
        val sharedPreferences = app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PROFILE_IMAGE_KEY, null)
    }

    /**
     * Saves chosen profile picture to the internal storage for displaying later
     */

    fun saveFileToInternalStorage(location: Uri?): Uri {
        var imageUri = "".toUri()
        if (location == null) return imageUri

        val entryId = UUID.randomUUID().hashCode() * -1
        val contentResolver = app.applicationContext.contentResolver
        val newFile = File(dir, "$entryId")

        var inputStream: InputStream? = null
        val byteStream = ByteArrayOutputStream()
        var fileOutputStream: FileOutputStream? = null

        try {
            inputStream = contentResolver.openInputStream(location)
            fileOutputStream = FileOutputStream(newFile)
            IOUtils.copy(inputStream, byteStream)
            val bytes = byteStream.toByteArray()

            fileOutputStream.write(bytes)
            imageUri = newFile.toUri()

        } catch (e: Exception) {
            Log.e("IMG_CREATE", "Failed to copy image", e)
            inputStream?.close()
            fileOutputStream?.close()
            byteStream.close()
        } finally {
            inputStream?.close()
            fileOutputStream?.close()
            byteStream.close()
            Log.d("Saved image to Internal storage", "success")
        }
        savePreferences(imageUri.toString())
        return imageUri
    }
}