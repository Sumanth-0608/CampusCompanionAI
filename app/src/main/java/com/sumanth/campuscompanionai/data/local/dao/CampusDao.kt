package com.sumanth.campuscompanionai.data.local.dao

import androidx.room.*
import com.sumanth.campuscompanionai.data.local.entities.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CampusDao {

    // Attendance
    @Query("SELECT * FROM attendance_records")
    fun getAllAttendance(): Flow<List<AttendanceRecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttendance(record: AttendanceRecord)

    @Delete
    suspend fun deleteAttendance(record: AttendanceRecord)

    // CGPA
    @Query("SELECT * FROM cgpa_records")
    fun getAllCGPA(): Flow<List<CGPARecord>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCGPA(record: CGPARecord)

    @Delete
    suspend fun deleteCGPA(record: CGPARecord)

    // Timetable
    @Query("SELECT * FROM timetable_entries WHERE dayOfWeek = :day")
    fun getTimetableForDay(day: String): Flow<List<TimetableEntry>>

    @Query("SELECT * FROM timetable_entries")
    fun getAllTimetable(): Flow<List<TimetableEntry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTimetable(entry: TimetableEntry)

    @Delete
    suspend fun deleteTimetable(entry: TimetableEntry)

    // Assignments
    @Query("SELECT * FROM assignments")
    fun getAllAssignments(): Flow<List<Assignment>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAssignment(assignment: Assignment)

    @Delete
    suspend fun deleteAssignment(assignment: Assignment)

    // Exams
    @Query("SELECT * FROM exams ORDER BY examDate ASC")
    fun getAllExams(): Flow<List<Exam>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExam(exam: Exam)

    @Delete
    suspend fun deleteExam(exam: Exam)

    // Notes
    @Query("SELECT * FROM notes WHERE title LIKE '%' || :searchQuery || '%' OR content LIKE '%' || :searchQuery || '%' ORDER BY timestamp DESC")
    fun searchNotes(searchQuery: String): Flow<List<Note>>

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<Note>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Delete
    suspend fun deleteNote(note: Note)

    // Study Goals
    @Query("SELECT * FROM study_goals")
    fun getAllGoals(): Flow<List<StudyGoal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGoal(goal: StudyGoal)

    @Delete
    suspend fun deleteGoal(goal: StudyGoal)
}
