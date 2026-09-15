package com.sumanth.campuscompanionai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumanth.campuscompanionai.ui.viewmodel.CampusViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.max

@Composable
fun ExamCountdownScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val list by viewModel.exams.collectAsState()

    var subject by remember { mutableStateOf("") }
    var daysInput by remember { mutableStateOf("") }

    val sdf = SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault())

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("⏳ Exam Deadlines", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
                Button(onClick = onBack) { Text("Back") }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Add Upcoming Exam", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject / Module") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = daysInput, onValueChange = { daysInput = it }, label = { Text("Days From Now (e.g. 15)") }, modifier = Modifier.fillMaxWidth())
                    
                    Button(
                        onClick = {
                            val days = daysInput.toLongOrNull() ?: 1L
                            val futureTime = System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000L)
                            if (subject.isNotBlank()) {
                                viewModel.addExam(subject, futureTime)
                                subject = ""
                                daysInput = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Exam")
                    }
                }
            }
        }

        items(list) { exam ->
            val diffMs = exam.examDate - System.currentTimeMillis()
            val daysLeft = max(0L, diffMs / (1000 * 60 * 60 * 24))

            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(exam.subject, fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
                        Text("📅 Date: ${sdf.format(Date(exam.examDate))}", fontSize = 15.sp, color = Color.Gray)
                    }
                    
                    Column(horizontalAlignment = Alignment.End) {
                        Text("$daysLeft", fontSize = 28.sp, style = MaterialTheme.typography.headlineLarge, color = if (daysLeft <= 3) Color.Red else MaterialTheme.colorScheme.primary)
                        Text("Days Left", fontSize = 12.sp, color = Color.Gray)
                    }

                    IconButton(onClick = { viewModel.deleteExam(exam) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}
