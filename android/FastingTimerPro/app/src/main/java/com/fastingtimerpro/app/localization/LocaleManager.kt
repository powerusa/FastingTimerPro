package com.fastingtimerpro.app.localization

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleManager {

    private const val PREFS_NAME = "locale_prefs"
    private const val KEY_OVERRIDE = "language_override"

    private val supportedLocales = listOf(
        "en", "es-ES", "es-419", "pt-BR", "de", "fr", "ja", "ko", "zh-Hans", "pl"
    )

    fun getOverride(context: Context): String? {
        return prefs(context).getString(KEY_OVERRIDE, null)
    }

    fun setOverride(context: Context, localeId: String?) {
        prefs(context).edit().apply {
            if (localeId == null) {
                remove(KEY_OVERRIDE)
            } else {
                putString(KEY_OVERRIDE, localeId)
            }
            apply()
        }
        applyLocale(context)
    }

    fun applyLocale(context: Context) {
        val override = getOverride(context)
        val effectiveLocale = if (override != null) {
            localeFromId(override)
        } else {
            mapDeviceLocale()
        }
        val localeList = LocaleListCompat.create(effectiveLocale)
        AppCompatDelegate.setApplicationLocales(localeList)
    }

    fun effectiveLocaleId(context: Context): String {
        val override = getOverride(context)
        if (override != null) return override
        return mapDeviceLocaleId()
    }

    private fun mapDeviceLocale(): Locale {
        val id = mapDeviceLocaleId()
        return localeFromId(id)
    }

    private fun mapDeviceLocaleId(): String {
        val deviceLocale = Locale.getDefault()
        val lang = deviceLocale.language
        val country = deviceLocale.country
        val script = deviceLocale.script

        return when {
            lang == "es" && country == "ES" -> "es-ES"
            lang == "es" -> "es-419"
            lang == "pt" && country == "BR" -> "pt-BR"
            lang == "pt" -> "pt-BR"
            lang == "zh" && (script == "Hans" || country in listOf("CN", "SG")) -> "zh-Hans"
            lang == "zh" -> "zh-Hans"
            lang == "de" -> "de"
            lang == "fr" -> "fr"
            lang == "ja" -> "ja"
            lang == "ko" -> "ko"
            lang == "pl" -> "pl"
            else -> "en"
        }
    }

    private fun localeFromId(id: String): Locale {
        return when (id) {
            "es-ES" -> Locale("es", "ES")
            "es-419" -> Locale("es", "419")
            "pt-BR" -> Locale("pt", "BR")
            "zh-Hans" -> Locale.Builder().setLanguage("zh").setScript("Hans").build()
            "de" -> Locale("de")
            "fr" -> Locale("fr")
            "ja" -> Locale("ja")
            "ko" -> Locale("ko")
            "pl" -> Locale("pl")
            else -> Locale("en")
        }
    }

    fun getSupportedLocales(): List<Pair<String, String>> {
        return listOf(
            "en" to "English",
            "es-ES" to "Español (España)",
            "es-419" to "Español (Latinoamérica)",
            "pt-BR" to "Português (Brasil)",
            "de" to "Deutsch",
            "fr" to "Français",
            "ja" to "日本語",
            "ko" to "한국어",
            "zh-Hans" to "简体中文",
            "pl" to "Polski"
        )
    }

    private fun prefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
}
