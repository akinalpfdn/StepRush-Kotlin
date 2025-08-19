package com.akinalpfdn.steprush.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.akinalpfdn.steprush.ui.components.NeomorphicCard
import com.akinalpfdn.steprush.ui.theme.*

data class Friend(
    val id: Int,
    val name: String,
    val avatar: String,
    val isOnline: Boolean
)

data class Race(
    val id: Int,
    val name: String,
    val participants: List<Friend>,
    val duration: String,
    val timeLeft: String,
    val isActive: Boolean,
    val isJoined: Boolean,
    val leaderSteps: Int,
    val mySteps: Int,
    val leaderName: String,
    val emoji: String,
    val startTime: String,
    val endTime: String
)

@Composable
fun RacesScreen() {
    var showCreateRaceDialog by remember { mutableStateOf(false) }
    var selectedRace by remember { mutableStateOf<Race?>(null) }
    
    val friends = remember {
        listOf(
            Friend(1, "Ahmet", "👨‍💻", true),
            Friend(2, "Ayşe", "👩‍🎨", true),
            Friend(3, "Mehmet", "👨‍🏫", false),
            Friend(4, "Fatma", "👩‍⚕️", true),
            Friend(5, "Ali", "👨‍🍳", false)
        )
    }
    
    val activeRaces = remember {
        listOf(
            Race(1, "🏃‍♂️ Morning Sprint", listOf(friends[0]), "2 saat", "1:23:45", true, true, 8500, 7200, "Ahmet", "🏃‍♂️", "08:00", "10:00"),
            Race(2, "🚶‍♀️ Walking Challenge", listOf(friends[3]), "4 saat", "2:15:30", true, true, 12000, 11500, "Fatma", "🚶‍♀️", "10:00", "14:00")
        )
    }
    
    val completedRaces = remember {
        listOf(
            Race(3, "🏃‍♀️ Weekend Marathon", listOf(friends[1]), "24 saat", "Tamamlandı", false, true, 45000, 42000, "Ayşe", "🏃‍♀️", "Dün", "Bugün"),
            Race(4, "🚴‍♂️ Cycling Race", listOf(friends[2]), "6 saat", "Tamamlandı", false, false, 25000, 0, "Mehmet", "🚴‍♂️", "2 gün önce", "2 gün önce")
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BackgroundLight, Color(0xFFF0F4F8))
                )
            )
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                HeaderSection()
            }
            
            item {
                CreateRaceButton(
                    onClick = { showCreateRaceDialog = true }
                )
            }
            
            if (activeRaces.isNotEmpty()) {
                item {
                    Text(
                        text = "🔥 Active Races",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(activeRaces) { race ->
                    ActiveRaceCard(
                        race = race,
                        onClick = { selectedRace = race }
                    )
                }
            }
            
            if (completedRaces.isNotEmpty()) {
                item {
                    Text(
                        text = "📚 Race History",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(completedRaces) { race ->
                    CompletedRaceCard(
                        race = race,
                        onClick = { selectedRace = race }
                    )
                }
            }
        }
        
        // Create Race Dialog
        if (showCreateRaceDialog) {
            CreateRaceDialog(
                friends = friends,
                onDismiss = { showCreateRaceDialog = false },
                onCreateRace = { selectedFriends, duration ->
                    // TODO: Create race logic
                    showCreateRaceDialog = false
                }
            )
        }
        
        // Race Details Dialog
        selectedRace?.let { race ->
            RaceDetailsDialog(
                race = race,
                onDismiss = { selectedRace = null }
            )
        }
    }
}

@Composable
fun HeaderSection() {
    NeomorphicCard(
        modifier = Modifier.fillMaxWidth(),
        elevation = 12
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated trophy icon
            val infiniteTransition = rememberInfiniteTransition()
            val rotation by infiniteTransition.animateFloat(
                initialValue = -5f,
                targetValue = 5f,
                animationSpec = infiniteRepeatable(
                    animation = tween(2000, easing = LinearEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )
            
            Text(
                text = "🏆",
                fontSize = 48.sp,
                modifier = Modifier.rotate(rotation)
            )
            
            Text(
                text = "Friend Races",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                modifier = Modifier.padding(top = 8.dp)
            )
            
            Text(
                text = "Challenge your friends, compete together!",
                fontSize = 14.sp,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )
            
            // Stats row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem("🏃‍♂️", "2", "Active")
                StatItem("👥", "5", "Friends")
                StatItem("🏆", "4", "Completed")
            }
        }
    }
}

@Composable
fun CreateRaceButton(onClick: () -> Unit) {
    NeomorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 8
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = SkyBlue,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Create New Race",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = SkyBlue
            )
        }
    }
}

@Composable
fun ActiveRaceCard(race: Race, onClick: () -> Unit) {
    NeomorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 16
    ) {
        Column {
            // Compact info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Your steps
                Column {
                    Text(
                        text = "${race.mySteps}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyBlue
                    )
                    Text(
                        text = "your steps",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
                
                // Step difference
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val difference = race.mySteps - race.leaderSteps
                    val isAhead = difference > 0
                    Text(
                        text = if (isAhead) "+${difference}" else "${difference}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAhead) SuccessGreen else ErrorRed
                    )
                    Text(
                        text = "steps",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
                
                // Time left
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = race.timeLeft,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmOrange
                    )
                    Text(
                        text = "remaining",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Progress bar
            LinearProgressIndicator(
                progress = (race.mySteps.toFloat() / race.leaderSteps.toFloat()).coerceIn(0f, 1f),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = SkyBlue,
                trackColor = Color(0xFFE8F2FF)
            )
        }
    }
}

@Composable
fun CompletedRaceCard(race: Race, onClick: () -> Unit) {
    NeomorphicCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = 8
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Your steps
                Column {
                    Text(
                        text = "${race.mySteps}",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = SkyBlue
                    )
                    Text(
                        text = "your steps",
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
                
                // Result
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    val difference = race.mySteps - race.leaderSteps
                    val isAhead = difference > 0
                    Text(
                        text = if (isAhead) "🏆 Winner!" else "${difference} behind",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isAhead) SuccessGreen else TextSecondary
                    )
                }
                
                // Winner
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = race.leaderName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = WarmOrange
                    )
                    Text(
                        text = "winner",
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                }
            }
        }
    }
}

@Composable
fun CreateRaceDialog(
    friends: List<Friend>,
    onDismiss: () -> Unit,
    onCreateRace: (List<Friend>, String) -> Unit
) {
    var selectedFriends by remember { mutableStateOf(setOf<Friend>()) }
    var selectedDuration by remember { mutableStateOf("2 saat") }
    
    val durations = listOf("30 dakika", "1 saat", "2 saat", "4 saat", "6 saat", "24 saat")
    
    Dialog(onDismissRequest = onDismiss) {
        NeomorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 20
        ) {
            Column {
                Text(
                    text = "🏁 Create New Race",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                Text(
                    text = "Select Friends",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LazyColumn(
                    modifier = Modifier.height(200.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(friends) { friend ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedFriends = if (selectedFriends.contains(friend)) {
                                        selectedFriends - friend
                                    } else {
                                        selectedFriends + friend
                                    }
                                }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = friend.avatar,
                                fontSize = 24.sp
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = friend.name,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = if (friend.isOnline) "🟢 Online" else "⚫ Offline",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            if (selectedFriends.contains(friend)) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = SuccessGreen,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Race Duration",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                LazyColumn(
                    modifier = Modifier.height(120.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(durations) { duration ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { selectedDuration = duration }
                                .padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = duration,
                                fontSize = 14.sp,
                                color = if (selectedDuration == duration) SkyBlue else TextPrimary,
                                fontWeight = if (selectedDuration == duration) FontWeight.Bold else FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.weight(1f))
                            if (selectedDuration == duration) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    tint = SkyBlue,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = TextSecondary
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Cancel")
                    }
                    
                    Button(
                        onClick = { 
                            if (selectedFriends.isNotEmpty()) {
                                onCreateRace(selectedFriends.toList(), selectedDuration)
                            }
                        },
                        enabled = selectedFriends.isNotEmpty(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SkyBlue
                        ),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Text("Create Race")
                    }
                }
            }
        }
    }
}

@Composable
fun RaceDetailsDialog(race: Race, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        NeomorphicCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 20
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = race.emoji,
                        fontSize = 32.sp
                    )
                    Column {
                        Text(
                            text = race.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = if (race.isActive) "🟢 Active" else "⚫ Completed",
                            fontSize = 12.sp,
                            color = if (race.isActive) SuccessGreen else TextSecondary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = null,
                            tint = TextSecondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Race details
                DetailRow("Duration", race.duration)
                DetailRow("Participants", "${race.participants.size} friends")
                if (race.isActive) {
                    DetailRow("Time Left", race.timeLeft)
                } else {
                    DetailRow("Completed", race.endTime)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Leaderboard preview
                Text(
                    text = "Leaderboard",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                race.participants.take(3).forEachIndexed { index, friend ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (index) {
                                0 -> "🥇"
                                1 -> "🥈"
                                2 -> "🥉"
                                else -> "${index + 1}."
                            },
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = friend.avatar,
                            fontSize = 20.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = friend.name,
                            fontSize = 14.sp,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = if (index == 0) "${race.leaderSteps} steps" else "---",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SkyBlue
                    ),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("Close")
                }
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = TextSecondary
        )
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary
        )
    }
}

@Composable
fun StatItem(emoji: String, value: String, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = emoji,
            fontSize = 24.sp
        )
        Text(
            text = value,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = SkyBlue
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}