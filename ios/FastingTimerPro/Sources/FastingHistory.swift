import Foundation

struct CompletedFast: Identifiable, Codable, Equatable {
    let id: String
    let startTime: Date
    let endTime: Date
    let plannedDuration: TimeInterval
    let actualDuration: TimeInterval
    let wasCompleted: Bool

    var formattedDate: String {
        let formatter = DateFormatter()
        formatter.dateStyle = .medium
        formatter.timeStyle = .short
        return formatter.string(from: startTime)
    }

    var formattedActualDuration: String {
        let hours = Int(actualDuration) / 3600
        let minutes = (Int(actualDuration) % 3600) / 60
        if hours >= 24 {
            let days = hours / 24
            let remainingHours = hours % 24
            return "\(days)d \(remainingHours)h \(minutes)m"
        }
        return "\(hours)h \(minutes)m"
    }

    var formattedPlannedDuration: String {
        let hours = Int(plannedDuration) / 3600
        if hours >= 24 {
            let days = hours / 24
            let remainingHours = hours % 24
            if remainingHours > 0 {
                return "\(days)d \(remainingHours)h"
            }
            return "\(days)d"
        }
        return "\(hours)h"
    }
}

@MainActor
final class FastingHistoryStore: ObservableObject {

    @Published private(set) var completedFasts: [CompletedFast] = []

    private let historyKey = "fasting.history"
    private let maxHistoryItems = 50

    init() {
        self.completedFasts = loadHistory()
    }

    func recordCompletedFast(session: FastingSession, endedAt: Date = Date()) {
        let actualDuration = endedAt.timeIntervalSince(session.startTime)
        let wasCompleted = actualDuration >= session.plannedDuration

        let completed = CompletedFast(
            id: session.id,
            startTime: session.startTime,
            endTime: endedAt,
            plannedDuration: session.plannedDuration,
            actualDuration: actualDuration,
            wasCompleted: wasCompleted
        )

        var history = completedFasts
        history.insert(completed, at: 0)

        if history.count > maxHistoryItems {
            history = Array(history.prefix(maxHistoryItems))
        }

        saveHistory(history)
        completedFasts = history
    }

    func clearHistory() {
        completedFasts = []
        UserDefaults.standard.removeObject(forKey: historyKey)
    }

    func deleteFast(id: String) {
        var history = completedFasts
        history.removeAll { $0.id == id }
        saveHistory(history)
        completedFasts = history
    }

    private func saveHistory(_ history: [CompletedFast]) {
        let encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .iso8601
        if let data = try? encoder.encode(history) {
            UserDefaults.standard.set(data, forKey: historyKey)
        }
    }

    private func loadHistory() -> [CompletedFast] {
        guard let data = UserDefaults.standard.data(forKey: historyKey) else { return [] }
        let decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .iso8601
        return (try? decoder.decode([CompletedFast].self, from: data)) ?? []
    }
}
