import Foundation

struct FastingStage: Identifiable, Equatable {
    let id: String
    let startHours: Double
    let endHours: Double?
    let title: String
    let descriptionLines: [String]
    let isExtendedTrackingOnly: Bool

    init(
        id: String = UUID().uuidString,
        startHours: Double,
        endHours: Double?,
        title: String,
        descriptionLines: [String],
        isExtendedTrackingOnly: Bool = false
    ) {
        self.id = id
        self.startHours = startHours
        self.endHours = endHours
        self.title = title
        self.descriptionLines = descriptionLines
        self.isExtendedTrackingOnly = isExtendedTrackingOnly
    }
}
