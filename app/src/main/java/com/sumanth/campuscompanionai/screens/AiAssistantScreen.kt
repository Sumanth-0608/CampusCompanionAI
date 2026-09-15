package com.sumanth.campuscompanionai.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumanth.campuscompanionai.ui.viewmodel.CampusViewModel

@Composable
fun AiAssistantScreen(
    viewModel: CampusViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val messages by viewModel.chatMessages.collectAsState()
    val isTyping by viewModel.isAiTyping.collectAsState()

    var inputMessage by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🤖 AI Campus Helper", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)
            Button(onClick = onBack) { Text("Back") }
        }

        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(messages) { pair ->
                val isUser = pair.second
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = if (isUser) Alignment.CenterEnd else Alignment.CenterStart
                ) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUser) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
                        ),
                        modifier = Modifier.widthIn(max = 280.dp)
                    ) {
                        Text(pair.first, modifier = Modifier.padding(12.dp), fontSize = 16.sp)
                    }
                }
            }

            if (isTyping) {
                item {
                    Text("AI Companion is typing... 💬", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 8.dp))
                }
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputMessage,
                onValueChange = { inputMessage = it },
                placeholder = { Text("Ask your campus buddy anything...") },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(24.dp)
            )
            Button(
                onClick = {
                    if (inputMessage.isNotBlank()) {
                        viewModel.sendAiMessage(inputMessage)
                        inputMessage = ""
                    }
                },
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Send")
            }
        }
    }
}
