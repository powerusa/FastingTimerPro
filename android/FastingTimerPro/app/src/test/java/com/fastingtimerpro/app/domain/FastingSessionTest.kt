package com.fastingtimerpro.app.domain

import java.time.Duration
import java.time.Instant
import org.junit.Assert.*
import org.junit.Test

class FastingSessionTest {
    private val start = Instant.parse("2026-10-15T12:00:00Z")

    @Test fun activeFastKeepsElapsedTimeAfterRecreation() {
        val session = FastingSession.startNow(Duration.ofHours(16), start)
        val restored = session.copy()
        val progress = FastingProgressCalculator.compute(restored, start.plus(Duration.ofHours(4)))
        assertEquals(Duration.ofHours(4), progress.elapsed)
        assertEquals(Duration.ofHours(12), progress.remaining)
        assertEquals(0.25f, progress.progress01, 0.0001f)
    }

    @Test fun extendingFastKeepsStartAndMovesTarget() {
        val session = FastingSession.startNow(Duration.ofHours(16), start)
        val extended = session.extendBy(Duration.ofHours(6))
        assertEquals(session.id, extended.id)
        assertEquals(start, extended.startTime)
        assertEquals(Duration.ofHours(22), extended.plannedDuration)
        assertEquals(start.plus(Duration.ofHours(22)), extended.endTime)
    }

    @Test fun retroactiveFastCountsFromSelectedStart() {
        val session = FastingSession.startRetroactive(start, Duration.ofHours(16), start.plus(Duration.ofHours(4)))
        val progress = FastingProgressCalculator.compute(session, start.plus(Duration.ofHours(4)))
        assertEquals(FastingSessionStartMode.RETROACTIVE, session.startMode)
        assertEquals(Duration.ofHours(4), progress.elapsed)
        assertEquals(Duration.ofHours(12), progress.remaining)
    }

    @Test fun overdueFastHasNoNegativeRemainingTime() {
        val session = FastingSession.startNow(Duration.ofHours(16), start)
        val progress = FastingProgressCalculator.compute(session, start.plus(Duration.ofHours(20)))
        assertEquals(Duration.ZERO, progress.remaining)
        assertEquals(Duration.ofHours(20), progress.elapsed)
        assertEquals(1f, progress.progress01, 0f)
        assertTrue(CompletedFast.fromSession(session, start.plus(Duration.ofHours(20))).wasCompleted)
    }

    @Test fun stoppingEarlyRecordsActualDuration() {
        val session = FastingSession.startNow(Duration.ofHours(16), start)
        val completed = CompletedFast.fromSession(session, start.plus(Duration.ofHours(2)))
        assertFalse(completed.wasCompleted)
        assertEquals(Duration.ofHours(2), completed.actualDuration)
        assertEquals(Duration.ofHours(16), completed.plannedDuration)
    }
}
