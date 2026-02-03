package com.fastingtimerpro.app.domain

import com.fastingtimerpro.app.R

object FastingStages {

    fun defaultStages(): List<FastingStage> {
        return listOf(
            FastingStage(
                startHours = 0.0,
                endHours = 4.0,
                titleRes = R.string.stages_digesting_title,
                descriptionLineRes = listOf(
                    R.string.stages_digesting_line1,
                    R.string.stages_digesting_line2,
                    R.string.stages_digesting_line3,
                    R.string.stages_digesting_line4
                )
            ),
            FastingStage(
                startHours = 4.0,
                endHours = 12.0,
                titleRes = R.string.stages_blood_sugar_title,
                descriptionLineRes = listOf(
                    R.string.stages_blood_sugar_line1,
                    R.string.stages_blood_sugar_line2,
                    R.string.stages_blood_sugar_line3,
                    R.string.stages_blood_sugar_line4
                )
            ),
            FastingStage(
                startHours = 12.0,
                endHours = 18.0,
                titleRes = R.string.stages_fat_burning_title,
                descriptionLineRes = listOf(
                    R.string.stages_fat_burning_line1,
                    R.string.stages_fat_burning_line2,
                    R.string.stages_fat_burning_line3,
                    R.string.stages_fat_burning_line4
                )
            ),
            FastingStage(
                startHours = 18.0,
                endHours = 24.0,
                titleRes = R.string.stages_metabolic_switching_title,
                descriptionLineRes = listOf(
                    R.string.stages_metabolic_switching_line1,
                    R.string.stages_metabolic_switching_line2,
                    R.string.stages_metabolic_switching_line3,
                    R.string.stages_metabolic_switching_line4
                )
            ),
            FastingStage(
                startHours = 24.0,
                endHours = 36.0,
                titleRes = R.string.stages_ketosis_title,
                descriptionLineRes = listOf(
                    R.string.stages_ketosis_line1,
                    R.string.stages_ketosis_line2,
                    R.string.stages_ketosis_line3,
                    R.string.stages_ketosis_line4
                )
            ),
            FastingStage(
                startHours = 36.0,
                endHours = 48.0,
                titleRes = R.string.stages_repair_mode_title,
                descriptionLineRes = listOf(
                    R.string.stages_repair_mode_line1,
                    R.string.stages_repair_mode_line2,
                    R.string.stages_repair_mode_line3,
                    R.string.stages_repair_mode_line4
                )
            ),
            FastingStage(
                startHours = 48.0,
                endHours = 72.0,
                titleRes = R.string.stages_deep_fasting_title,
                descriptionLineRes = listOf(
                    R.string.stages_deep_fasting_line1,
                    R.string.stages_deep_fasting_line2,
                    R.string.stages_deep_fasting_line3,
                    R.string.stages_deep_fasting_line4
                )
            ),
            FastingStage(
                startHours = 72.0,
                endHours = null,
                titleRes = R.string.stages_extended_title,
                descriptionLineRes = listOf(
                    R.string.stages_extended_line1,
                    R.string.stages_extended_line2,
                    R.string.stages_extended_line3,
                    R.string.stages_extended_line4
                ),
                isExtendedTrackingOnly = true
            )
        )
    }
}
