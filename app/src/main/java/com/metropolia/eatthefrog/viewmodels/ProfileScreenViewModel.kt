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
    private val all = application.getString(R.string.all)

    var darkmode = MutableLiveData(false)
    var showDeadline = MutableLiveData(true)
    var showConfirmWindow = MutableLiveData(true)
    var selectedTypes = MutableLiveData(listOf(all))

    fun getClosedTasks() = database.taskDao().getClosedTasks()
    fun getActiveTasks() = database.taskDao().getActiveTasks()
    fun getFrogsEaten() = database.taskDao().getFrogsEaten()
    fun getTotalTaskCount() = database.taskDao().getTotalTaskCount()
    fun parseStringToDate(string: String) = SimpleDateFormat(DATE_FORMAT).parse(string)

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
        Log.d("DEADLINE PREFS STATUS", getBooleanFromPreferences(DEADLINE_KEY, true).toString())
        Log.d("DEADLINE VALUE", showDeadline.value.toString())
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
        var entries = mutableListOf<Pair<String, Float>>()

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
                        }
                    }
                    else {

                        if (taskAmount > 0f) entries.add(Pair(curDate, taskAmount))
                        curDate = tasks[i].deadline
                        taskAmount = 1f
                    }
                }
            } catch (e: Exception) {
                entries = mutableListOf(Pair(Calendar.getInstance().time.toString(), 0f))
                Log.d("TASK_ENTRY_FAILURE", e.message.toString())
            }
        }
        return entries.mapIndexed { index, (dateString, y) ->
            TaskEntry(
                date = parseStringToDate(dateString) as Date,
                taskCompletedAmount = index,
                x = index.toFloat(),
                y = y,
            )
        }.let { entryCollection -> ChartEntryModelProducer(entryCollection) }
    }



    /**
     * Creates Entry-objects of the completed frogs.
     */
    fun getFrogsCompletedEntries(): ChartEntryModelProducer {
        var entries = mutableListOf<Pair<String, Int>>()

        runBlocking {
            try {
                var tasks = database.taskDao().getAllCompletedTasksOrderedByDate()

                for (i in tasks.indices) {
                    val task = tasks[i]

                    if (task.isFrog) {
                        entries.add(Pair(task.deadline, 1))
                        Log.d("LOOPING, FROG FOUND", "task: ${tasks[i].name}")
                    }

                }
            } catch (e: Exception) {
                entries = mutableListOf(Pair(Calendar.getInstance().time.toString(), 1))
                Log.d("FROG_ENTRY_FAILURE", e.message.toString())
            }
        }

        return entries.mapIndexed { index, (dateString, isFrog) ->
            TaskEntry(
                date = parseStringToDate(dateString) as Date,
                taskCompletedAmount = 1,
                x = index.toFloat(),
                y = isFrog.toFloat(),
            )
        }.let { entryCollection -> ChartEntryModelProducer(entryCollection) }
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
        taskCompletedAmount = this.taskCompletedAmount,
        x = this.x,
        y = y,
    )
}
/*

class FrogEntry(
    val date: Date,

    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = FrogEntry(
        date = this.date,
        isFrog = this.isFrog,
        x = this.x,
        y = y,
    )
}
*/



