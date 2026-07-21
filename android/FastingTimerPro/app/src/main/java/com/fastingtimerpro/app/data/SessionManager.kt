package com.fastingtimerpro.app.data

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.fastingtimerpro.app.domain.CompletedFast
import com.fastingtimerpro.app.domain.FastingSession
import com.fastingtimerpro.app.domain.FastingSessionStartMode
import java.time.Duration
import java.time.Instant

object SessionManager {

    private const val PREFS_NAME = "fasting_session_prefs"
    private const val KEY_SESSION_ID = "session_id"
    private const val KEY_START_TIME = "start_time"
    private const val KEY_PLANNED_DURATION = "planned_duration"
    private const val KEY_END_TIME = "end_time"
    private const val KEY_IS_ACTIVE = "is_active"
    private const val KEY_START_MODE = "start_mode"
    private const val KEY_COMPLETED_FASTS = "completed_fasts"

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    fun saveSession(context: Context, session: FastingSession?) {
        prefs(context).edit {
            if (session == null) {
                remove(KEY_SESSION_ID)
                remove(KEY_START_TIME)
                remove(KEY_PLANNED_DURATION)
                remove(KEY_END_TIME)
                remove(KEY_IS_ACTIVE)
                remove(KEY_START_MODE)
            } else {
                putString(KEY_SESSION_ID, session.id)
                putLong(KEY_START_TIME, session.startTime.toEpochMilli())
                putLong(KEY_PLANNED_DURATION, session.plannedDuration.toMillis())
                putLong(KEY_END_TIME, session.endTime.toEpochMilli())
                putBoolean(KEY_IS_ACTIVE, session.isActive)
                putString(KEY_START_MODE, session.startMode.name)
            }
        }
    }

    fun loadSession(context: Context): FastingSession? {
        val prefs = prefs(context)
        val sessionId = prefs.getString(KEY_SESSION_ID, null) ?: return null
        val startTime = prefs.getLong(KEY_START_TIME, 0L)
        val plannedDuration = prefs.getLong(KEY_PLANNED_DURATION, 0L)
        val endTime = prefs.getLong(KEY_END_TIME, 0L)
        val isActive = prefs.getBoolean(KEY_IS_ACTIVE, false)
        val startModeName = prefs.getString(KEY_START_MODE, "NOW") ?: "NOW"

        if (startTime == 0L || plannedDuration == 0L) return null

        return FastingSession(
            id = sessionId,
            startTime = Instant.ofEpochMilli(startTime),
            plannedDuration = Duration.ofMillis(plannedDuration),
            endTime = Instant.ofEpochMilli(endTime),
            isActive = isActive,
            startMode = FastingSessionStartMode.valueOf(startModeName)
        )
    }

    fun saveCompletedFasts(context: Context, fasts: List<CompletedFast>) {
        val json = fasts.joinToString("|") { fast ->
            "${fast.id},${fast.startTime.toEpochMilli()},${fast.endTime.toEpochMilli()},${fast.plannedDuration.toMillis()},${fast.actualDuration.toMillis()},${fast.wasCompleted}"
        }
        prefs(context).edit { putString(KEY_COMPLETED_FASTS, json) }
    }

    fun loadCompletedFasts(context: Context): List<CompletedFast> {
        val prefs = prefs(context)
        val json = prefs.getString(KEY_COMPLETED_FASTS, null) ?: return emptyList()
        if (json.isEmpty()) return emptyList()

        return try {
            json.split("|").mapNotNull { entry ->
                val parts = entry.split(",")
                if (parts.size >= 6) {
                    CompletedFast(
                        id = parts[0],
                        startTime = Instant.ofEpochMilli(parts[1].toLong()),
                        endTime = Instant.ofEpochMilli(parts[2].toLong()),
                        plannedDuration = Duration.ofMillis(parts[3].toLong()),
                        actualDuration = Duration.ofMillis(parts[4].toLong()),
                        wasCompleted = parts[5].toBoolean()
                    )
                } else null
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
