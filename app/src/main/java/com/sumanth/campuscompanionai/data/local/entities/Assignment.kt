package com.sumanth.campuscompanionai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assignments")
data class Assignment(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subject: String,
    val description: String,
    val dueDate: Long, // timestamp
    val isCompleted: Boolean = false
)
