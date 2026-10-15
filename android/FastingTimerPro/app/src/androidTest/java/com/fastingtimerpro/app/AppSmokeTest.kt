package com.fastingtimerpro.app

import android.content.Intent
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Test
import org.junit.Assert.*
import androidx.appcompat.app.AppCompatActivity
import com.fastingtimerpro.app.data.SessionManager
import com.fastingtimerpro.app.domain.FastingSession
import com.fastingtimerpro.app.domain.CompletedFast
import com.fastingtimerpro.app.localization.LocaleManager
import java.time.Duration
import java.time.Instant

@Suppress("DEPRECATION")
class AppSmokeTest {
    private val instrumentation get() = InstrumentationRegistry.getInstrumentation()
    @Test fun testSessionAndHistorySurviveStorageRoundTrip() {
        val context = instrumentation.targetContext
        val previous = SessionManager.loadSession(context)
        val oldHistory = SessionManager.loadCompletedFasts(context)
        val start = Instant.parse("2026-10-15T12:00:00Z")
        try {
            val session = FastingSession.startRetroactive(start, Duration.ofHours(16), start.plusSeconds(3600))
            SessionManager.saveSession(context, session)
            assertEquals(session, SessionManager.loadSession(context))
            val completed = CompletedFast.fromSession(session, start.plus(Duration.ofHours(17)))
            SessionManager.saveCompletedFasts(context, listOf(completed))
            assertEquals(listOf(completed), SessionManager.loadCompletedFasts(context))
            SessionManager.saveSession(context, null)
            assertNull(SessionManager.loadSession(context))
        } finally {
            SessionManager.saveSession(context, previous)
            SessionManager.saveCompletedFasts(context, oldHistory)
        }
    }

    @Test fun testActivityLaunchesWithPolishOverride() {
        val context = instrumentation.targetContext
        val previous = LocaleManager.getOverride(context)
        var activity: android.app.Activity? = null
        try {
            instrumentation.runOnMainSync { LocaleManager.setOverride(context, "pl") }
            val intent = Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            val launched = instrumentation.startActivitySync(intent)
            activity = launched
            instrumentation.waitForIdleSync()
            assertTrue(launched is AppCompatActivity)
            assertFalse(launched.isFinishing)
            assertEquals("pl", launched.resources.configuration.locales[0].language)
            assertEquals("Ustawienia", launched.getString(R.string.settings_title))
        } finally {
            instrumentation.runOnMainSync {
                activity?.finish()
                LocaleManager.setOverride(context, previous)
            }
        }
    }
}
