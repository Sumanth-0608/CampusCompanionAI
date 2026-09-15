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

@Composable
fun TimetableScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val entries by viewModel.timetableEntries.collectAsState()
    
    val days = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
    var selectedDay by remember { mutableStateOf("Monday") }
    
    var subject by remember { mutableStateOf("") }
    var start by remember { mutableStateOf("") }
    var end by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }

    var expandedDayMenu by remember { mutableStateOf(false) }

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
                Text("📅 Timetable Schedule", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
                Button(onClick = onBack) { Text("Back") }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Add Class Schedule Entry", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                    
                    Box {
                        Button(onClick = { expandedDayMenu = true }) {
                            Text("Day: $selectedDay ▾")
                        }
                        DropdownMenu(expanded = expandedDayMenu, onDismissRequest = { expandedDayMenu = false }) {
                            days.forEach { day ->
                                DropdownMenuItem(
                                    text = { Text(day) },
                                    onClick = {
                                        selectedDay = day
                                        expandedDayMenu = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(value = subject, onValueChange = { subject = it }, label = { Text("Subject Name") }, modifier = Modifier.fillMaxWidth())
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(value = start, onValueChange = { start = it }, label = { Text("Start Time") }, modifier = Modifier.weight(1f))
                        OutlinedTextField(value = end, onValueChange = { end = it }, label = { Text("End Time") }, modifier = Modifier.weight(1f))
                    }
                    OutlinedTextField(value = room, onValueChange = { room = it }, label = { Text("Room / Lab Number") }, modifier = Modifier.fillMaxWidth())

                    Button(
                        onClick = {
                            if (subject.isNotBlank() && start.isNotBlank()) {
                                viewModel.addTimetableEntry(selectedDay, subject, start, end, room.ifBlank { "N/A" })
                                subject = ""
                                start = ""
                                end = ""
                                room = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add to Timetable")
                    }
                }
            }
        }

        item {
            Text("Your Full Class Schedule", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
        }

        items(entries.sortedWith(compareBy({ days.indexOf(it.dayOfWeek) }, { it.startTime }))) { entry ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("${entry.dayOfWeek} • ${entry.subjectName}", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                        Text("🕒 ${entry.startTime} - ${entry.endTime} | 📍 Room: ${entry.roomNumber}", fontSize = 15.sp)
                    }
                    IconButton(onClick = { viewModel.deleteTimetableEntry(entry) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}
