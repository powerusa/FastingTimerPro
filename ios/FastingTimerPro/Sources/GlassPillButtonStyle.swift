import SwiftUI

struct GlassPillButtonStyle: ButtonStyle {

    var isPrimary: Bool = false
    var isDestructive: Bool = false

    func makeBody(configuration: Configuration) -> some View {
        configuration.label
            .font(.system(size: 16, weight: .semibold, design: .rounded))
            .foregroundStyle(AppColors.primaryText)
            .padding(.vertical, 14)
            .padding(.horizontal, 16)
            .background(
                RoundedRectangle(cornerRadius: 999, style: .continuous)
                    .fill(backgroundColor)
            )
            .overlay(
                RoundedRectangle(cornerRadius: 999, style: .continuous)
                    .stroke(borderColor(isPressed: configuration.isPressed), lineWidth: 1)
            )
            .shadow(color: glowColor.opacity(0.3), radius: 8, x: 0, y: 4)
            .scaleEffect(configuration.isPressed ? 0.97 : 1.0)
            .animation(.easeInOut(duration: 0.15), value: configuration.isPressed)
    }

    private var backgroundColor: Color {
        if isDestructive {
            return AppColors.destructiveButtonBackground
        } else if isPrimary {
            return AppColors.primaryButtonBackground
        } else {
            return AppColors.secondaryButtonBackground
        }
    }

    private func borderColor(isPressed: Bool) -> Color {
        if isDestructive {
            return Color(red: 1.0, green: 0.35, blue: 0.35).opacity(isPressed ? 0.5 : 0.3)
        } else if isPrimary {
            return AppColors.primaryAccent.opacity(isPressed ? 0.5 : 0.3)
        } else {
            return AppColors.cardBorder.opacity(isPressed ? 1.0 : 0.8)
        }
    }

    private var glowColor: Color {
        if isDestructive {
            return Color(red: 1.0, green: 0.35, blue: 0.35)
        } else if isPrimary {
            return AppColors.primaryAccent
        } else {
            return .clear
        }
    }
}
