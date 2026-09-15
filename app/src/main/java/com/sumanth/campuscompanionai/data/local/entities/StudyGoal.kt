package com.sumanth.campuscompanionai.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "study_goals")
data class StudyGoal(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val isWeekly: Boolean, // false for daily, true for weekly
    val isCompleted: Boolean = false,
    val dateString: String // e.g. YYYY-MM-DD to track streaks
)
