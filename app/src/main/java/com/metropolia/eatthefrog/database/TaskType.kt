package com.metropolia.eatthefrog.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * TaskType class, used for storing TaskTypes into Room DB.
 * @param uid: ID of the object, this is autogenerated.
 * @param name: name of the TaskType.
 * @param icon: the ID of the TaskType's icon.
 */
@Entity
data class TaskType(
    @PrimaryKey(autoGenerate = true) val uid: Long = 0,
    @ColumnInfo(name = "type_name") val name: String,
    @ColumnInfo(name = "icon_id") val icon: Int?
)