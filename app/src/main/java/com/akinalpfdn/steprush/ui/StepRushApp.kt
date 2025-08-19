package com.akinalpfdn.steprush.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.abs
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
            NavigationBar(
                containerColor = Color(0xFFF5F5F5),
                modifier = Modifier.height(68.dp)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.DirectionsRun, contentDescription = "Activity") },
                    label = { 
                        Text(
                            text = "Activity", 
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ) 
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == "activity" } == true,
                    onClick = {
                        navController.navigate("activity") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFBB6653),
                        selectedTextColor = Color(0xFFBB6653),
                        unselectedIconColor = Color(0xFFBB6653),
                        unselectedTextColor = Color(0xFFBB6653),
                        indicatorColor = Color(0xFFBB6653).copy(alpha = 0.1f)
                    )
                )
                
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.EmojiEvents, contentDescription = "Races") },
                    label = { 
                        Text(
                            text = "Races", 
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        ) 
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == "races" } == true,
                    onClick = {
                        navController.navigate("races") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFFBB6653),
                        selectedTextColor = Color(0xFFBB6653),
                        unselectedIconColor = Color(0xFFBB6653),
                        unselectedTextColor = Color(0xFFBB6653),
                        indicatorColor = Color(0xFFBB6653).copy(alpha = 0.1f)
                    )
                )
            }
        }
    ) { innerPadding ->
        var totalDragOffset by remember { mutableStateOf(0f) }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = {
                            totalDragOffset = 0f
                        },
                        onDragEnd = {
                            val navBackStackEntry = navController.currentBackStackEntry
                            val currentRoute = navBackStackEntry?.destination?.route
                            
                            // Minimum drag distance to trigger navigation (in pixels)
                            val minDragDistance = 200f
                            
                            if (abs(totalDragOffset) > minDragDistance) {
                                when {
                                    // Swipe right (positive totalDragOffset) - go to previous screen
                                    totalDragOffset > 0 && currentRoute == "races" -> {
                                        navController.navigate("activity") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                    // Swipe left (negative totalDragOffset) - go to next screen
                                    totalDragOffset < 0 && currentRoute == "activity" -> {
                                        navController.navigate("races") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                }
                            }
                            totalDragOffset = 0f
                        }
                    ) { _, dragAmount ->
                        totalDragOffset += dragAmount
                    }
                }
        ) {
            NavHost(
                navController = navController,
                startDestination = "activity",
                modifier = Modifier.fillMaxSize()
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
}