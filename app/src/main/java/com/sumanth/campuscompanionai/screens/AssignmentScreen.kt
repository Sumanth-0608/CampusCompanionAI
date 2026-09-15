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

@Composable
fun AssignmentScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val list by viewModel.assignments.collectAsState()

    var subject by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var daysInputField by remember { mutableStateOf("") }

    val sdf = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())

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
                Text("📝 Assignment Tasks", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
                Button(onClick = onBack) { Text("Back") }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Add Assignment Task", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject Reference") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = description, onValueChange = { description = it }, label = { Text("Assignment Task Description") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = daysInputField, onValueChange = { daysInputField = it }, label = { Text("Days Until Due (e.g. 3)") }, modifier = Modifier.fillMaxWidth())
                    
                    Button(
                        onClick = {
                            val days = daysInputField.toIntOrNull() ?: 1
                            val computedDueDate = System.currentTimeMillis() + (days * 24 * 60 * 60 * 1000L)
                            if (subject.isNotBlank() && description.isNotBlank()) {
                                viewModel.addAssignment(subject, description, computedDueDate)
                                subject = ""
                                description = ""
                                daysInputField = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Assignment")
                    }
                }
            }
        }

        items(list) { assignment ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = assignment.isCompleted,
                        onCheckedChange = { viewModel.toggleAssignmentStatus(assignment) }
                    )
                    
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                        Text(
                            text = assignment.subject,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (assignment.isCompleted) Color.Gray else Color.Unspecified
                        )
                        Text(assignment.description, fontSize = 15.sp)
                        Text("📅 Due Date: ${sdf.format(Date(assignment.dueDate))}", fontSize = 13.sp, color = Color.Gray)
                    }

                    IconButton(onClick = { viewModel.deleteAssignment(assignment) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}
