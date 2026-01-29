package com.fastingtimerpro.app.domain

import java.time.Duration
import java.time.Instant

data class FastingSession(
    val id: String,
    val startTime: Instant,
    val plannedDuration: Duration,
    val endTime: Instant,
    val isActive: Boolean
) {
    companion object {
        fun startNow(plannedDuration: Duration, now: Instant = Instant.now()): FastingSession {
            val id = now.toEpochMilli().toString()
            return FastingSession(
                id = id,
                startTime = now,
                plannedDuration = plannedDuration,
                endTime = now.plus(plannedDuration),
                isActive = true
            )
        }

        fun startRetroactive(
            startedAt: Instant,
            plannedDuration: Duration,
            now: Instant = Instant.now()
        ): FastingSession {
            val id = now.toEpochMilli().toString()
            return FastingSession(
                id = id,
                startTime = startedAt,
                plannedDuration = plannedDuration,
                endTime = startedAt.plus(plannedDuration),
                isActive = true
            )
        }
    }

    fun extendBy(extra: Duration): FastingSession {
        if (extra.isZero || extra.isNegative) return this
        val newPlanned = plannedDuration.plus(extra)
        return copy(
            plannedDuration = newPlanned,
            endTime = startTime.plus(newPlanned)
        )
    }
}
