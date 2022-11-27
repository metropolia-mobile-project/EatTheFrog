package com.metropolia.eatthefrog.viewmodels

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.metropolia.eatthefrog.R
import com.metropolia.eatthefrog.constants.*
import com.metropolia.eatthefrog.database.InitialDB
import com.patrykandpatryk.vico.core.entry.ChartEntry
import com.patrykandpatryk.vico.core.entry.ChartEntryModel
import com.patrykandpatryk.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.composed.ComposedChartEntryModelProducer
import com.patrykandpatryk.vico.core.entry.composed.plus
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
    private val all = application.getString(R.string.all)

    var darkmode = MutableLiveData(false)
    var showDeadline = MutableLiveData(true)
    var showConfirmWindow = MutableLiveData(true)
    var selectedTypes = MutableLiveData(listOf(all))

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
    private fun dateToString(date: Date): String  {
        var d = ""
        try {
            d = SimpleDateFormat(DATE_FORMAT).format(date)
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
     * Toggles the selected graph filter values
     */
    fun toggleSelectedType(type: String) {
        var newList : MutableList<String> = selectedTypes.value?.toMutableList() ?: mutableListOf()

        if (type == all) {
            selectedTypes.value = listOf(all)
            return
        } else {
            if (selectedTypes.value?.contains(type) == true) {
                newList.remove(type)
            } else {
                newList.add(type)
            }
        }

        if (newList.isEmpty()) newList.add(all)
        else newList.remove(all)

        selectedTypes.value = newList.toList()
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
        var entries = mutableListOf(Pair(dateToString(Date()), 0f))

        runBlocking {
            try {
                var tasks = database.taskDao().getAllCompletedTasksOrderedByDate()
                var curDate = tasks[0].deadline
                var taskAmount = 0f

                for (i in tasks.indices) {

                    if (tasks[i].deadline == curDate) {
                        taskAmount++
                        if (i == tasks.size -1) {
                            entries.add(Pair(curDate, taskAmount))
                            Log.d("TASK_ENTRY_SUCCESS", "$curDate, $taskAmount")
                        }
                    }
                    else {

                        if (taskAmount > 0f) {
                            entries.add(Pair(curDate, taskAmount))
                            Log.d("TASK_ENTRY_SUCCESS", "$curDate, $taskAmount")
                        }
                        curDate = tasks[i].deadline
                        taskAmount = 1f
                    }
                }
            } catch (e: Exception) {
                entries = mutableListOf(Pair(Calendar.getInstance().time.toString(), 0f))
                Log.d("TASK_ENTRY_FAILURE", e.message.toString())
            }
        }

        var e = entries.mapIndexed { index, (dateString, y) ->
            TaskEntry(
                date = parseStringToDate(dateString),
                taskCompletedAmount = y.toInt(),
                x = index.toFloat(),
                y = y,
            )
        }

        return ChartEntryModelProducer(e)
    }



    /**
     * Creates Entry-objects of the completed frogs.
     */
    fun getFrogsCompletedEntries(): ChartEntryModelProducer {
        var entries = mutableListOf(Pair(dateToString(Date()), 0))

        runBlocking {
            try {

                var tasks = database.taskDao().getAllCompletedTasksOrderedByDate()
                var curDate = tasks[0].deadline
                var curDateHandled = false

                for (i in tasks.indices) {

                    if (tasks[i].deadline == curDate) {

                        if (tasks[i].isFrog && !curDateHandled) {
                            entries.add(Pair(curDate, 1))
                            curDateHandled = true
                            Log.d("FROG_ENTRY_SUCCESS", "$curDate, 1")

                        } else if (i == tasks.size -1 && !curDateHandled) {
                            entries.add(Pair(curDate, 0))
                            curDateHandled = true
                            Log.d("FROG_ENTRY_SUCCESS", "$curDate, 1")
                        }
                    }
                    else {
                        entries.add(Pair(curDate, 0))
                        curDate = tasks[i].deadline
                        curDateHandled = false
                        Log.d("FROG_ENTRY_SUCCESS", "$curDate, 1")
                    }
                }

            } catch (e: Exception) {
                entries = mutableListOf(Pair(Calendar.getInstance().time.toString(), 0))
                Log.d("FROG_ENTRY_FAILURE", e.message.toString())
            }
        }

        var e = entries.mapIndexed { index, (dateString, isFrog) ->
            TaskEntry(
                date = parseStringToDate(dateString),
                taskCompletedAmount = isFrog,
                x = index.toFloat(),
                y = isFrog.toFloat(),
            )
        }
        return ChartEntryModelProducer(e)
    }
}

class TaskEntry(
    val date: Date,
    val taskCompletedAmount: Int,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = TaskEntry(
        date = this.date,
        taskCompletedAmount = y.toInt(),
        x = this.x,
        y = y,
    )
}

