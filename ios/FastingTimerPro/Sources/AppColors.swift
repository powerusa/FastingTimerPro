import SwiftUI

enum AppColors {

    // MARK: - Background
    static let baseBackground = Color(hex: 0x0B1220)
    static let gradientTop = Color(hex: 0x0F1A2E)
    static let gradientBottom = Color(hex: 0x070B14)

    // MARK: - Accent
    static let primaryAccent = Color(hex: 0x4DA3FF)
    static let secondaryAccent = Color(hex: 0x6FD3FF)

    // MARK: - Text
    static let primaryText = Color.white
    static let secondaryText = Color(hex: 0xA9B4C7)
    static let mutedText = Color(hex: 0x7C8698)

    // MARK: - Glass / Cards
    static let cardBackground = Color.white.opacity(0.08)
    static let cardBorder = Color.white.opacity(0.18)

    // MARK: - Buttons
    static let primaryButtonBackground = Color(hex: 0x4DA3FF).opacity(0.25)
    static let secondaryButtonBackground = Color.white.opacity(0.10)
    static let destructiveButtonBackground = Color(red: 1.0, green: 0.35, blue: 0.35).opacity(0.35)

    // MARK: - Highlights
    static let brightRed = Color(hex: 0xFF5A5A)
}

extension Color {
    init(hex: UInt, alpha: Double = 1.0) {
        self.init(
            .sRGB,
            red: Double((hex >> 16) & 0xFF) / 255.0,
            green: Double((hex >> 8) & 0xFF) / 255.0,
            blue: Double(hex & 0xFF) / 255.0,
            opacity: alpha
        )
    }
}
