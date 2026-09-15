package com.sumanth.campuscompanionai.data

import android.content.Context

class AppPreferences(context: Context) {

    private val prefs =
        context.getSharedPreferences("campus_companion", Context.MODE_PRIVATE)

    fun saveAttendance(attendance: Float) {
        prefs.edit().putFloat("attendance", attendance).apply()
    }

    fun getAttendance(): Float {
        return prefs.getFloat("attendance", 84f)
    }

    fun saveCGPA(cgpa: Float) {
        prefs.edit().putFloat("cgpa", cgpa).apply()
    }

    fun getCGPA(): Float {
        return prefs.getFloat("cgpa", 8.2f)
    }

    fun saveStudentName(name: String) {
        prefs.edit().putString("student_name", name).apply()
    }

    fun getStudentName(): String? {
        return prefs.getString("student_name", "Sumanth")
    }

    fun saveDepartment(dept: String) {
        prefs.edit().putString("department", dept).apply()
    }

    fun getDepartment(): String? {
        return prefs.getString("department", "Computer Science")
    }

    fun saveCollege(college: String) {
        prefs.edit().putString("college", college).apply()
    }

    fun getCollege(): String? {
        return prefs.getString("college", "KITSW")
    }

    fun saveSemester(semester: String) {
        prefs.edit().putString("semester", semester).apply()
    }

    fun getSemester(): String? {
        return prefs.getString("semester", "3")
    }
}
