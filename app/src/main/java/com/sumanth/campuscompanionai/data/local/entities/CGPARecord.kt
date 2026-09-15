package com.sumanth.campuscompanionai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cgpa_records")
data class CGPARecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val semesterName: String,
    val gpa: Float
)
