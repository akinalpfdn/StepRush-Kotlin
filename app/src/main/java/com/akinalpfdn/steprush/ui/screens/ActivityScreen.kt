
package com.akinalpfdn.steprush.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.PermissionController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.akinalpfdn.steprush.data.HealthConnectManager
import com.akinalpfdn.steprush.ui.components.CircularProgressBar
import com.akinalpfdn.steprush.ui.components.StatCard
import com.akinalpfdn.steprush.viewmodel.ActivityViewModel
import java.text.DecimalFormat

@Composable
fun ActivityScreen(viewModel: ActivityViewModel = viewModel()) {
    val context = LocalContext.current
    val stepData by viewModel.stepData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val hasPermissions by viewModel.hasPermissions.collectAsState()
    
    val healthConnectManager = remember { HealthConnectManager(context) }
    
    var healthConnectAvailable by remember { mutableStateOf<Boolean?>(null) }
    
    // Health Connect availability check
    LaunchedEffect(Unit) {
        healthConnectAvailable = healthConnectManager.isHealthConnectAvailable()
    }
    
    // Activity Result Contract for permissions
    val requestPermissionActivityContract = remember { 
        PermissionController.createRequestPermissionResultContract() 
    }
    
    val requestPermissions = rememberLauncherForActivityResult(
        contract = requestPermissionActivityContract
    ) { granted ->
        if (granted.containsAll(healthConnectManager.permissions)) {
            viewModel.onPermissionsGranted()
        } else {
            viewModel.onPermissionsDenied()
        }
    }
    
    // Activity'e geri döndüğünde izinleri tekrar kontrol et
    LaunchedEffect(Unit) {
        viewModel.checkPermissionsOnResume()
    }
    
    val dailyGoal = 10000
    val currentStreak = viewModel.calculateStreak(stepData.weeklySteps, dailyGoal)
    val bestStreak = 23 // Bu değer veritabanından gelecek
    val rating = 2150
    val rankName = "Bronze Runner"
    
    val progressPercentage = (stepData.todaySteps.toFloat() / dailyGoal.toFloat()) * 100f
    
    // Health Connect kullanılamıyorsa hata mesajı göster
    if (healthConnectAvailable == false) {
        HealthConnectUnavailableScreen()
        return
    }
    
    if (!hasPermissions) {
        PermissionRequestScreen(
            onRequestPermission = { 
                requestPermissions.launch(healthConnectManager.permissions)
            }
        )
        return
    }
    
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        
        item {
            // Header Stats
            Column {
                Text(
                    text = "Toplam Adım : ${formatNumber(stepData.totalSteps)}",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E),
                )
                

            }
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
                    modifier = Modifier.padding(6.dp)
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
            // Circular Progress with Refresh Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                // Progress bar centered
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
                            text = formatNumber(stepData.todaySteps),
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
                
                // Refresh button positioned to the right
                IconButton(
                    onClick = { viewModel.refreshData() },
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Yenile",
                        tint = Color(0xFFf3770a),
                        modifier = Modifier.size(38.dp)
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

@Composable
private fun HealthConnectUnavailableScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "⚠️",
                    fontSize = 64.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Health Connect Kullanılamıyor",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Text(
                    text = "Bu özellik için Health Connect gereklidir. Lütfen Google Play Store'dan Health Connect uygulamasını indirin.",
                    fontSize = 16.sp,
                    color = Color(0xFF78716C),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun PermissionRequestScreen(
    onRequestPermission: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🏃‍♂️",
                    fontSize = 64.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Adım Takibi İçin İzin",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                
                Text(
                    text = "Uygulamanın adımlarınızı takip edebilmesi için Health Connect izni gerekiyor.",
                    fontSize = 16.sp,
                    color = Color(0xFF78716C),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
                
                Button(
                    onClick = onRequestPermission,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF92400E)
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "İzin Ver",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
            }
        }
    }
}

private fun formatNumber(number: Int): String {
    return when {
        number >= 1000000 -> "${number / 1000000}.${(number % 1000000) / 100000}M"
        else -> DecimalFormat("#,###")
            .format(number)
    }
}