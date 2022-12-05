package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.constants.*
import com.metropolia.eatthefrog.database.InitialDB
import com.patrykandpatryk.vico.core.entry.ChartEntry
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import kotlinx.coroutines.runBlocking
import org.apache.commons.io.IOUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class ProfileScreenViewModel(application: Application): AndroidViewModel(application) {

    private val dir = application.applicationContext.filesDir.absolutePath
    private val database = InitialDB.get(application)
    private val app = application
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        SHARED_PREF_KEY, Context.MODE_PRIVATE)

    var darkmode = MutableLiveData(false)
    var showDeadline = MutableLiveData(true)
    var showConfirmWindow = MutableLiveData(true)

    fun getClosedTasks() = database.taskDao().getClosedTasks()
    fun getActiveTasks() = database.taskDao().getActiveTasks()
    fun getFrogsEaten() = database.taskDao().getFrogsEaten()
    fun getTotalTaskCount() = database.taskDao().getTotalTaskCount()

    private fun parseStringToDate(string: String): Date {
        var d = Date()
        try {
            d = SimpleDateFormat(DATE_FORMAT).parse(string)
        } catch (e: Exception) {
            Log.d("Failed to parse date", e.message.toString())
        }
        return d
    }

    init {
        darkmode.value = getBooleanFromPreferences(DARK_MODE_KEY, false)
        showDeadline.value = getBooleanFromPreferences(DEADLINE_KEY, true)
        showConfirmWindow.value = getBooleanFromPreferences(CONFIRM_WINDOW_KEY, true)
    }

    /**
     * Saves the image uri of the chosen profile picture to the shared preferences
     */
    private fun saveImageUri(imageUri: String) {
        with (sharedPreferences.edit()) {
            putString(PROFILE_IMAGE_KEY, imageUri)
            apply()
        }
    }

    private fun saveDarkModeStatus() {
        with (sharedPreferences.edit()) {
            putBoolean(DARK_MODE_KEY, darkmode.value!!)
            apply()
        }
    }

    private fun saveConfirmWindowStatus() {
        with (sharedPreferences.edit()) {
            putBoolean(CONFIRM_WINDOW_KEY, showConfirmWindow.value!!)
            apply()
        }
    }

    private fun saveShowDeadlineStatus() {
        with (sharedPreferences.edit()) {
            putBoolean(DEADLINE_KEY, showDeadline.value!!)
            apply()
        }
    }

    private fun getBooleanFromPreferences(key: String, default: Boolean): Boolean {
        val prefs = app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, default)
    }

    /**
     * Loads the uri of the current saved profile picture from shared preferences
     */
    fun loadProfilePicture() : String? {
        val sharedPreferences = app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PROFILE_IMAGE_KEY, null)
    }

    fun toggleDarkMode() {
        darkmode.value = !darkmode.value!!
        saveDarkModeStatus()
    }
    fun toggleDeadline() {
        showDeadline.value = !showDeadline.value!!
        saveShowDeadlineStatus()
    }
    fun toggleConfirmWindow() {
        showConfirmWindow.value = !showConfirmWindow.value!!
        saveConfirmWindowStatus()
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
        saveImageUri(imageUri.toString())
        return imageUri
    }


    /**
     * Creates Entry-objects of the completed tasks.
     */
    fun getTasksCompletedEntries(): ChartEntryModelProducer {

        var entries = mutableListOf<TaskEntry>()

        runBlocking {
            try {
                var tasks = database.taskDao().getAllCompletedTasksOrderedByDate()
                tasks = tasks.sortedByDescending { parseStringToDate(it.deadline) }

                for (i in tasks.indices) {
                    var task = tasks[i]

                    val entry = entries.find { it.date == parseStringToDate(task.deadline)}
                    if (entry != null) {
                        entries.forEach {
                            if (it.date == parseStringToDate(task.deadline)) {
                                it.y++
                                if (!it.frogCompleted) it.frogCompleted = task.isFrog
                            }
                        }
                        Log.d("TASK_ENTRY", "task entry modified with deadline ${task.deadline}")
                    } else {
                        entries.add(TaskEntry(parseStringToDate(task.deadline), task.isFrog, entries.size.toFloat(), 1f))
                        Log.d("TASK_ENTRY", "new entry added with deadline ${task.deadline}")
                    }
                }
            } catch (e: Exception) {
                entries = mutableListOf<TaskEntry>()
                Log.d("TASK_ENTRY_FAILURE", e.message.toString())
            }
        }

        // Must be at least 2 items to display columns for some reason
        if (entries.size == 1) {
            entries.add(TaskEntry(Date(), false, entries.size.toFloat(), 0f))
        }

        return ChartEntryModelProducer(entries)
    }
}

class TaskEntry(
    val date: Date,
    var frogCompleted: Boolean,
    override val x: Float,
    override var y: Float,
) : ChartEntry {
    override fun withY(y: Float) = TaskEntry(
        date = this.date,
        frogCompleted = this.frogCompleted,
        x = this.x,
        y = y,
    )
}

