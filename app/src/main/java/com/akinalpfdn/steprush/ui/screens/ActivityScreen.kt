package com.akinalpfdn.steprush.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.akinalpfdn.steprush.ui.components.CircularProgressBar
import com.akinalpfdn.steprush.ui.components.StatCard

@Composable
fun ActivityScreen() {
    // Mock data - will be replaced with real Health Connect data
    val totalSteps = 287543
    val dailySteps = 8247
    val dailyGoal = 10000
    val currentStreak = 12
    val bestStreak = 23
    val rating = 2150
    val rankName = "Bronze Runner"
    
    val progressPercentage = (dailySteps.toFloat() / dailyGoal.toFloat()) * 100f
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        
        item {
            // Header Stats
            Text(
                text = "Total Steps: ${formatNumber(totalSteps)}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E),
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        
        item {
            // Rank Info
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "🏆",
                            fontSize = 32.sp,
                            modifier = Modifier.padding(end = 12.dp)
                        )
                        Column {
                            Text(
                                text = rankName,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF92400E)
                            )
                            Text(
                                text = "($rating)",
                                fontSize = 18.sp,
                                color = Color(0xFF059669),
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                    
                    Text(
                        text = "Bir sonraki seviye için ${3000 - rating} Rating daha!",
                        fontSize = 16.sp,
                        color = Color(0xFF78716C),
                        modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }
        
        item {
            // Circular Progress
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                CircularProgressBar(
                    progress = progressPercentage / 100f,
                    size = 180.dp,
                    strokeWidth = 10.dp,
                    progressColor = Color(0xFFf3770a),
                    backgroundColor = Color(0xFFf5f1eb)
                )
                
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = formatNumber(dailySteps),
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                    Text(
                        text = "/ ${formatNumber(dailyGoal)} steps",
                        fontSize = 14.sp,
                        color = Color(0xFF78716C),
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${progressPercentage.toInt()}%",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF059669),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
        
        item {
            // Streak Stats
            Text(
                text = "Seriler",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Seri devam ediyor! Muhteşem gidiyorsun!",
                fontSize = 16.sp,
                color = Color(0xFF78716C),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    emoji = "🔥",
                    title = "Mevcut Adım Serisi",
                    value = currentStreak.toString(),
                    backgroundColor = Color(0xFFF97316),
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    emoji = "⭐",
                    title = "En İyi Adım Serisi",
                    value = bestStreak.toString(),
                    backgroundColor = Color(0xFFEAB308),
                    modifier = Modifier.weight(1f)
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                StatCard(
                    emoji = "🏆",
                    title = "Mevcut Galibiyet Serisi",
                    value = (currentStreak / 3).toString(),
                    backgroundColor = Color(0xFF10B981),
                    modifier = Modifier.weight(1f)
                )
                
                StatCard(
                    emoji = "👑",
                    title = "En Uzun Galibiyet Serisi",
                    value = (bestStreak / 3).toString(),
                    backgroundColor = Color(0xFF8B5CF6),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1000000 -> "${number / 1000000}.${(number % 1000000) / 100000}M"
        number >= 1000 -> "${number / 1000}.${(number % 1000) / 100}K"
        else -> number.toString()
    }
}