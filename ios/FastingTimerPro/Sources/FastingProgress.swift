import Foundation

struct FastingProgress {
    let elapsed: TimeInterval
    let remaining: TimeInterval
    let progress01: Double
    let currentStage: FastingStage
    let nextStage: FastingStage?
}

enum FastingProgressCalculator {

    static func compute(
        session: FastingSession,
        now: Date,
        stages: [FastingStage] = FastingStages.default()
    ) -> FastingProgress {
        let elapsed: TimeInterval = Swift.max(0.0, now.timeIntervalSince(session.startTime))
        let total: TimeInterval = Swift.max(60.0, session.plannedDuration)
        let remaining: TimeInterval = Swift.max(0.0, session.endTime.timeIntervalSince(now))
        let progress01: Double = Swift.min(1.0, Swift.max(0.0, elapsed / total))

        let elapsedHours = elapsed / 3600.0
        var current = stages.first!
        for stage in stages {
            if elapsedHours >= stage.startHours {
                if let end = stage.endHours {
                    if elapsedHours < end {
                        current = stage
                    }
                } else {
                    current = stage
                }
            }
        }

        let next = stages.first(where: { $0.startHours > current.startHours })

        return FastingProgress(
            elapsed: elapsed,
            remaining: remaining,
            progress01: progress01,
            currentStage: current,
            nextStage: next
        )
    }
}
