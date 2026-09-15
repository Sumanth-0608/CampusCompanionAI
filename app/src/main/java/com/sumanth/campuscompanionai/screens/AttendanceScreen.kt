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
import com.sumanth.campuscompanionai.data.local.entities.AttendanceRecord
import com.sumanth.campuscompanionai.ui.viewmodel.CampusViewModel
import kotlin.math.ceil

@Composable
fun AttendanceScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val records by viewModel.attendanceRecords.collectAsState()
    
    var subjectName by remember { mutableStateOf("") }
    var attendedInput by remember { mutableStateOf("") }
    var totalInput by remember { mutableStateOf("") }

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
                Text("📊 Attendance Tracker", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
                Button(onClick = onBack) { Text("Back") }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Add Subject Attendance", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = subjectName,
                        onValueChange = { subjectName = it },
                        label = { Text("Subject Name") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = attendedInput,
                            onValueChange = { attendedInput = it },
                            label = { Text("Attended") },
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedTextField(
                            value = totalInput,
                            onValueChange = { totalInput = it },
                            label = { Text("Total Classes") },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    Button(
                        onClick = {
                            val att = attendedInput.toIntOrNull() ?: 0
                            val tot = totalInput.toIntOrNull() ?: 0
                            if (subjectName.isNotBlank() && tot >= att && tot > 0) {
                                viewModel.addAttendance(subjectName, att, tot)
                                subjectName = ""
                                attendedInput = ""
                                totalInput = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Subject")
                    }
                }
            }
        }

        items(records) { record ->
            val percentage = if (record.totalClasses > 0) (record.attendedClasses.toFloat() / record.totalClasses) * 100 else 0f
            
            // Logic for targets
            val target75 = ceil((0.75f * record.totalClasses - record.attendedClasses) / 0.25f).toInt().coerceAtLeast(0)
            val target80 = ceil((0.80f * record.totalClasses - record.attendedClasses) / 0.20f).toInt().coerceAtLeast(0)
            val target85 = ceil((0.85f * record.totalClasses - record.attendedClasses) / 0.15f).toInt().coerceAtLeast(0)
            
            val safeLeave = ((record.attendedClasses - 0.75f * record.totalClasses) / 0.75f).toInt().coerceAtLeast(0)

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(record.subjectName, fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
                        IconButton(onClick = { viewModel.deleteAttendance(record) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                    
                    Text("Classes: ${record.attendedClasses} / ${record.totalClasses}", fontSize = 16.sp)
                    
                    LinearProgressIndicator(
                        progress = if (record.totalClasses > 0) record.attendedClasses.toFloat() / record.totalClasses else 0f,
                        modifier = Modifier.fillMaxWidth(),
                        color = if (percentage >= 75f) Color(0xFF4CAF50) else Color.Red
                    )
                    
                    Text(
                        text = String.format("Percentage: %.1f%%", percentage),
                        fontSize = 16.sp,
                        color = if (percentage >= 75f) Color(0xFF4CAF50) else Color.Red
                    )

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    if (percentage < 75f) {
                        Text("⚡ Need $target75 classes to reach 75%", color = Color.Red, fontSize = 14.sp)
                    } else {
                        Text("✅ Safe to leave next $safeLeave classes", color = Color(0xFF4CAF50), fontSize = 14.sp)
                    }
                    Text("• Need $target80 classes to reach 80%", fontSize = 13.sp)
                    Text("• Need $target85 classes to reach 85%", fontSize = 13.sp)
                }
            }
        }
    }
}
