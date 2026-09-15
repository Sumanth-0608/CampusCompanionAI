package com.sumanth.campuscompanionai.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sumanth.campuscompanionai.ui.viewmodel.CampusViewModel

@Composable
fun SettingsScreen(
    viewModel: CampusViewModel,
    modifier: Modifier = Modifier
) {
    var openConfirmationDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("⚙️ Settings Configuration", fontSize = 24.sp, style = MaterialTheme.typography.headlineMedium)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("App Preferences Mode", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                Text("The application theme uses the device default system wide Dark / Light configuration natively.", fontSize = 14.sp)
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Danger Zone Area", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium, color = Color.Red)
                Text("This action will erase all student data records permanently from the internal Room Storage and clear shared metrics cache.", fontSize = 14.sp)
                Button(
                    onClick = { openConfirmationDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                ) {
                    Text("Reset App Content Database", color = Color.White)
                }
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("About Application", fontSize = 18.sp, style = MaterialTheme.typography.titleMedium)
                Text("Campus Companion AI v1.0.0 Pro Edition.", fontSize = 14.sp)
                Text("Designed with Jetpack Compose & Material 3 architecture specs.", fontSize = 13.sp, color = MaterialTheme.colorScheme.outline)
            }
        }

        if (openConfirmationDialog) {
            AlertDialog(
                onDismissRequest = { openConfirmationDialog = false },
                title = { Text("Confirm Reset?") },
                text = { Text("Are you absolutely sure you want to proceed? All timetable listings, notes details, and progress points will be cleared.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.resetAllData()
                            openConfirmationDialog = false
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text("Reset")
                    }
                },
                dismissButton = {
                    OutlinedButton(onClick = { openConfirmationDialog = false }) {
                        Text("Cancel")
                    }
                }
            )
        }
    }
}
