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
fun CGPAScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val records by viewModel.cgpaRecords.collectAsState()

    var semName by remember { mutableStateOf("") }
    var gpaInput by remember { mutableStateOf("") }

    val calculatedCgpa = remember(records) {
        if (records.isEmpty()) 0f else records.map { it.gpa }.sum() / records.size
    }

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
                Text("🎯 CGPA Calculator", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
                Button(onClick = onBack) { Text("Back") }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Current Cumulative CGPA", fontSize = 16.sp)
                    Text(String.format("%.2f", calculatedCgpa), fontSize = 48.sp, style = MaterialTheme.typography.displayLarge)
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Add Semester GPA Record", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(
                        value = semName,
                        onValueChange = { semName = it },
                        label = { Text("Semester / Subject Reference") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = gpaInput,
                        onValueChange = { gpaInput = it },
                        label = { Text("GPA / SGPA (e.g. 9.1)") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Button(
                        onClick = {
                            val gpa = gpaInput.toFloatOrNull() ?: 0f
                            if (semName.isNotBlank() && gpa in 0f..10f) {
                                viewModel.addCGPARecord(semName, gpa)
                                semName = ""
                                gpaInput = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Add Record")
                    }
                }
            }
        }

        item {
            Text("Previous GPA Records History", fontSize = 20.sp, style = MaterialTheme.typography.titleLarge)
        }

        items(records) { record ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(record.semesterName, fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                        Text("GPA: ${record.gpa}", fontSize = 16.sp)
                    }
                    IconButton(onClick = { viewModel.deleteCGPA(record) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}
