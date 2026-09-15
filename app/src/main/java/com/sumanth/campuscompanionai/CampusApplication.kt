package com.sumanth.campuscompanionai

import android.app.Application
import com.sumanth.campuscompanionai.data.AppPreferences
import com.sumanth.campuscompanionai.data.local.AppDatabase
import com.sumanth.campuscompanionai.data.repository.CampusRepository

class CampusApplication : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
    val repository by lazy { CampusRepository(database.campusDao()) }
    val prefs by lazy { AppPreferences(this) }
}
