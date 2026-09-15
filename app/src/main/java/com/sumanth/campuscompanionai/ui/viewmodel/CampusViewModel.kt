package com.sumanth.campuscompanionai.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.sumanth.campuscompanionai.data.AppPreferences
import com.sumanth.campuscompanionai.data.local.entities.*
import com.sumanth.campuscompanionai.data.repository.CampusRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.Calendar

class CampusViewModel(
    private val repository: CampusRepository,
    private val prefs: AppPreferences
) : ViewModel() {

    // Profile States
    private val _studentName = MutableStateFlow(prefs.getStudentName() ?: "Sumanth")
    val studentName = _studentName.asStateFlow()

    private val _department = MutableStateFlow(prefs.getDepartment() ?: "Computer Science")
    val department = _department.asStateFlow()

    private val _college = MutableStateFlow(prefs.getCollege() ?: "KITSW")
    val college = _college.asStateFlow()

    private val _semester = MutableStateFlow(prefs.getSemester() ?: "3")
    val semester = _semester.asStateFlow()

    fun updateProfile(name: String, dept: String, coll: String, sem: String) {
        viewModelScope.launch {
            prefs.saveStudentName(name)
            prefs.saveDepartment(dept)
            prefs.saveCollege(coll)
            prefs.saveSemester(sem)
            _studentName.value = name
            _department.value = dept
            _college.value = coll
            _semester.value = sem
        }
    }

    // Attendance Tracker
    val attendanceRecords: StateFlow<List<AttendanceRecord>> = repository.allAttendance
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addAttendance(subject: String, attended: Int, total: Int) {
        viewModelScope.launch {
            repository.insertAttendance(AttendanceRecord(subjectName = subject, attendedClasses = attended, totalClasses = total))
        }
    }

    fun deleteAttendance(record: AttendanceRecord) {
        viewModelScope.launch {
            repository.deleteAttendance(record)
        }
    }

    // CGPA Calculator
    val cgpaRecords: StateFlow<List<CGPARecord>> = repository.allCGPA
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addCGPARecord(semester: String, gpa: Float) {
        viewModelScope.launch {
            repository.insertCGPA(CGPARecord(semesterName = semester, gpa = gpa))
        }
    }

    fun deleteCGPA(record: CGPARecord) {
        viewModelScope.launch {
            repository.deleteCGPA(record)
        }
    }

    // Timetable
    val timetableEntries: StateFlow<List<TimetableEntry>> = repository.allTimetable
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addTimetableEntry(day: String, subject: String, start: String, end: String, room: String) {
        viewModelScope.launch {
            repository.insertTimetable(TimetableEntry(dayOfWeek = day, subjectName = subject, startTime = start, endTime = end, roomNumber = room))
        }
    }

    fun deleteTimetableEntry(entry: TimetableEntry) {
        viewModelScope.launch {
            repository.deleteTimetable(entry)
        }
    }

    // Assignments
    val assignments: StateFlow<List<Assignment>> = repository.allAssignments
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addAssignment(subject: String, description: String, dueDate: Long) {
        viewModelScope.launch {
            repository.insertAssignment(Assignment(subject = subject, description = description, dueDate = dueDate))
        }
    }

    fun toggleAssignmentStatus(assignment: Assignment) {
        viewModelScope.launch {
            repository.insertAssignment(assignment.copy(isCompleted = !assignment.isCompleted))
        }
    }

    fun deleteAssignment(assignment: Assignment) {
        viewModelScope.launch {
            repository.deleteAssignment(assignment)
        }
    }

    // Exams
    val exams: StateFlow<List<Exam>> = repository.allExams
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addExam(subject: String, date: Long) {
        viewModelScope.launch {
            repository.insertExam(Exam(subject = subject, examDate = date))
        }
    }

    fun deleteExam(exam: Exam) {
        viewModelScope.launch {
            repository.deleteExam(exam)
        }
    }

    // Notes
    private val _notesSearchQuery = MutableStateFlow("")
    val notesSearchQuery = _notesSearchQuery.asStateFlow()

    val notes: StateFlow<List<Note>> = _notesSearchQuery
        .flatMapLatest { query ->
            if (query.isBlank()) repository.allNotes else repository.searchNotes(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _notesSearchQuery.value = query
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.insertNote(Note(title = title, content = content, timestamp = System.currentTimeMillis()))
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            repository.deleteNote(note)
        }
    }

    // Study Planner & Streaks
    val studyGoals: StateFlow<List<StudyGoal>> = repository.allGoals
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addStudyGoal(title: String, isWeekly: Boolean) {
        viewModelScope.launch {
            val dateStr = Calendar.getInstance().run {
                "${get(Calendar.YEAR)}-${get(Calendar.MONTH) + 1}-${get(Calendar.DAY_OF_MONTH)}"
            }
            repository.insertGoal(StudyGoal(title = title, isWeekly = isWeekly, dateString = dateStr))
        }
    }

    fun toggleGoalStatus(goal: StudyGoal) {
        viewModelScope.launch {
            repository.insertGoal(goal.copy(isCompleted = !goal.isCompleted))
        }
    }

    fun deleteGoal(goal: StudyGoal) {
        viewModelScope.launch {
            repository.deleteGoal(goal)
        }
    }

    // AI Chat Setup (Gemini Prep Architecture)
    private val _chatMessages = MutableStateFlow<List<Pair<String, Boolean>>>(listOf(
        Pair("Hello! I am your AI Campus Assistant. How can I help you manage your studies today? 🤖", false)
    ))
    val chatMessages = _chatMessages.asStateFlow()

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping = _isAiTyping.asStateFlow()

    fun sendAiMessage(message: String) {
        if (message.isBlank()) return
        _chatMessages.value = _chatMessages.value + Pair(message, true)
        
        viewModelScope.launch {
            _isAiTyping.value = true
            delay(1500) // Realistic typing delay
            
            val aiResponse = when {
                message.contains("hi", true) || message.contains("hello", true) -> 
                    "Hello! Don't forget to complete your pending assignments today!"
                message.contains("exam", true) -> 
                    "You have upcoming exams listed in your tracker. Start reviewing your study plan notes!"
                message.contains("attendance", true) -> 
                    "Keep your attendance above 75% to stay safe from condonation thresholds!"
                else -> "I have logged your request. [Future Integration: This query will connect directly to the Gemini API layer here.]"
            }
            _chatMessages.value = _chatMessages.value + Pair(aiResponse, false)
            _isAiTyping.value = false
        }
    }

    // Reset Data Method
    fun resetAllData() {
        viewModelScope.launch {
            // Reset preferences
            prefs.saveStudentName("Sumanth")
            prefs.saveDepartment("Computer Science")
            prefs.saveCollege("KITSW")
            prefs.saveSemester("3")
            _studentName.value = "Sumanth"
            _department.value = "Computer Science"
            _college.value = "KITSW"
            _semester.value = "3"
            
            // Delete room items
            attendanceRecords.value.forEach { repository.deleteAttendance(it) }
            cgpaRecords.value.forEach { repository.deleteCGPA(it) }
            timetableEntries.value.forEach { repository.deleteTimetable(it) }
            assignments.value.forEach { repository.deleteAssignment(it) }
            exams.value.forEach { repository.deleteExam(it) }
            notes.value.forEach { repository.deleteNote(it) }
            studyGoals.value.forEach { repository.deleteGoal(it) }
        }
    }
}

class CampusViewModelFactory(
    private val repository: CampusRepository,
    private val prefs: AppPreferences
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CampusViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CampusViewModel(repository, prefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
