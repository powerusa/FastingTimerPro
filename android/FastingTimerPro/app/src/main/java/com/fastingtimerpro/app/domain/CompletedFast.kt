package com.fastingtimerpro.app.domain

import java.time.Duration
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

data class CompletedFast(
    val id: String,
    val startTime: Instant,
    val endTime: Instant,
    val plannedDuration: Duration,
    val actualDuration: Duration,
    val wasCompleted: Boolean
) {
    val formattedDate: String
        get() {
            val formatter = DateTimeFormatter.ofPattern("MMM d, yyyy h:mm a")
                .withZone(ZoneId.systemDefault())
            return formatter.format(startTime)
        }

    val formattedActualDuration: String
        get() {
            val hours = actualDuration.toHours()
            val minutes = (actualDuration.toMinutes() % 60)
            return if (hours >= 24) {
                val days = hours / 24
                val remainingHours = hours % 24
                "${days}d ${remainingHours}h ${minutes}m"
            } else {
                "${hours}h ${minutes}m"
            }
        }

    val formattedPlannedDuration: String
        get() {
            val hours = plannedDuration.toHours()
            return if (hours >= 24) {
                val days = hours / 24
                val remainingHours = hours % 24
                if (remainingHours > 0) "${days}d ${remainingHours}h" else "${days}d"
            } else {
                "${hours}h"
            }
        }

    companion object {
        fun fromSession(session: FastingSession, endedAt: Instant = Instant.now()): CompletedFast {
            val actualDuration = Duration.between(session.startTime, endedAt)
            val wasCompleted = actualDuration >= session.plannedDuration

            return CompletedFast(
                id = session.id,
                startTime = session.startTime,
                endTime = endedAt,
                plannedDuration = session.plannedDuration,
                actualDuration = actualDuration,
                wasCompleted = wasCompleted
            )
        }
    }
}
