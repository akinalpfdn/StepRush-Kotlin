package com.akinalpfdn.steprush.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun RacesScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFECECEC))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🏁",
                fontSize = 64.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                text = "Races",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF92400E),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Race features coming soon!",
                fontSize = 16.sp,
                color = Color(0xFF78716C)
            )
        }
    }
}