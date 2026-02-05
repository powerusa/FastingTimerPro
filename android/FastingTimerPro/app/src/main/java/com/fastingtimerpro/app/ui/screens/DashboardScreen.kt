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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.fastingtimerpro.app.R
import com.fastingtimerpro.app.data.SessionManager
import com.fastingtimerpro.app.domain.CompletedFast
import com.fastingtimerpro.app.domain.FastingProgressCalculator
import com.fastingtimerpro.app.domain.FastingSession
import com.fastingtimerpro.app.domain.FastingSessionStartMode
import com.fastingtimerpro.app.ui.components.ProgressRing
import com.fastingtimerpro.app.ui.theme.AppColors
import java.time.Duration
import java.time.Instant
import kotlinx.coroutines.delay

@Composable
fun DashboardScreen() {
    val context = LocalContext.current
    var now by remember { mutableStateOf(Instant.now()) }
    var activeSession by remember { mutableStateOf(SessionManager.loadSession(context)) }
    var showCustomPicker by remember { mutableStateOf(false) }
    var showRetroactivePicker by remember { mutableStateOf(false) }
    var showHistory by remember { mutableStateOf(false) }
    var showSettings by remember { mutableStateOf(false) }
    var completedFasts by remember { mutableStateOf(SessionManager.loadCompletedFasts(context)) }

    if (showHistory) {
        HistoryScreen(
            completedFasts = completedFasts,
            onBack = { showHistory = false },
            onClearHistory = {
                completedFasts = emptyList()
                SessionManager.saveCompletedFasts(context, emptyList())
            },
            onDeleteFast = { id ->
                completedFasts = completedFasts.filter { it.id != id }
                SessionManager.saveCompletedFasts(context, completedFasts)
            }
        )
        return
    }

    if (showSettings) {
        SettingsScreen(onBack = { showSettings = false })
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
                .verticalScroll(rememberScrollState())
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 48.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = AppColors.primaryText
                    )
                    Text(
                        text = stringResource(R.string.app_subtitle),
                        fontSize = 13.sp,
                        color = AppColors.secondaryText
                    )
                }
                Text(
                    text = "⚙️",
                    fontSize = 24.sp,
                    modifier = Modifier.clickable { showSettings = true }
                )
                Text(
                    text = "⏱",
                    fontSize = 24.sp,
                    color = AppColors.brightRed,
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable { showHistory = true }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (activeSession != null) {
                ActiveSessionContent(
                    session = activeSession!!,
                    now = now,
                    onStop = {
                        val newFasts = listOf(CompletedFast.fromSession(activeSession!!)) + completedFasts
                        completedFasts = newFasts
                        activeSession = null
                        SessionManager.saveSession(context, null)
                        SessionManager.saveCompletedFasts(context, newFasts)
                    },
                    onExtend = { duration ->
                        activeSession = activeSession?.extendBy(duration)
                        SessionManager.saveSession(context, activeSession)
                    }
                )
            } else {
                StartFastContent(
                    onStartFast = { duration ->
                        val newSession = FastingSession.startNow(plannedDuration = duration)
                        activeSession = newSession
                        SessionManager.saveSession(context, newSession)
                    },
                    onShowCustomPicker = { showCustomPicker = true },
                    onAlreadyStarted = { showRetroactivePicker = true }
                )
            }
        }
    }

    if (showCustomPicker) {
        CustomDurationPickerDialog(
            onDismiss = { showCustomPicker = false },
            onConfirm = { duration ->
                val newSession = FastingSession.startNow(plannedDuration = duration)
                activeSession = newSession
                SessionManager.saveSession(context, newSession)
                showCustomPicker = false
            }
        )
    }

    if (showRetroactivePicker) {
        RetroactiveStartTimePickerDialog(
            onDismiss = { showRetroactivePicker = false },
            onConfirm = { startTime ->
                val newSession = FastingSession.startRetroactive(
                    startedAt = startTime,
                    plannedDuration = Duration.ofHours(16)
                )
                activeSession = newSession
                SessionManager.saveSession(context, newSession)
                showRetroactivePicker = false
            }
        )
    }
}

@Composable
private fun ActiveSessionContent(
    session: FastingSession,
    now: Instant,
    onStop: () -> Unit,
    onExtend: (Duration) -> Unit
) {
    val progress = remember(now, session) {
        FastingProgressCalculator.compute(session = session, now = now)
    }
    val isRetroactive = session.startMode == FastingSessionStartMode.RETROACTIVE
    val ringTime = if (isRetroactive) progress.elapsed else progress.remaining
    val ringSubtitle = if (isRetroactive) {
        stringResource(R.string.dashboard_ring_custom_time)
    } else {
        stringResource(R.string.dashboard_ring_remaining)
    }
    val plannedLabel = if (isRetroactive) {
        stringResource(R.string.dashboard_planned_custom_time)
    } else {
        formatPlannedDuration(session.plannedDuration)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        ProgressRing(
            progress = progress.progress01,
            size = 220.dp,
            strokeWidth = 14.dp
        )
        
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = formatDuration(ringTime),
                fontSize = 42.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.primaryText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = ringSubtitle,
                fontSize = 14.sp,
                color = AppColors.secondaryText
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = plannedLabel,
                fontSize = 12.sp,
                color = AppColors.brightRed
            )
        }
    }

    GlassCard {
        Text(
            text = stringResource(R.string.dashboard_elapsed_format, formatDuration(progress.elapsed)),
            fontSize = 16.sp,
            color = AppColors.primaryText
        )
    }

    GlassCard {
        Text(
            text = stringResource(R.string.dashboard_body_title),
            fontSize = 14.sp,
            color = AppColors.secondaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(progress.currentStage.titleRes),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.primaryAccent
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = progress.currentStage.descriptionLineRes.map { stringResource(it) }.joinToString("\n"),
            fontSize = 15.sp,
            color = AppColors.primaryText.copy(alpha = 0.9f)
        )
    }

    if (progress.nextStage != null) {
        GlassCard {
            Text(
                text = stringResource(R.string.dashboard_next_prefix) + " " + stringResource(progress.nextStage.titleRes),
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = AppColors.secondaryAccent
            )
        }
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        GlassPillButton(
            text = stringResource(R.string.dashboard_extend_hours_format, 1),
            onClick = { onExtend(Duration.ofHours(1)) },
            modifier = Modifier.weight(1f)
        )
        GlassPillButton(
            text = stringResource(R.string.dashboard_extend_hours_format, 6),
            onClick = { onExtend(Duration.ofHours(6)) },
            modifier = Modifier.weight(1f)
        )
        GlassPillButton(
            text = stringResource(R.string.common_stop),
            isDestructive = true,
            onClick = onStop,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun StartFastContent(
    onStartFast: (Duration) -> Unit,
    onShowCustomPicker: () -> Unit,
    onAlreadyStarted: () -> Unit
) {
    GlassCard {
        Text(
            text = stringResource(R.string.dashboard_start_title),
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = AppColors.primaryText
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.dashboard_start_subtitle),
            fontSize = 14.sp,
            color = AppColors.secondaryText
        )
    }

    val presetPairs = listOf(12 to 16, 18 to 24, 36 to 48)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        presetPairs.forEach { (left, right) ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                GlassPillButton(
                    text = stringResource(R.string.dashboard_hours_format, left),
                    isPrimary = true,
                    onClick = { onStartFast(Duration.ofHours(left.toLong())) },
                    modifier = Modifier.weight(1f)
                )
                GlassPillButton(
                    text = stringResource(R.string.dashboard_hours_format, right),
                    isPrimary = true,
                    onClick = { onStartFast(Duration.ofHours(right.toLong())) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // 72h and Custom side by side
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            GlassPillButton(
                text = stringResource(R.string.dashboard_hours_format, 72),
                isPrimary = true,
                onClick = { onStartFast(Duration.ofHours(72)) },
                modifier = Modifier.weight(1f)
            )
            GlassPillButton(
                text = stringResource(R.string.common_custom),
                isPrimary = true,
                onClick = onShowCustomPicker,
                modifier = Modifier.weight(1f)
            )
        }
    }

    Spacer(modifier = Modifier.height(8.dp))

    GlassPillButton(
        text = stringResource(R.string.dashboard_already_started_custom),
        onClick = onAlreadyStarted,
        modifier = Modifier.fillMaxWidth()
    )
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
