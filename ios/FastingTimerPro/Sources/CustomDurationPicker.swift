import SwiftUI

struct CustomDurationPicker: View {

    @EnvironmentObject private var localization: LocalizationManager

    @Binding var isPresented: Bool
    let onConfirm: (TimeInterval) -> Void

    @State private var days: Int = 0
    @State private var hours: Int = 16

    var body: some View {
        ZStack {
            Color.black.opacity(0.6)
                .ignoresSafeArea()
                .onTapGesture {
                    isPresented = false
                }

            VStack(spacing: 20) {
                Text(localization.t("custom_duration.title"))
                    .font(.system(size: 20, weight: .semibold, design: .rounded))
                    .foregroundStyle(AppColors.primaryText)

                Text(localization.t("custom_duration.subtitle"))
                    .font(.system(size: 14, weight: .regular))
                    .foregroundStyle(AppColors.secondaryText)

                HStack(spacing: 24) {
                    VStack(spacing: 8) {
                        Text(localization.t("custom_duration.days"))
                            .font(.system(size: 13, weight: .medium))
                            .foregroundStyle(AppColors.secondaryText)

                        Picker("Days", selection: $days) {
                            ForEach(0..<31) { d in
                                Text("\(d)")
                                    .foregroundStyle(AppColors.brightRed)
                                    .tag(d)
                            }
                        }
                        .pickerStyle(.wheel)
                        .frame(width: 80, height: 120)
                        .clipped()
                    }

                    VStack(spacing: 8) {
                        Text(localization.t("custom_duration.hours"))
                            .font(.system(size: 13, weight: .medium))
                            .foregroundStyle(AppColors.secondaryText)

                        Picker("Hours", selection: $hours) {
                            ForEach(0..<25) { h in
                                Text("\(h)")
                                    .foregroundStyle(AppColors.brightRed)
                                    .tag(h)
                            }
                        }
                        .pickerStyle(.wheel)
                        .frame(width: 80, height: 120)
                        .clipped()
                    }
                }

                Text(totalDurationText)
                    .font(.system(size: 16, weight: .medium))
                    .foregroundStyle(AppColors.primaryAccent)
                    .padding(.top, 4)

                HStack(spacing: 12) {
                    Button {
                        isPresented = false
                    } label: {
                        Text(localization.t("common.cancel"))
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(GlassPillButtonStyle())

                    Button {
                        let totalSeconds = TimeInterval((days * 24 + hours) * 3600)
                        if totalSeconds >= 3600 {
                            onConfirm(totalSeconds)
                            isPresented = false
                        }
                    } label: {
                        Text(localization.t("common.start_fast"))
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(GlassPillButtonStyle(isPrimary: true))
                    .disabled(totalHours < 1)
                    .opacity(totalHours < 1 ? 0.5 : 1.0)
                }
            }
            .padding(24)
            .background(AppColors.baseBackground.opacity(0.95))
            .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
            .overlay(
                RoundedRectangle(cornerRadius: 24, style: .continuous)
                    .stroke(AppColors.cardBorder, lineWidth: 1.5)
            )
            .shadow(color: .black.opacity(0.6), radius: 40, x: 0, y: 15)
            .padding(24)
        }
    }

    private var totalHours: Int {
        days * 24 + hours
    }

    private var totalDurationText: String {
        let totalSeconds = TimeInterval(totalHours * 3600)
        let formatter = DateComponentsFormatter()
        if days > 0 {
            formatter.allowedUnits = hours > 0 ? [.day, .hour] : [.day]
        } else {
            formatter.allowedUnits = [.hour]
        }
        formatter.unitsStyle = .abbreviated
        formatter.maximumUnitCount = 2
        formatter.zeroFormattingBehavior = [.dropAll]
        formatter.calendar = Calendar.current
        formatter.calendar?.locale = Locale(identifier: localization.effectiveLocaleId)
        let formatted = formatter.string(from: totalSeconds) ?? ""
        return localization.tf("custom_duration.total_format", formatted)
    }
}
