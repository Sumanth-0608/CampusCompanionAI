package com.sumanth.campuscompanionai.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumanth.campuscompanionai.ui.viewmodel.CampusViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.max

@Composable
fun DashboardScreen(
    viewModel: CampusViewModel,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val name by viewModel.studentName.collectAsState()
    val attendanceRecords by viewModel.attendanceRecords.collectAsState()
    val cgpaRecords by viewModel.cgpaRecords.collectAsState()
    val assignments by viewModel.assignments.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val timetable by viewModel.timetableEntries.collectAsState()
    val goals by viewModel.studyGoals.collectAsState()

    // Calculated fields
    val overallAttendance = remember(attendanceRecords) {
        val total = attendanceRecords.sumOf { it.totalClasses }
        val attended = attendanceRecords.sumOf { it.attendedClasses }
        if (total == 0) 84f else (attended.toFloat() / total) * 100
    }
    val calculatedCgpa = remember(cgpaRecords) {
        if (cgpaRecords.isEmpty()) 8.20f else cgpaRecords.map { it.gpa }.sum() / cgpaRecords.size
    }
    val pendingAssignmentsCount = remember(assignments) { assignments.count { !it.isCompleted } }
    
    val nearestExamDays = remember(exams) {
        val futureExams = exams.filter { it.examDate > System.currentTimeMillis() }
        if (futureExams.isEmpty()) "N/A" else {
            val nearest = futureExams.minByOrNull { it.examDate }!!
            val diff = nearest.examDate - System.currentTimeMillis()
            "${max(0L, diff / (1000 * 60 * 60 * 24))}"
        }
    }

    val currentDay = remember {
        SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
    }
    val todaysClasses = remember(timetable, currentDay) {
        timetable.filter { it.dayOfWeek.equals(currentDay, ignoreCase = true) }
    }

    val completedGoals = remember(goals) { goals.count { it.isCompleted } }
    val goalsFraction = remember(goals) { if (goals.isEmpty()) 0.5f else completedGoals.toFloat() / goals.size }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcoming Gradient Header Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(Brush.linearGradient(colors = listOf(Color(0xFF2196F3), Color(0xFF673AB7))))
                    .padding(20.dp)
            ) {
                Column {
                    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
                        in 0..11 -> "Good Morning ☀️"
                        in 12..16 -> "Good Afternoon 🌤️"
                        else -> "Good Evening 🌙"
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = greeting, fontSize = 28.sp, color = Color.White)
                            Text(text = name, fontSize = 18.sp, color = Color.White)
                            Text(text = "Let's make today highly productive!", fontSize = 14.sp, color = Color.White.copy(alpha = 0.8f))
                        }
                        Text(text = "👨‍🎓", fontSize = 50.sp)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column {
                            Text(String.format("%.0f%%", overallAttendance), color = Color.White, fontSize = 24.sp)
                            Text("Attendance", color = Color.White)
                        }
                        Column {
                            Text("$pendingAssignmentsCount", color = Color.White, fontSize = 24.sp)
                            Text("Pending Assg", color = Color.White)
                        }
                        Column {
                            Text(nearestExamDays, color = Color.White, fontSize = 24.sp)
                            Text("Exam Days Left", color = Color.White)
                        }
                    }
                }
            }
        }

        // Daily Motivation Quote Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "💡 Daily Motivation Target", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "\"Small progress every single day builds up to epic milestones.\"", fontSize = 16.sp)
            }
        }

        // Overview Scores Card
        Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(20.dp)) {
            Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = String.format("%.2f", calculatedCgpa), fontSize = 22.sp)
                    Text("CGPA Score")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${timetable.size}", fontSize = 22.sp)
                    Text("Classes Set")
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "${goals.size}", fontSize = 22.sp)
                    Text("Goals Created")
                }
            }
        }

        // Study Goals Progress indicators
        Text(text = "Today's Trackers Progress 📈", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Attendance Target Goal")
                LinearProgressIndicator(progress = overallAttendance / 100f, modifier = Modifier.fillMaxWidth())
                Text("Study Planner Objectives Progress")
                LinearProgressIndicator(progress = goalsFraction, modifier = Modifier.fillMaxWidth())
            }
        }

        // Today's Live Classes Card list
        Text(text = "Today's Live Classes ($currentDay) 🎓", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
        Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Color.White)) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                if (todaysClasses.isEmpty()) {
                    Text("No classes scheduled for today! Enjoy your free study blocks. ✨", color = Color.Gray)
                } else {
                    todaysClasses.forEach { cls ->
                        Text("• ${cls.startTime} - ${cls.endTime} : ${cls.subjectName} [Room: ${cls.roomNumber}]", fontSize = 16.sp)
                    }
                }
            }
        }

        // Quick Navigator Feature Grid Layout
        Text(text = "Quick Navigator Features", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { onNavigate("attendance") }, modifier = Modifier.fillMaxWidth()) { Text("📊 Attendance") }
                Button(onClick = { onNavigate("assignments") }, modifier = Modifier.fillMaxWidth()) { Text("📝 Assignments") }
                Button(onClick = { onNavigate("cgpa") }, modifier = Modifier.fillMaxWidth()) { Text("🎯 CGPA Calc") }
                Button(onClick = { onNavigate("planner") }, modifier = Modifier.fillMaxWidth()) { Text("📚 Planner") }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(onClick = { onNavigate("timetable") }, modifier = Modifier.fillMaxWidth()) { Text("📅 Timetable") }
                Button(onClick = { onNavigate("notes") }, modifier = Modifier.fillMaxWidth()) { Text("📖 Lecture Notes") }
                Button(onClick = { onNavigate("exams") }, modifier = Modifier.fillMaxWidth()) { Text("⏳ Exam Alert") }
                Button(onClick = { onNavigate("ai") }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)) { Text("🤖 AI Helper") }
            }
        }
    }
}
