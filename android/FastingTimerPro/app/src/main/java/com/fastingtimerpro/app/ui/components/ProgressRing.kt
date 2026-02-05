package com.fastingtimerpro.app.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.fastingtimerpro.app.ui.theme.AppColors

@Composable
fun ProgressRing(
    progress: Float,
    modifier: Modifier = Modifier,
    size: Dp = 220.dp,
    strokeWidth: Dp = 14.dp
) {
    val progressClamped = progress.coerceIn(0f, 1f)
    
    Canvas(modifier = modifier.size(size)) {
        val stroke = strokeWidth.toPx()
        val diameter = size.toPx()
        val radius = (diameter - stroke) / 2
        val topLeft = Offset(stroke / 2, stroke / 2)
        val arcSize = Size(radius * 2, radius * 2)
        
        // Background track
        drawArc(
            color = AppColors.cardBackground,
            startAngle = -90f,
            sweepAngle = 360f,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
        
        // Progress arc
        drawArc(
            color = AppColors.primaryAccent,
            startAngle = -90f,
            sweepAngle = 360f * progressClamped,
            useCenter = false,
            topLeft = topLeft,
            size = arcSize,
            style = Stroke(width = stroke, cap = StrokeCap.Round)
        )
    }
}
