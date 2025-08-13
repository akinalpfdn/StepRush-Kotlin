package com.akinalpfdn.steprush.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.akinalpfdn.steprush.ui.screens.ActivityScreen
import com.akinalpfdn.steprush.ui.screens.RacesScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepRushApp() {
    val navController = rememberNavController()
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DirectionsRun, contentDescription = "Activity") },
                    label = { Text("Activity") },
                    selected = currentDestination?.hierarchy?.any { it.route == "activity" } == true,
                    onClick = {
                        navController.navigate("activity") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.EmojiEvents, contentDescription = "Races") },
                    label = { Text("Races") },
                    selected = currentDestination?.hierarchy?.any { it.route == "races" } == true,
                    onClick = {
                        navController.navigate("races") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "activity",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("activity") {
                ActivityScreen()
            }
            composable("races") {
                RacesScreen()
            }
        }
    }
}