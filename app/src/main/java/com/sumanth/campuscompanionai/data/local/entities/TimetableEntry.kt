package com.sumanth.campuscompanionai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "timetable_entries")
data class TimetableEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val dayOfWeek: String, // e.g., "Monday", "Tuesday"
    val subjectName: String,
    val startTime: String,  // e.g., "09:00 AM"
    val endTime: String,    // e.g., "10:00 AM"
    val roomNumber: String
)
