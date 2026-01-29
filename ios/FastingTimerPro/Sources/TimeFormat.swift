import Foundation

enum TimeFormat {

    static func hhmmss(_ interval: TimeInterval) -> String {
        let totalSeconds = Int(Swift.max(0, interval))
        let hours = totalSeconds / 3600
        let minutes = (totalSeconds % 3600) / 60
        let seconds = totalSeconds % 60
        return String(format: "%02d:%02d:%02d", hours, minutes, seconds)
    }
}
