package com.akinalpfdn.steprush.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.akinalpfdn.steprush.ui.theme.*

@Composable
fun NeomorphicCard(
    modifier: Modifier = Modifier,
    elevation: Int = 8,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = elevation.dp,
                shape = RoundedCornerShape(16.dp),
                spotColor = DarkShadow,
                ambientColor = LightShadow
            )
            .clip(RoundedCornerShape(16.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(GradientStart, GradientEnd)
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}

@Composable
fun NeomorphicButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
                spotColor = DarkShadow,
                ambientColor = LightShadow
            )
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(SkyBlue, Color(0xFF3A7BC8))
                )
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
    ) {
        Box(
            modifier = Modifier.padding(16.dp)
        ) {
            content()
        }
    }
}
