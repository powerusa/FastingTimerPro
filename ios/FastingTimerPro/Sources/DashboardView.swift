import SwiftUI

struct DashboardView: View {

    @StateObject private var store = FastingSessionStore()
    @StateObject private var historyStore = FastingHistoryStore()
    @State private var now = Date()
    @State private var showCustomPicker = false
    @State private var showHistory = false

    private let tick = Timer.publish(every: 1, on: .main, in: .common).autoconnect()

    var body: some View {
        ZStack {
            BackgroundGradient()

            ScrollView {
                VStack(spacing: 20) {
                    header

                    if let session = store.activeSession {
                        activeSessionView(session)
                    } else {
                        startView
                    }
                }
                .padding(20)
            }
        }
        .onReceive(tick) { value in
            now = value
        }
        .overlay {
            if showCustomPicker {
                CustomDurationPicker(isPresented: $showCustomPicker) { duration in
                    store.startNow(plannedDuration: duration)
                }
            }
        }
        .sheet(isPresented: $showHistory) {
            HistoryView(historyStore: historyStore)
        }
    }

    private var header: some View {
        HStack {
            VStack(alignment: .leading, spacing: 6) {
                Text("Fasting Timer Pro")
                    .font(.system(size: 28, weight: .semibold, design: .rounded))
                    .foregroundStyle(AppColors.primaryText)

                Text("Educational tracking only. No medical advice.")
                    .font(.system(size: 13, weight: .regular))
                    .foregroundStyle(AppColors.secondaryText)
            }

            Spacer()

            Button {
                showHistory = true
            } label: {
                Image(systemName: "clock.arrow.circlepath")
                    .font(.system(size: 22))
                    .foregroundStyle(AppColors.primaryAccent)
            }
        }
    }

    @ViewBuilder
    private func activeSessionView(_ session: FastingSession) -> some View {
        let progress = FastingProgressCalculator.compute(session: session, now: now)

        VStack(spacing: 20) {
            ZStack {
                ProgressRing(progress: progress.progress01, lineWidth: 14, size: 220)

                VStack(spacing: 6) {
                    Text(TimeFormat.hhmmss(progress.remaining))
                        .font(.system(size: 42, weight: .semibold, design: .rounded))
                        .foregroundStyle(AppColors.primaryText)
                        .monospacedDigit()

                    Text("remaining")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(AppColors.secondaryText)

                    Text(formatPlannedDuration(session.plannedDuration))
                        .font(.system(size: 12, weight: .regular))
                        .foregroundStyle(AppColors.brightRed)
                }
            }
            .padding(.vertical, 10)

            GlassCard {
                VStack(alignment: .leading, spacing: 10) {
                    Text("Elapsed: \(TimeFormat.hhmmss(progress.elapsed))")
                        .font(.system(size: 16, weight: .medium))
                        .foregroundStyle(AppColors.primaryText)
                        .monospacedDigit()
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }

            GlassCard {
                VStack(alignment: .leading, spacing: 10) {
                    Text("What's happening in your body")
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(AppColors.secondaryText)

                    Text(progress.currentStage.title)
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundStyle(AppColors.primaryAccent)

                    Text(progress.currentStage.descriptionLines.joined(separator: "\n"))
                        .font(.system(size: 15, weight: .regular))
                        .foregroundStyle(AppColors.primaryText.opacity(0.9))
                        .fixedSize(horizontal: false, vertical: true)

                    if progress.currentStage.isExtendedTrackingOnly {
                        Text("Extended fasts are tracked for educational purposes only.")
                            .font(.system(size: 13, weight: .regular))
                            .foregroundStyle(AppColors.mutedText)
                            .padding(.top, 6)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }

            if let next = progress.nextStage {
                GlassCard {
                    HStack {
                        Text("Next:")
                            .font(.system(size: 14, weight: .medium))
                            .foregroundStyle(AppColors.secondaryText)
                        Text(next.title)
                            .font(.system(size: 14, weight: .semibold))
                            .foregroundStyle(AppColors.secondaryAccent)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            }

            HStack(spacing: 12) {
                Button { store.extendActive(by: 3600) } label: {
                    Text("+1h").frame(maxWidth: .infinity)
                }
                .buttonStyle(GlassPillButtonStyle())

                Button { store.extendActive(by: 6 * 3600) } label: {
                    Text("+6h").frame(maxWidth: .infinity)
                }
                .buttonStyle(GlassPillButtonStyle())

                Button {
                    if let session = store.activeSession {
                        historyStore.recordCompletedFast(session: session)
                    }
                    store.stopActive()
                } label: {
                    Text("Stop").frame(maxWidth: .infinity)
                }
                .buttonStyle(GlassPillButtonStyle(isDestructive: true))
            }
        }
    }

    private var startView: some View {
        VStack(spacing: 16) {
            GlassCard {
                VStack(alignment: .leading, spacing: 10) {
                    Text("Start a fast")
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundStyle(AppColors.primaryText)

                    Text("Choose a target duration. You can extend anytime.")
                        .font(.system(size: 14, weight: .regular))
                        .foregroundStyle(AppColors.secondaryText)
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }

            LazyVGrid(columns: [GridItem(.flexible()), GridItem(.flexible())], spacing: 12) {
                ForEach([12, 16, 18, 24, 36, 48, 72], id: \.self) { hours in
                    Button {
                        store.startNow(plannedDuration: Double(hours) * 3600)
                    } label: {
                        Text("\(hours)h")
                            .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(GlassPillButtonStyle(isPrimary: true))
                }

                Button {
                    showCustomPicker = true
                } label: {
                    Text("Custom")
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(GlassPillButtonStyle())
            }

            Button {
                store.startRetroactive(
                    startedAt: Date().addingTimeInterval(-4 * 3600),
                    plannedDuration: 16 * 3600
                )
            } label: {
                Text("I already started (4h ago)")
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(GlassPillButtonStyle())
        }
    }

    private func formatPlannedDuration(_ duration: TimeInterval) -> String {
        let totalHours = Int(duration) / 3600
        if totalHours >= 24 {
            let days = totalHours / 24
            let hours = totalHours % 24
            if hours > 0 {
                return "\(days) day\(days == 1 ? "" : "s") \(hours) hour\(hours == 1 ? "" : "s") fast"
            }
            return "\(days) day\(days == 1 ? "" : "s") fast"
        }
        return "\(totalHours) hour\(totalHours == 1 ? "" : "s") fast"
    }
}
