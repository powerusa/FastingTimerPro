package com.fastingtimerpro.app.domain

object FastingStages {

    fun defaultStages(): List<FastingStage> {
        return listOf(
            FastingStage(
                startHours = 0.0,
                endHours = 4.0,
                title = "Digesting",
                descriptionLines = listOf(
                    "Your body is digesting the last meal.",
                    "Blood sugar and insulin are elevated.",
                    "Energy primarily comes from food intake.",
                    "This phase is about digestion and nutrient absorption."
                )
            ),
            FastingStage(
                startHours = 4.0,
                endHours = 12.0,
                title = "Blood Sugar Stabilization",
                descriptionLines = listOf(
                    "Insulin levels begin to drop.",
                    "Blood sugar becomes more stable.",
                    "The body starts transitioning from fed to fasted state.",
                    "Hunger may appear as insulin decreases."
                )
            ),
            FastingStage(
                startHours = 12.0,
                endHours = 18.0,
                title = "Fat Burning Begins",
                descriptionLines = listOf(
                    "Glycogen (stored sugar) levels decrease.",
                    "The body increasingly uses fat for energy.",
                    "Growth hormone levels begin to rise.",
                    "Many people notice improved mental focus."
                )
            ),
            FastingStage(
                startHours = 18.0,
                endHours = 24.0,
                title = "Metabolic Switching",
                descriptionLines = listOf(
                    "Fat burning becomes more efficient.",
                    "Ketone production begins to increase.",
                    "Insulin remains low.",
                    "Energy may feel steady and calm."
                )
            ),
            FastingStage(
                startHours = 24.0,
                endHours = 36.0,
                title = "Ketosis & Cellular Cleanup",
                descriptionLines = listOf(
                    "Ketosis deepens.",
                    "The body relies more heavily on fat and ketones.",
                    "Cellular recycling processes increase.",
                    "Inflammation-related processes may reduce."
                )
            ),
            FastingStage(
                startHours = 36.0,
                endHours = 48.0,
                title = "Increased Repair Mode",
                descriptionLines = listOf(
                    "Growth hormone levels are elevated.",
                    "Cellular maintenance processes continue.",
                    "Fat utilization remains high.",
                    "Many users report mental clarity and calm focus."
                )
            ),
            FastingStage(
                startHours = 48.0,
                endHours = 72.0,
                title = "Deep Fasting State",
                descriptionLines = listOf(
                    "The body adapts to prolonged fasting.",
                    "Energy efficiency improves.",
                    "Cellular renewal processes remain active.",
                    "Fat stores are the primary energy source."
                )
            ),
            FastingStage(
                startHours = 72.0,
                endHours = null,
                title = "Extended Fasting (Tracking Only)",
                descriptionLines = listOf(
                    "The body operates in a long-term fasting state.",
                    "Energy use stabilizes.",
                    "Changes occur more slowly and steadily.",
                    "This app provides educational tracking only."
                ),
                isExtendedTrackingOnly = true
            )
        )
    }
}
