package com.sumanth.campuscompanionai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sumanth.campuscompanionai.screens.*
import com.sumanth.campuscompanionai.ui.theme.CampusCompanionAITheme
import com.sumanth.campuscompanionai.ui.viewmodel.CampusViewModel
import com.sumanth.campuscompanionai.ui.viewmodel.CampusViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CampusCompanionAITheme {
                val app = application as CampusApplication
                val campusViewModel: CampusViewModel = viewModel(
                    factory = CampusViewModelFactory(app.repository, app.prefs)
                )
                
                val navController = rememberNavController()
                
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentRoute = navBackStackEntry?.destination?.route
                        
                        // Only show bottom bar on core application tabs
                        val coreTabs = listOf("dashboard", "timetable", "assignments", "notes", "profile", "settings")
                        if (currentRoute in coreTabs) {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = currentRoute == "dashboard",
                                    onClick = { navigateToTab(navController, "dashboard") },
                                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                                    label = { Text("Home") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "timetable",
                                    onClick = { navigateToTab(navController, "timetable") },
                                    icon = { Icon(Icons.Default.DateRange, contentDescription = "Schedule") },
                                    label = { Text("Schedule") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "assignments",
                                    onClick = { navigateToTab(navController, "assignments") },
                                    icon = { Icon(Icons.Default.Assignment, contentDescription = "Assg") },
                                    label = { Text("Tasks") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "notes",
                                    onClick = { navigateToTab(navController, "notes") },
                                    icon = { Icon(Icons.Default.Book, contentDescription = "Notes") },
                                    label = { Text("Notes") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "profile",
                                    onClick = { navigateToTab(navController, "profile") },
                                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                                    label = { Text("Profile") }
                                )
                                NavigationBarItem(
                                    selected = currentRoute == "settings",
                                    onClick = { navigateToTab(navController, "settings") },
                                    icon = { Icon(Icons.Default.Settings, contentDescription = "Settings") },
                                    label = { Text("Settings") }
                                )
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "dashboard",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("dashboard") {
                            DashboardScreen(viewModel = campusViewModel, onNavigate = { route -> navController.navigate(route) })
                        }
                        composable("timetable") {
                            TimetableScreen(viewModel = campusViewModel, onBack = { navController.popBackStack() })
                        }
                        composable("assignments") {
                            AssignmentScreen(viewModel = campusViewModel, onBack = { navController.popBackStack() })
                        }
                        composable("notes") {
                            NoteScreen(viewModel = campusViewModel, onBack = { navController.popBackStack() })
                        }
                        composable("profile") {
                            ProfileScreen(viewModel = campusViewModel)
                        }
                        composable("settings") {
                            SettingsScreen(viewModel = campusViewModel)
                        }
                        // Inner deep link flows
                        composable("attendance") {
                            AttendanceScreen(viewModel = campusViewModel, onBack = { navController.popBackStack() })
                        }
                        composable("cgpa") {
                            CGPAScreen(viewModel = campusViewModel, onBack = { navController.popBackStack() })
                        }
                        composable("planner") {
                            StudyPlannerScreen(viewModel = campusViewModel, onBack = { navController.popBackStack() })
                        }
                        composable("exams") {
                            ExamCountdownScreen(viewModel = campusViewModel, onBack = { navController.popBackStack() })
                        }
                        composable("ai") {
                            AiAssistantScreen(viewModel = campusViewModel, onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }

    private fun navigateToTab(navController: NavHostController, route: String) {
        navController.navigate(route) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}
