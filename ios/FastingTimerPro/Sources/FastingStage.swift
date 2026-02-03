import Foundation

struct FastingStage: Identifiable, Equatable {
    let id: String
    let startHours: Double
    let endHours: Double?
    let titleKey: String
    let descriptionKeys: [String]
    let isExtendedTrackingOnly: Bool

    init(
        id: String = UUID().uuidString,
        startHours: Double,
        endHours: Double?,
        titleKey: String,
        descriptionKeys: [String],
        isExtendedTrackingOnly: Bool = false
    ) {
        self.id = id
        self.startHours = startHours
        self.endHours = endHours
        self.titleKey = titleKey
        self.descriptionKeys = descriptionKeys
        self.isExtendedTrackingOnly = isExtendedTrackingOnly
    }
}
