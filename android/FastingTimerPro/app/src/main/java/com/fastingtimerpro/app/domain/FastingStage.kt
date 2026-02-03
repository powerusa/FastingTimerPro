package com.fastingtimerpro.app.domain

import androidx.annotation.StringRes

data class FastingStage(
    val startHours: Double,
    val endHours: Double?,
    @StringRes val titleRes: Int,
    @StringRes val descriptionLineRes: List<Int>,
    val isExtendedTrackingOnly: Boolean = false
)
