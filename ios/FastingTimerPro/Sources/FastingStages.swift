import Foundation

enum FastingStages {

    static func `default`() -> [FastingStage] {
        [
            FastingStage(
                startHours: 0,
                endHours: 4,
                titleKey: "stages.digesting.title",
                descriptionKeys: [
                    "stages.digesting.line1",
                    "stages.digesting.line2",
                    "stages.digesting.line3",
                    "stages.digesting.line4"
                ]
            ),
            FastingStage(
                startHours: 4,
                endHours: 12,
                titleKey: "stages.blood_sugar.title",
                descriptionKeys: [
                    "stages.blood_sugar.line1",
                    "stages.blood_sugar.line2",
                    "stages.blood_sugar.line3",
                    "stages.blood_sugar.line4"
                ]
            ),
            FastingStage(
                startHours: 12,
                endHours: 18,
                titleKey: "stages.fat_burning.title",
                descriptionKeys: [
                    "stages.fat_burning.line1",
                    "stages.fat_burning.line2",
                    "stages.fat_burning.line3",
                    "stages.fat_burning.line4"
                ]
            ),
            FastingStage(
                startHours: 18,
                endHours: 24,
                titleKey: "stages.metabolic_switching.title",
                descriptionKeys: [
                    "stages.metabolic_switching.line1",
                    "stages.metabolic_switching.line2",
                    "stages.metabolic_switching.line3",
                    "stages.metabolic_switching.line4"
                ]
            ),
            FastingStage(
                startHours: 24,
                endHours: 36,
                titleKey: "stages.ketosis.title",
                descriptionKeys: [
                    "stages.ketosis.line1",
                    "stages.ketosis.line2",
                    "stages.ketosis.line3",
                    "stages.ketosis.line4"
                ]
            ),
            FastingStage(
                startHours: 36,
                endHours: 48,
                titleKey: "stages.repair_mode.title",
                descriptionKeys: [
                    "stages.repair_mode.line1",
                    "stages.repair_mode.line2",
                    "stages.repair_mode.line3",
                    "stages.repair_mode.line4"
                ]
            ),
            FastingStage(
                startHours: 48,
                endHours: 72,
                titleKey: "stages.deep_fasting.title",
                descriptionKeys: [
                    "stages.deep_fasting.line1",
                    "stages.deep_fasting.line2",
                    "stages.deep_fasting.line3",
                    "stages.deep_fasting.line4"
                ]
            ),
            FastingStage(
                startHours: 72,
                endHours: nil,
                titleKey: "stages.extended.title",
                descriptionKeys: [
                    "stages.extended.line1",
                    "stages.extended.line2",
                    "stages.extended.line3",
                    "stages.extended.line4"
                ],
                isExtendedTrackingOnly: true
            )
        ]
    }
}
