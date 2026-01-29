import Foundation

@MainActor
final class FastingSessionStore: ObservableObject {

    @Published private(set) var activeSession: FastingSession?

    private let activeKey = "fasting.activeSession"

    init() {
        self.activeSession = loadActive()
    }

    func startNow(plannedDuration: TimeInterval, now: Date = Date()) {
        let session = FastingSession.startNow(plannedDuration: plannedDuration, now: now)
        saveActive(session)
        activeSession = session
    }

    func startRetroactive(startedAt: Date, plannedDuration: TimeInterval, now: Date = Date()) {
        let session = FastingSession.startRetroactive(startedAt: startedAt, plannedDuration: plannedDuration, now: now)
        saveActive(session)
        activeSession = session
    }

    func extendActive(by extra: TimeInterval) {
        guard let current = activeSession else { return }
        let updated = current.extendBy(extra)
        saveActive(updated)
        activeSession = updated
    }

    func stopActive() {
        activeSession = nil
        UserDefaults.standard.removeObject(forKey: activeKey)
    }

    private func saveActive(_ session: FastingSession) {
        let encoder = JSONEncoder()
        encoder.dateEncodingStrategy = .iso8601
        if let data = try? encoder.encode(session) {
            UserDefaults.standard.set(data, forKey: activeKey)
        }
    }

    private func loadActive() -> FastingSession? {
        guard let data = UserDefaults.standard.data(forKey: activeKey) else { return nil }
        let decoder = JSONDecoder()
        decoder.dateDecodingStrategy = .iso8601
        return try? decoder.decode(FastingSession.self, from: data)
    }
}
