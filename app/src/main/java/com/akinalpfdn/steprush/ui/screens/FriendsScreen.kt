package com.akinalpfdn.steprush.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FriendsScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        
        item {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Arkadaşlar",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E),
                )
                
                IconButton(
                    onClick = { /* TODO: Add friend functionality */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Arkadaş Ekle",
                        tint = Color(0xFFf3770a),
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
        
        item {
            // Friends List Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Friends",
                        tint = Color(0xFF92400E),
                        modifier = Modifier.size(48.dp)
                    )
                    
                    Text(
                        text = "Arkadaş Listesi",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E),
                        modifier = Modifier.padding(top = 8.dp)
                    )
                    
                    Text(
                        text = "Arkadaşlarınla birlikte adım sayısı yarışına katıl!",
                        fontSize = 14.sp,
                        color = Color(0xFF78716C),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
        
        // Sample friends (mockup data)
        items(5) { index ->
            FriendCard(
                name = when (index) {
                    0 -> "Ahmet K."
                    1 -> "Ayşe M."
                    2 -> "Mehmet Y."
                    3 -> "Zeynep S."
                    else -> "Fatma T."
                },
                todaySteps = when (index) {
                    0 -> 12500
                    1 -> 8900
                    2 -> 15200
                    3 -> 6700
                    else -> 9800
                },
                isOnline = index % 2 == 0
            )
        }
        
        item {
            // Add more friends prompt
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFf3770a).copy(alpha = 0.1f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🤝",
                        fontSize = 32.sp,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    
                    Text(
                        text = "Daha Fazla Arkadaş Ekle",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                    
                    Text(
                        text = "Arkadaşlarınla birlikte daha motive ol!",
                        fontSize = 14.sp,
                        color = Color(0xFF78716C),
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    
                    Button(
                        onClick = { /* TODO: Invite friends */ },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF92400E)
                        ),
                        modifier = Modifier.padding(top = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PersonAdd,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Arkadaş Davet Et")
                    }
                }
            }
        }
    }
}

@Composable
private fun FriendCard(
    name: String,
    todaySteps: Int,
    isOnline: Boolean
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = name,
                        tint = Color(0xFF92400E),
                        modifier = Modifier.size(40.dp)
                    )
                    
                    // Online indicator
                    if (isOnline) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    Color(0xFF10B981),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .align(Alignment.BottomEnd)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(12.dp))
                
                Column {
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF92400E)
                    )
                    
                    Text(
                        text = if (isOnline) "Çevrimiçi" else "Çevrimdışı",
                        fontSize = 12.sp,
                        color = if (isOnline) Color(0xFF10B981) else Color(0xFF78716C)
                    )
                }
            }
            
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatSteps(todaySteps),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF92400E)
                )
                
                Text(
                    text = "adım",
                    fontSize = 12.sp,
                    color = Color(0xFF78716C)
                )
            }
        }
    }
}

private fun formatSteps(steps: Int): String {
    return when {
        steps >= 1000 -> "${steps / 1000}.${(steps % 1000) / 100}k"
        else -> steps.toString()
    }
}