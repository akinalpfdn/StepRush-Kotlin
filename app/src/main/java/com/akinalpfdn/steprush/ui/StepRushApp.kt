package com.akinalpfdn.steprush.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.People
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
import com.akinalpfdn.steprush.ui.screens.FriendsScreen
import com.akinalpfdn.steprush.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepRushApp() {
    val navController = rememberNavController()
    
    StepRushTheme(
        darkTheme = false, // Force light theme
        dynamicColor = false // Disable dynamic colors for consistent theme
    ) {
        Scaffold(
        bottomBar = {
            Surface(
                color = SurfaceWhite,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal =  6.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Activity Button
                    CustomNavButton(
                        icon = Icons.Filled.DirectionsRun,
                        label = "Activity",
                        isSelected = currentDestination?.hierarchy?.any { it.route == "activity" } == true,
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
                    
                    // Friends Button
                    CustomNavButton(
                        icon = Icons.Filled.People,
                        label = "Friends",
                        isSelected = currentDestination?.hierarchy?.any { it.route == "friends" } == true,
                        onClick = {
                            navController.navigate("friends") {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                    
                    // Races Button
                    CustomNavButton(
                        icon = Icons.Filled.EmojiEvents,
                        label = "Races",
                        isSelected = currentDestination?.hierarchy?.any { it.route == "races" } == true,
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
                                    totalDragOffset > 0 && currentRoute == "friends" -> {
                                        navController.navigate("activity") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                    totalDragOffset > 0 && currentRoute == "races" -> {
                                        navController.navigate("friends") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                    // Swipe left (negative totalDragOffset) - go to next screen
                                    totalDragOffset < 0 && currentRoute == "activity" -> {
                                        navController.navigate("friends") {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                    totalDragOffset < 0 && currentRoute == "friends" -> {
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
                composable("friends") {
                    FriendsScreen()
                }
                composable("races") {
                    RacesScreen()
                }
            }
        }
    }
    }
}

@Composable
private fun CustomNavButton(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 2.dp, horizontal =  6.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(
                    color = if (isSelected) SkyBlue.copy(alpha = 0.2f) else Color.Transparent,
                    shape = RoundedCornerShape(20.dp)
                )
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isSelected) SkyBlue else TextSecondary,
                modifier = Modifier.padding(bottom = 2.dp)
            )
        }
        
        Text(
            text = label,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            color = if (isSelected) SkyBlue else TextSecondary
        )
    }
}