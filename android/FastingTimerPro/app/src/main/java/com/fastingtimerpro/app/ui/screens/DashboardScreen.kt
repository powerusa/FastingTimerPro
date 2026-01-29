package com.fastingtimerpro.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fastingtimerpro.app.domain.CompletedFast
import com.fastingtimerpro.app.domain.FastingProgressCalculator
import com.fastingtimerpro.app.domain.FastingSession
import com.fastingtimerpro.app.ui.theme.AppColors
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen() {
    var now by remember { mutableStateOf(Instant.now()) }
    var activeSession by remember { mutableStateOf<FastingSession?>(null) }
    var showCustomPicker by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var completedFasts by remember { mutableStateOf<List<CompletedFast>>(emptyList()) }

    if (showHistory) {
        HistoryScreen(
            completedFasts = completedFasts,
            onBack = { showHistory = false },
            onClearHistory = { completedFasts = emptyList() },
            onDeleteFast = { id -> completedFasts = completedFasts.filter { it.id != id } }
        )
        return
    }

    LaunchedEffect(Unit) {
        while (true) {
            delay(1000)
            now = Instant.now()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        AppColors.gradientTop,
                        AppColors.baseBackground,
                        AppColors.gradientBottom
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Fasting Timer Pro",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.primaryText
                    )
                    Text(
                        text = "Educational tracking only. No medical advice.",
                        fontSize = 13.sp,
                        color = AppColors.secondaryText
                    )
                }
                Text(
                    text = "⏱",
                    fontSize = 24.sp,
                    modifier = Modifier.clickable { showHistory = true }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (activeSession != null) {
                ActiveSessionContent(
                    session = activeSession!!,
                    now = now,
                    onStop = {
                        completedFasts = listOf(CompletedFast.fromSession(activeSession!!)) + completedFasts
                        activeSession = null
                    }
                )
            } else {
                StartFastContent(
                    onStartFast = { duration ->
                        activeSession = FastingSession.startNow(plannedDuration = duration)
                    },
                    onShowCustomPicker = { showCustomPicker = true }
                )
            }
        }
    }

    if (showCustomPicker) {
        CustomDurationPickerDialog(
            onDismiss = { showCustomPicker = false },
            onConfirm = { duration ->
                activeSession = FastingSession.startNow(plannedDuration = duration)
                showCustomPicker = false
            }
        )
    }
}

@Composable
private fun ActiveSessionContent(
    session: FastingSession,
    now: Instant,
    onStop: () -> Unit
) {
    val progress = remember(now, session) {
        FastingProgressCalculator.compute(session = session, now = now)
    }

    GlassCard {
        Text(
            text = "Remaining",
            fontSize = 14.sp,
            color = AppColors.secondaryText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatDuration(progress.remaining),
            fontSize = 42.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.primaryText
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = formatPlannedDuration(session.plannedDuration),
            fontSize = 12.sp,
            color = AppColors.brightRed
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Elapsed: ${formatDuration(progress.elapsed)}",
            fontSize = 16.sp,
            color = AppColors.primaryText
        )
    }

    GlassCard {
        Text(
            text = "What's happening in your body",
            fontSize = 14.sp,
            color = AppColors.secondaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = progress.currentStage.title,
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.primaryAccent
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = progress.currentStage.descriptionLines.joinToString("\n"),
            fontSize = 15.sp,
            color = AppColors.primaryText.copy(alpha = 0.9f)
        )
    }

    if (progress.nextStage != null) {
        GlassCard {
            Text(
                text = "Next: ${progress.nextStage.title}",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.secondaryAccent
            )
        }
    }

    GlassPillButton(
        text = "Stop Fast",
        isDestructive = true,
        onClick = onStop,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun StartFastContent(
    onStartFast: (Duration) -> Unit,
    onShowCustomPicker: () -> Unit
) {
    GlassCard {
        Text(
            text = "Start a fast",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.primaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Choose a target duration. You can extend anytime.",
            fontSize = 14.sp,
            color = AppColors.secondaryText
        )
    }

    val presetHours = listOf(12, 16, 18, 24, 36, 48, 72)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        presetHours.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { hours ->
                    GlassPillButton(
                        text = "${hours}h",
                        isPrimary = true,
                        onClick = { onStartFast(Duration.ofHours(hours.toLong())) },
                        modifier = Modifier.weight(1f)
                    )
                }
                if (row.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }

        GlassPillButton(
            text = "Custom",
            onClick = onShowCustomPicker,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun GlassPillButton(
    text: String,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    isDestructive: Boolean = false,
    onClick: () -> Unit
) {
    val backgroundColor = when {
        isDestructive -> AppColors.destructiveButtonBackground
        isPrimary -> AppColors.primaryButtonBackground
        else -> AppColors.secondaryButtonBackground
    }

    val borderColor = when {
        isDestructive -> AppColors.destructiveButtonBackground.copy(alpha = 0.5f)
        isPrimary -> AppColors.primaryAccent.copy(alpha = 0.3f)
        else -> AppColors.cardBorder
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(999.dp))
            .background(backgroundColor)
            .border(1.dp, borderColor, RoundedCornerShape(999.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp, horizontal = 16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.primaryText
        )
    }
}

@Composable
private fun GlassCard(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 20.dp,
                shape = RoundedCornerShape(22.dp),
                ambientColor = AppColors.baseBackground,
                spotColor = AppColors.baseBackground.copy(alpha = 0.5f)
            )
            .clip(RoundedCornerShape(22.dp))
            .background(AppColors.cardBackground)
            .border(
                width = 1.dp,
                color = AppColors.cardBorder,
                shape = RoundedCornerShape(22.dp)
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            content()
        }
    }
}

private fun formatDuration(d: Duration): String {
    val totalSeconds = d.seconds.coerceAtLeast(0)
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    val seconds = totalSeconds % 60
    return String.format("%02d:%02d:%02d", hours, minutes, seconds)
}

private fun formatPlannedDuration(d: Duration): String {
    val totalHours = d.toHours()
    return if (totalHours >= 24) {
        val days = totalHours / 24
        val hours = totalHours % 24
        if (hours > 0) {
            "$days day${if (days == 1L) "" else "s"} $hours hour${if (hours == 1L) "" else "s"} fast"
        } else {
            "$days day${if (days == 1L) "" else "s"} fast"
        }
    } else {
        "$totalHours hour${if (totalHours == 1L) "" else "s"} fast"
    }
}
