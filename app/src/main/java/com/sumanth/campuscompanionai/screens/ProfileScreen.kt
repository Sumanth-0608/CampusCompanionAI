package com.sumanth.campuscompanionai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumanth.campuscompanionai.ui.viewmodel.CampusViewModel
import java.util.Locale

@Composable
fun ProfileScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    val name by viewModel.studentName.collectAsState()
    val dept by viewModel.department.collectAsState()
    val coll by viewModel.college.collectAsState()
    val sem by viewModel.semester.collectAsState()

    val attendanceRecords by viewModel.attendanceRecords.collectAsState()
    val cgpaRecords by viewModel.cgpaRecords.collectAsState()
    val assignments by viewModel.assignments.collectAsState()

    var editMode by remember { mutableStateOf(false) }

    var nameField by remember(name) { mutableStateOf(name) }
    var deptField by remember(dept) { mutableStateOf(dept) }
    var collField by remember(coll) { mutableStateOf(coll) }
    var semField by remember(sem) { mutableStateOf(sem) }

    val calculatedCgpa = remember(cgpaRecords) {
        if (cgpaRecords.isEmpty()) 0f else cgpaRecords.map { it.gpa }.sum() / cgpaRecords.size
    }
    val overallAttendance = remember(attendanceRecords) {
        val total = attendanceRecords.sumOf { it.totalClasses }
        val attended = attendanceRecords.sumOf { it.attendedClasses }
        if (total == 0) 0f else (attended.toFloat() / total) * 100
    }
    val completedAssignments = remember(assignments) { assignments.count { it.isCompleted } }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("👨‍🎓 Student Profile", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
        
        Text("👨‍💻", fontSize = 72.sp)

        if (!editMode) {
            Text(name, fontSize = 26.sp, style = MaterialTheme.typography.titleLarge)
            Text("$dept | Semester $sem", fontSize = 16.sp, color = MaterialTheme.colorScheme.secondary)
            Text(coll, fontSize = 15.sp, color = MaterialTheme.colorScheme.outline)
            
            Button(onClick = { editMode = true }) {
                Text("Edit Profile Details")
            }
        } else {
            OutlinedTextField(value = nameField, onValueChange = { nameField = it }, label = { Text("Student Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = deptField, onValueChange = { deptField = it }, label = { Text("Department / Branch") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = collField, onValueChange = { collField = it }, label = { Text("College Name") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = semField, onValueChange = { semField = it }, label = { Text("Semester Number") }, modifier = Modifier.fillMaxWidth())
            
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    viewModel.updateProfile(nameField, deptField, collField, semField)
                    editMode = false
                }) {
                    Text("Save")
                }
                OutlinedButton(onClick = { editMode = false }) {
                    Text("Cancel")
                }
            }
        }

        Divider(modifier = Modifier.padding(vertical = 8.dp))

        Text("Academic Achievements Summary", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium, modifier = Modifier.align(Alignment.Start))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(String.format(Locale.getDefault(), "📊 Overall Attendance: %.1f%%", overallAttendance), fontSize = 16.sp)
                Text(String.format(Locale.getDefault(), "🎯 Current CGPA Score: %.2f", calculatedCgpa), fontSize = 16.sp)
                Text("📝 Completed Assignments Count: $completedAssignments tasks", fontSize = 16.sp)
            }
        }
    }
}
