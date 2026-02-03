import Foundation

enum FastingSessionStartMode: String, Codable {
    case now
    case retroactive
}

struct FastingSession: Identifiable, Codable, Equatable {
    let id: String
    let startTime: Date
    let plannedDuration: TimeInterval
    let endTime: Date
    let isActive: Bool
    let startMode: FastingSessionStartMode

    enum CodingKeys: String, CodingKey {
        case id
        case startTime
        case plannedDuration
        case endTime
        case isActive
        case startMode
    }

    init(from decoder: any Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        id = try container.decode(String.self, forKey: .id)
        startTime = try container.decode(Date.self, forKey: .startTime)
        plannedDuration = try container.decode(TimeInterval.self, forKey: .plannedDuration)
        endTime = try container.decode(Date.self, forKey: .endTime)
        isActive = try container.decode(Bool.self, forKey: .isActive)
        startMode = try container.decodeIfPresent(FastingSessionStartMode.self, forKey: .startMode) ?? .now
    }

    func encode(to encoder: any Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        try container.encode(id, forKey: .id)
        try container.encode(startTime, forKey: .startTime)
        try container.encode(plannedDuration, forKey: .plannedDuration)
        try container.encode(endTime, forKey: .endTime)
        try container.encode(isActive, forKey: .isActive)
        try container.encode(startMode, forKey: .startMode)
    }

    init(
        id: String,
        startTime: Date,
        plannedDuration: TimeInterval,
        endTime: Date,
        isActive: Bool,
        startMode: FastingSessionStartMode
    ) {
        self.id = id
        self.startTime = startTime
        self.plannedDuration = plannedDuration
        self.endTime = endTime
        self.isActive = isActive
        self.startMode = startMode
    }

    static func startNow(plannedDuration: TimeInterval, now: Date = Date()) -> FastingSession {
        let id = String(Int(now.timeIntervalSince1970 * 1000))
        return FastingSession(
            id: id,
            startTime: now,
            plannedDuration: plannedDuration,
            endTime: now.addingTimeInterval(plannedDuration),
            isActive: true,
            startMode: .now
        )
    }

    static func startRetroactive(startedAt: Date, plannedDuration: TimeInterval, now: Date = Date()) -> FastingSession {
        let id = String(Int(now.timeIntervalSince1970 * 1000))
        return FastingSession(
            id: id,
            startTime: startedAt,
            plannedDuration: plannedDuration,
            endTime: startedAt.addingTimeInterval(plannedDuration),
            isActive: true,
            startMode: .retroactive
        )
    }

    func extendBy(_ extra: TimeInterval) -> FastingSession {
        guard extra > 0 else { return self }
        let newPlanned = plannedDuration + extra
        return FastingSession(
            id: id,
            startTime: startTime,
            plannedDuration: newPlanned,
            endTime: startTime.addingTimeInterval(newPlanned),
            isActive: isActive,
            startMode: startMode
        )
    }
}
