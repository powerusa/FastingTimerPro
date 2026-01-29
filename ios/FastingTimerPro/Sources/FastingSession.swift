import Foundation

struct FastingSession: Identifiable, Codable, Equatable {
    let id: String
    let startTime: Date
    let plannedDuration: TimeInterval
    let endTime: Date
    let isActive: Bool

    static func startNow(plannedDuration: TimeInterval, now: Date = Date()) -> FastingSession {
        let id = String(Int(now.timeIntervalSince1970 * 1000))
        return FastingSession(
            id: id,
            startTime: now,
            plannedDuration: plannedDuration,
            endTime: now.addingTimeInterval(plannedDuration),
            isActive: true
        )
    }

    static func startRetroactive(startedAt: Date, plannedDuration: TimeInterval, now: Date = Date()) -> FastingSession {
        let id = String(Int(now.timeIntervalSince1970 * 1000))
        return FastingSession(
            id: id,
            startTime: startedAt,
            plannedDuration: plannedDuration,
            endTime: startedAt.addingTimeInterval(plannedDuration),
            isActive: true
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
            isActive: isActive
        )
    }
}
