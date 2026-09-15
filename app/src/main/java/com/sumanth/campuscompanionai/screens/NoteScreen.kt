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
fun NoteScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val list by viewModel.notes.collectAsState()
    val searchQuery by viewModel.notesSearchQuery.collectAsState()

    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

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
                Text("📖 Lecture Notes", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
                Button(onClick = onBack) { Text("Back") }
            }
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("🔎 Search Notes...") },
                modifier = Modifier.fillMaxWidth()
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Create New Note", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Note Title") }, modifier = Modifier.fillMaxWidth())
                    OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Note Content Body") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
                    
                    Button(
                        onClick = {
                            if (title.isNotBlank() && content.isNotBlank()) {
                                viewModel.addNote(title, content)
                                title = ""
                                content = ""
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Save Note")
                    }
                }
            }
        }

        items(list) { note ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(note.title, fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                        IconButton(onClick = { viewModel.deleteNote(note) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                    Text(note.content, fontSize = 15.sp)
                }
            }
        }
    }
}
