package com.sumanth.campuscompanionai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "attendance_records")
data class AttendanceRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val subjectName: String,
    val attendedClasses: Int,
    val totalClasses: Int
)
