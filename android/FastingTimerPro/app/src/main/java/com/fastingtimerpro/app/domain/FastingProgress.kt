package com.fastingtimerpro.app.domain

import java.time.Duration
import java.time.Instant

data class FastingProgress(
    val elapsed: Duration,
    val remaining: Duration,
    val progress01: Float,
    val currentStage: FastingStage,
    val nextStage: FastingStage?
)

object FastingProgressCalculator {

    fun compute(
        session: FastingSession,
        now: Instant,
        stages: List<FastingStage> = FastingStages.defaultStages()
    ): FastingProgress {
        val rawElapsed = Duration.between(session.startTime, now)
        val elapsed = if (rawElapsed.isNegative) Duration.ZERO else rawElapsed

        val total = if (session.plannedDuration < Duration.ofMinutes(1)) {
            Duration.ofMinutes(1)
        } else {
            session.plannedDuration
        }

        val rawRemaining = Duration.between(now, session.endTime)
        val remaining = if (rawRemaining.isNegative) Duration.ZERO else rawRemaining
        val progress01 = (elapsed.toMillis().toDouble() / total.toMillis().toDouble())
            .coerceIn(0.0, 1.0)
            .toFloat()

        val elapsedHours = elapsed.toMinutes().toDouble() / 60.0
        val current = stages.lastOrNull { stage ->
            elapsedHours >= stage.startHours && (stage.endHours == null || elapsedHours < stage.endHours)
        } ?: stages.first()

        val next = stages.firstOrNull { stage ->
            stage.startHours > current.startHours
        }

        return FastingProgress(
            elapsed = elapsed,
            remaining = remaining,
            progress01 = progress01,
            currentStage = current,
            nextStage = next
        )
    }
}
