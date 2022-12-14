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

/**
 * ViewModel for the ProfileScreen.
 * @param application: Application context.
 */
class ProfileScreenViewModel(application: Application): AndroidViewModel(application) {

    private val dir = application.applicationContext.filesDir.absolutePath
    private val database = InitialDB.get(application)
    private val app = application
    private val sharedPreferences: SharedPreferences = application.getSharedPreferences(
        SHARED_PREF_KEY, Context.MODE_PRIVATE)

    var darkmode = MutableLiveData(false)
    var showDeadline = MutableLiveData(true)
    var showConfirmWindow = MutableLiveData(true)

    /**
     * Fetch the amount of closed Task objects from Room db.
     * @return Count of closed Tasks (Int) within a LiveData object.
     */
    fun getClosedTasks() = database.taskDao().getClosedTasks()

    /**
     * Fetch the amount of active Task objects from Room db.
     * @return Count of active Tasks (Int) within a LiveData object.
     */
    fun getActiveTasks() = database.taskDao().getActiveTasks()

    /**
     * Fetch the amount of frogs completed from Room db.
     * @return Count of completed frogs (Int) within a LiveData object.
     */
    fun getFrogsEaten() = database.taskDao().getFrogsEaten()

    /**
     * Fetch the total amount of Task objects from Room db.
     * @return Count of closed Tasks (Int) within a LiveData object.
     */
    fun getTotalTaskCount() = database.taskDao().getTotalTaskCount()


    /**
     * Attempts to convert the given string to a Date object. Returns default current date if an error is thrown.
     * @param string: Date in string format to be converted
     * @return Date object of the parsed String.
     */
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
     * Saves the image uri of the chosen profile picture to the shared preferences.
     * @param imageUri: URI of the location the image is stored within the device.
     */
    private fun saveImageUri(imageUri: String) {
        with (sharedPreferences.edit()) {
            putString(PROFILE_IMAGE_KEY, imageUri)
            apply()
        }
    }

    /**
     * Saves the darkmode status to the SharedPreferences.
     */
    private fun saveDarkModeStatus() {
        with (sharedPreferences.edit()) {
            putBoolean(DARK_MODE_KEY, darkmode.value!!)
            apply()
        }
    }

    /**
     * Saves the showConfirmWindow status to the SharedPreferences.
     */
    private fun saveConfirmWindowStatus() {
        with (sharedPreferences.edit()) {
            putBoolean(CONFIRM_WINDOW_KEY, showConfirmWindow.value!!)
            apply()
        }
    }

    /**
     * Saves the showDeadline status to the SharedPreferences.
     */
    private fun saveShowDeadlineStatus() {
        with (sharedPreferences.edit()) {
            putBoolean(DEADLINE_KEY, showDeadline.value!!)
            apply()
        }
    }

    /**
     * Fetches boolean values from the SharedPreferences according to the given key.
     * @param key: Key/value pair to be fetched.
     * @param default: default to be set if nothing is found.
     * @return boolean value of the given key.
     */
    private fun getBooleanFromPreferences(key: String, default: Boolean): Boolean {
        val prefs = app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, default)
    }

    /**
     * Loads the uri of the current saved profile picture from shared preferences
     * @return URI of the location the image is stored within the device.
     */
    fun loadProfilePicture() : String? {
        val sharedPreferences = app.getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE)
        return sharedPreferences.getString(PROFILE_IMAGE_KEY, null)
    }

    /**
     * Toggle the status of darkmode and save it to SharedPreferences.
     */
    fun toggleDarkMode() {
        darkmode.value = !darkmode.value!!
        saveDarkModeStatus()
    }

    /**
     * Toggle the status of showDeadline and save it to SharedPreferences.
     */
    fun toggleDeadline() {
        showDeadline.value = !showDeadline.value!!
        saveShowDeadlineStatus()
    }

    /**
     * Toggle the status of showConfirmWindow and save it to SharedPreferences.
     */
    fun toggleConfirmWindow() {
        showConfirmWindow.value = !showConfirmWindow.value!!
        saveConfirmWindowStatus()
    }

    /**
     * Saves chosen profile picture to the internal storage for displaying later
     * @param location: URI of the location to be stored to.
     * @return URI of the location the image was stored to.
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
     * Creates Entry-objects of the completed tasks to be displayed on the Chart.
     * @return ChartEntryModelProducer, used for displaying Entry object within the Chart.
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

/**
 * Used for displaying Task entries in the activity Chart. Extends the ChartEntry object.
 *
 * @param date: Date of the TaskEntry, displayed on the x-axis.
 * @param frogCompleted: Completion status, has a frog completed on the given date.
 * @param x: index of the TaskEntry on the x-axis.
 * @param y: index of the TaskEntry on the y-axis.
 */
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

