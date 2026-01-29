package com.fastingtimerpro.app.domain

data class FastingStage(
    val startHours: Double,
    val endHours: Double?,
    val title: String,
    val descriptionLines: List<String>,
    val isExtendedTrackingOnly: Boolean = false
)
