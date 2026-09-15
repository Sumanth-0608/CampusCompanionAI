package com.sumanth.campuscompanionai.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sumanth.campuscompanionai.data.local.dao.CampusDao
import com.sumanth.campuscompanionai.data.local.entities.*

@Database(
    entities = [
        AttendanceRecord::class,
        CGPARecord::class,
        TimetableEntry::class,
        Assignment::class,
        Exam::class,
        Note::class,
        StudyGoal::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun campusDao(): CampusDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "campus_companion_db"
                )
                .fallbackToDestructiveMigration()
                .build()
                .also { Instance = it }
            }
        }
    }
}
