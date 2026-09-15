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
fun StudyPlannerScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val goals by viewModel.studyGoals.collectAsState()

    var goalTitle by remember { mutableStateOf("") }
    var isWeekly by remember { mutableStateOf(false) }

    val completedCount = remember(goals) { goals.count { it.isCompleted } }
    val progressFraction = remember(goals) { if (goals.isEmpty()) 0f else completedCount.toFloat() / goals.size }

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
                Text("📚 Study Planner & Goals", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
                Button(onClick = onBack) { Text("Back") }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Overall Goal Progress Status", fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    LinearProgressIndicator(progress = progressFraction, modifier = Modifier.fillMaxWidth())
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("$completedCount / ${goals.size} Completed Tasks", fontSize = 14.sp, color = Color.Gray)
                }
            }
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Create Target Goal", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(value = goalTitle, onValueChange = { goalTitle = it }, label = { Text("Goal Title / Objective") }, modifier = Modifier.fillMaxWidth())
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = !isWeekly, onClick = { isWeekly = false })
                        Text("Daily Goal")
                        Spacer(modifier = Modifier.width(16.dp))
                        RadioButton(selected = isWeekly, onClick = { isWeekly = true })
                        Text("Weekly Target")
                    }

                    Button(
                        onClick = {
                            if (goalTitle.isNotBlank()) {
                                viewModel.addStudyGoal(goalTitle, isWeekly)
                                goalTitle = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Goal")
                    }
                }
            }
        }

        items(goals) { goal ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = goal.isCompleted,
                        onCheckedChange = { viewModel.toggleGoalStatus(goal) }
                    )
                    Column(modifier = Modifier.weight(1f).padding(horizontal = 8.dp)) {
                        Text(
                            text = goal.title,
                            fontSize = 18.sp,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (goal.isCompleted) Color.Gray else Color.Unspecified
                        )
                        Text(if (goal.isWeekly) "🗓️ Weekly Goal" else "☀️ Daily Goal", fontSize = 13.sp, color = Color.Gray)
                    }
                    IconButton(onClick = { viewModel.deleteGoal(goal) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                    }
                }
            }
        }
    }
}
