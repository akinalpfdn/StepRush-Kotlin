package com.akinalpfdn.steprush.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircularProgressBar(
    progress: Float,
    size: Dp = 120.dp,
    strokeWidth: Dp = 8.dp,
    progressColor: Color = Color.Blue,
    backgroundColor: Color = Color.Gray,
    content: @Composable () -> Unit = {}
) {
    Box(
        modifier = Modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val canvasSize = this.size
            val strokeWidthPx = strokeWidth.toPx()
            val radius = (canvasSize.minDimension - strokeWidthPx) / 2
            val centerX = canvasSize.width / 2f
            val centerY = canvasSize.height / 2f
            
            // Background circle
            drawCircle(
                color = backgroundColor,
                radius = radius,
                center = Offset(centerX, centerY),
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
            
            // Progress arc
            val sweepAngle = 360 * progress
            val arcSize = Size(radius * 2, radius * 2)
            val topLeft = Offset(
                centerX - radius,
                centerY - radius
            )
            
            drawArc(
                color = progressColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round)
            )
        }
        
        content()
    }
}