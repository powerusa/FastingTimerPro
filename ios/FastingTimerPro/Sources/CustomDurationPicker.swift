import SwiftUI

struct CustomDurationPicker: View {

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
                Text("Custom Fast Duration")
                    .font(.system(size: 20, weight: .semibold, design: .rounded))
                    .foregroundStyle(AppColors.primaryText)

                Text("Set your target fasting time")
                    .font(.system(size: 14, weight: .regular))
                    .foregroundStyle(AppColors.secondaryText)

                HStack(spacing: 24) {
                    VStack(spacing: 8) {
                        Text("Days")
                            .font(.system(size: 13, weight: .medium))
                            .foregroundStyle(AppColors.secondaryText)

                        Picker("Days", selection: $days) {
                            ForEach(0..<31) { d in
                                Text("\(d)").tag(d)
                            }
                        }
                        .pickerStyle(.wheel)
                        .frame(width: 80, height: 120)
                        .clipped()
                    }

                    VStack(spacing: 8) {
                        Text("Hours")
                            .font(.system(size: 13, weight: .medium))
                            .foregroundStyle(AppColors.secondaryText)

                        Picker("Hours", selection: $hours) {
                            ForEach(0..<24) { h in
                                Text("\(h)").tag(h)
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
                        Text("Cancel")
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
                        Text("Start Fast")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(GlassPillButtonStyle(isPrimary: true))
                    .disabled(totalHours < 1)
                    .opacity(totalHours < 1 ? 0.5 : 1.0)
                }
            }
            .padding(24)
            .background(AppColors.cardBackground)
            .clipShape(RoundedRectangle(cornerRadius: 24, style: .continuous))
            .overlay(
                RoundedRectangle(cornerRadius: 24, style: .continuous)
                    .stroke(AppColors.cardBorder, lineWidth: 1)
            )
            .shadow(color: .black.opacity(0.4), radius: 30, x: 0, y: 10)
            .padding(24)
        }
    }

    private var totalHours: Int {
        days * 24 + hours
    }

    private var totalDurationText: String {
        if days > 0 && hours > 0 {
            return "Total: \(days)d \(hours)h"
        } else if days > 0 {
            return "Total: \(days) day\(days == 1 ? "" : "s")"
        } else {
            return "Total: \(hours) hour\(hours == 1 ? "" : "s")"
        }
    }
}
