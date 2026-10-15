package com.fastingtimerpro.app

import android.os.Bundle
import android.os.Build
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.fastingtimerpro.app.localization.LocaleManager
import com.fastingtimerpro.app.ui.screens.DashboardScreen
import com.fastingtimerpro.app.ui.theme.AppColors
import com.fastingtimerpro.app.ui.theme.FastingTimerProTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (Build.VERSION.SDK_INT <= 32) LocaleManager.applyLocale(this)
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= 33) LocaleManager.applyLocale(this)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(Color.TRANSPARENT)
        )
        setContent {
            FastingTimerProTheme {
                AppRoot()
            }
        }
    }
}

@Composable
private fun AppRoot() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppColors.baseBackground)
            .safeDrawingPadding()
    ) {
        DashboardScreen()
    }
}
