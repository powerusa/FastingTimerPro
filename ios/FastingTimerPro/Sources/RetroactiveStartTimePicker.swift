import SwiftUI

struct RetroactiveStartTimePicker: View {

    @EnvironmentObject private var localization: LocalizationManager

    @Binding var isPresented: Bool
    let onConfirm: (Date) -> Void

    @State private var hoursAgo: Int

    init(isPresented: Binding<Bool>, initialStartedAt: Date, onConfirm: @escaping (Date) -> Void) {
        self._isPresented = isPresented
        self.onConfirm = onConfirm
        let diff = Date().timeIntervalSince(initialStartedAt)
        let computedHoursAgo = Int(diff / 3600)
        self._hoursAgo = State(initialValue: min(max(0, computedHoursAgo), 24))
    }

    var body: some View {
        let now = Date()
        let startedAt = now.addingTimeInterval(-TimeInterval(hoursAgo) * 3600)

        return ZStack {
            Color.black.opacity(0.6)
                .ignoresSafeArea()
                .onTapGesture {
                    isPresented = false
                }

            VStack(spacing: 20) {
                Text(localization.t("retroactive_picker.title"))
                    .font(.system(size: 20, weight: .semibold, design: .rounded))
                    .foregroundStyle(AppColors.primaryText)

                Text(localization.t("retroactive_picker.subtitle"))
                    .font(.system(size: 14, weight: .regular))
                    .foregroundStyle(AppColors.secondaryText)

                VStack(spacing: 8) {
                    Text(localization.t("retroactive_picker.hours_ago"))
                        .font(.system(size: 13, weight: .medium))
                        .foregroundStyle(AppColors.secondaryText)

                    Picker("Hours ago", selection: $hoursAgo) {
                        ForEach(0...24, id: \.self) { h in
                            Text("\(h)")
                                .foregroundStyle(AppColors.brightRed)
                                .tag(h)
                        }
                    }
                    .pickerStyle(.wheel)
                    .frame(width: 120, height: 170)
                    .clipped()
                }

                Text(localization.tf("dashboard.elapsed_format", TimeFormat.hhmmss(max(0, now.timeIntervalSince(startedAt)))))
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
                        onConfirm(startedAt)
                        isPresented = false
                    } label: {
                        Text(localization.t("common.start_fast"))
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(GlassPillButtonStyle(isPrimary: true))
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
}
