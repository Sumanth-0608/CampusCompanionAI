package com.sumanth.campuscompanionai.data.repository

import com.sumanth.campuscompanionai.data.local.dao.CampusDao
import com.sumanth.campuscompanionai.data.local.entities.*
import kotlinx.coroutines.flow.Flow

class CampusRepository(private val campusDao: CampusDao) {

    // Attendance
    val allAttendance: Flow<List<AttendanceRecord>> = campusDao.getAllAttendance()
    suspend fun insertAttendance(record: AttendanceRecord) = campusDao.insertAttendance(record)
    suspend fun deleteAttendance(record: AttendanceRecord) = campusDao.deleteAttendance(record)

    // CGPA
    val allCGPA: Flow<List<CGPARecord>> = campusDao.getAllCGPA()
    suspend fun insertCGPA(record: CGPARecord) = campusDao.insertCGPA(record)
    suspend fun deleteCGPA(record: CGPARecord) = campusDao.deleteCGPA(record)

    // Timetable
    val allTimetable: Flow<List<TimetableEntry>> = campusDao.getAllTimetable()
    fun getTimetableForDay(day: String): Flow<List<TimetableEntry>> = campusDao.getTimetableForDay(day)
    suspend fun insertTimetable(entry: TimetableEntry) = campusDao.insertTimetable(entry)
    suspend fun deleteTimetable(entry: TimetableEntry) = campusDao.deleteTimetable(entry)

    // Assignments
    val allAssignments: Flow<List<Assignment>> = campusDao.getAllAssignments()
    suspend fun insertAssignment(assignment: Assignment) = campusDao.insertAssignment(assignment)
    suspend fun deleteAssignment(assignment: Assignment) = campusDao.deleteAssignment(assignment)

    // Exams
    val allExams: Flow<List<Exam>> = campusDao.getAllExams()
    suspend fun insertExam(exam: Exam) = campusDao.insertExam(exam)
    suspend fun deleteExam(exam: Exam) = campusDao.deleteExam(exam)

    // Notes
    val allNotes: Flow<List<Note>> = campusDao.getAllNotes()
    fun searchNotes(query: String): Flow<List<Note>> = campusDao.searchNotes(query)
    suspend fun insertNote(note: Note) = campusDao.insertNote(note)
    suspend fun deleteNote(note: Note) = campusDao.deleteNote(note)

    // Study Goals
    val allGoals: Flow<List<StudyGoal>> = campusDao.getAllGoals()
    suspend fun insertGoal(goal: StudyGoal) = campusDao.insertGoal(goal)
    suspend fun deleteGoal(goal: StudyGoal) = campusDao.deleteGoal(goal)
}
