import Foundation

enum FastingStages {

    static func `default`() -> [FastingStage] {
        [
            FastingStage(
                startHours: 0,
                endHours: 4,
                title: "Digesting",
                descriptionLines: [
                    "Your body is digesting the last meal.",
                    "Blood sugar and insulin are elevated.",
                    "Energy primarily comes from food intake.",
                    "This phase is about digestion and nutrient absorption."
                ]
            ),
            FastingStage(
                startHours: 4,
                endHours: 12,
                title: "Blood Sugar Stabilization",
                descriptionLines: [
                    "Insulin levels begin to drop.",
                    "Blood sugar becomes more stable.",
                    "The body starts transitioning from fed to fasted state.",
                    "Hunger may appear as insulin decreases."
                ]
            ),
            FastingStage(
                startHours: 12,
                endHours: 18,
                title: "Fat Burning Begins",
                descriptionLines: [
                    "Glycogen (stored sugar) levels decrease.",
                    "The body increasingly uses fat for energy.",
                    "Growth hormone levels begin to rise.",
                    "Many people notice improved mental focus."
                ]
            ),
            FastingStage(
                startHours: 18,
                endHours: 24,
                title: "Metabolic Switching",
                descriptionLines: [
                    "Fat burning becomes more efficient.",
                    "Ketone production begins to increase.",
                    "Insulin remains low.",
                    "Energy may feel steady and calm."
                ]
            ),
            FastingStage(
                startHours: 24,
                endHours: 36,
                title: "Ketosis & Cellular Cleanup",
                descriptionLines: [
                    "Ketosis deepens.",
                    "The body relies more heavily on fat and ketones.",
                    "Cellular recycling processes increase.",
                    "Inflammation-related processes may reduce."
                ]
            ),
            FastingStage(
                startHours: 36,
                endHours: 48,
                title: "Increased Repair Mode",
                descriptionLines: [
                    "Growth hormone levels are elevated.",
                    "Cellular maintenance processes continue.",
                    "Fat utilization remains high.",
                    "Many users report mental clarity and calm focus."
                ]
            ),
            FastingStage(
                startHours: 48,
                endHours: 72,
                title: "Deep Fasting State",
                descriptionLines: [
                    "The body adapts to prolonged fasting.",
                    "Energy efficiency improves.",
                    "Cellular renewal processes remain active.",
                    "Fat stores are the primary energy source."
                ]
            ),
            FastingStage(
                startHours: 72,
                endHours: nil,
                title: "Extended Fasting (Tracking Only)",
                descriptionLines: [
                    "The body operates in a long-term fasting state.",
                    "Energy use stabilizes.",
                    "Changes occur more slowly and steadily.",
                    "This app provides educational tracking only."
                ],
                isExtendedTrackingOnly: true
            )
        ]
    }
}
