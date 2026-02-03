import SwiftUI

struct DashboardView: View {

    @EnvironmentObject private var localization: LocalizationManager

    @StateObject private var store = FastingSessionStore()
    @StateObject private var historyStore = FastingHistoryStore()
    @State private var now = Date()
    @State private var showCustomPicker = false
    @State private var showCustomStartTimePicker = false
    @State private var customStartedAt = Date().addingTimeInterval(-4 * 3600)
    @State private var showHistory = false
    @State private var showSettings = false

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
                .environmentObject(localization)
            }

            if showCustomStartTimePicker {
                RetroactiveStartTimePicker(isPresented: $showCustomStartTimePicker, initialStartedAt: customStartedAt) { startedAt in
                    store.startRetroactive(
                        startedAt: startedAt,
                        plannedDuration: 16 * 3600
                    )
                }
                .environmentObject(localization)
            }
        }
        .sheet(isPresented: $showHistory) {
            HistoryView(historyStore: historyStore)
                .environmentObject(localization)
        }
        .sheet(isPresented: $showSettings) {
            SettingsView()
                .environmentObject(localization)
        }
    }

    private var header: some View {
        HStack {
            VStack(alignment: .leading, spacing: 6) {
                Text(localization.t("app.title"))
                    .font(.system(size: 28, weight: .semibold, design: .rounded))
                    .foregroundStyle(AppColors.primaryText)

                Text(localization.t("app.subtitle"))
                    .font(.system(size: 13, weight: .regular))
                    .foregroundStyle(AppColors.secondaryText)
            }

            Spacer()

            HStack(spacing: 12) {
                Button {
                    showSettings = true
                } label: {
                    Image(systemName: "gearshape")
                        .font(.system(size: 20))
                        .foregroundStyle(AppColors.primaryAccent)
                }

                Button {
                    showHistory = true
                } label: {
                    Image(systemName: "clock.arrow.circlepath")
                        .font(.system(size: 22))
                        .foregroundStyle(AppColors.primaryAccent)
                }
            }
        }
    }

    @ViewBuilder
    private func activeSessionView(_ session: FastingSession) -> some View {
        let progress = FastingProgressCalculator.compute(session: session, now: now)
        let isRetroactive = session.startMode == .retroactive
        let ringTime = isRetroactive ? progress.elapsed : progress.remaining
        let ringSubtitle = isRetroactive ? localization.t("dashboard.ring.custom_time") : localization.t("dashboard.ring.remaining")
        let plannedLabel = session.startMode == .retroactive ? localization.t("dashboard.planned.custom_time") : formatPlannedDuration(session.plannedDuration)
        let stageTitle = localization.t(progress.currentStage.titleKey)
        let stageDescription = progress.currentStage.descriptionKeys.map { localization.t($0) }.joined(separator: "\n")

        VStack(spacing: 20) {
            ZStack {
                ProgressRing(progress: progress.progress01, lineWidth: 14, size: 220)

                VStack(spacing: 6) {
                    Text(TimeFormat.hhmmss(ringTime))
                        .font(.system(size: 42, weight: .semibold, design: .rounded))
                        .foregroundStyle(AppColors.primaryText)
                        .monospacedDigit()

                    Text(ringSubtitle)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(AppColors.secondaryText)

                    Text(plannedLabel)
                        .font(.system(size: 12, weight: .regular))
                        .foregroundStyle(AppColors.brightRed)
                }
            }
            .padding(.vertical, 10)

            GlassCard {
                VStack(alignment: .leading, spacing: 10) {
                    Text(localization.tf("dashboard.elapsed_format", TimeFormat.hhmmss(progress.elapsed)))
                        .font(.system(size: 16, weight: .medium))
                        .foregroundStyle(AppColors.primaryText)
                        .monospacedDigit()
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }

            GlassCard {
                VStack(alignment: .leading, spacing: 10) {
                    Text(localization.t("dashboard.body.title"))
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(AppColors.secondaryText)

                    Text(stageTitle)
                        .font(.system(size: 20, weight: .semibold))
                        .foregroundStyle(AppColors.primaryAccent)

                    Text(stageDescription)
                        .font(.system(size: 15, weight: .regular))
                        .foregroundStyle(AppColors.primaryText.opacity(0.9))
                        .fixedSize(horizontal: false, vertical: true)

                    if progress.currentStage.isExtendedTrackingOnly {
                        Text(localization.t("dashboard.extended_tracking_only"))
                            .font(.system(size: 13, weight: .regular))
                            .foregroundStyle(AppColors.mutedText)
                            .padding(.top, 6)
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
            }

            if let next = progress.nextStage {
                let nextTitle = localization.t(next.titleKey)
                GlassCard {
                    HStack {
                        Text(localization.t("dashboard.next_prefix"))
                            .font(.system(size: 14, weight: .medium))
                            .foregroundStyle(AppColors.secondaryText)
                        Text(nextTitle)
                            .font(.system(size: 14, weight: .semibold))
                            .foregroundStyle(AppColors.secondaryAccent)
                    }
                    .frame(maxWidth: .infinity, alignment: .leading)
                }
            }

            HStack(spacing: 12) {
                Button { store.extendActive(by: 3600) } label: {
                    Text(localization.tf("dashboard.extend_hours_format", 1)).frame(maxWidth: .infinity)
                }
                .buttonStyle(GlassPillButtonStyle())

                Button { store.extendActive(by: 6 * 3600) } label: {
                    Text(localization.tf("dashboard.extend_hours_format", 6)).frame(maxWidth: .infinity)
                }
                .buttonStyle(GlassPillButtonStyle())

                Button {
                    if let session = store.activeSession {
                        historyStore.recordCompletedFast(session: session)
                    }
                    store.stopActive()
                } label: {
                    Text(localization.t("common.stop")).frame(maxWidth: .infinity)
                }
                .buttonStyle(GlassPillButtonStyle(isDestructive: true))
            }
        }
    }

    private var startView: some View {
        VStack(spacing: 16) {
            GlassCard {
                VStack(alignment: .leading, spacing: 10) {
                    Text(localization.t("dashboard.start.title"))
                        .font(.system(size: 18, weight: .semibold))
                        .foregroundStyle(AppColors.primaryText)

                    Text(localization.t("dashboard.start.subtitle"))
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
                    Text(localization.t("common.custom"))
                        .frame(maxWidth: .infinity)
                }
                .buttonStyle(GlassPillButtonStyle())
            }

            Button {
                customStartedAt = Date().addingTimeInterval(-4 * 3600)
                showCustomStartTimePicker = true
            } label: {
                Text(localization.t("dashboard.already_started_custom"))
                    .frame(maxWidth: .infinity)
            }
            .buttonStyle(GlassPillButtonStyle())
        }
    }

    private func formatPlannedDuration(_ duration: TimeInterval) -> String {
        let formatter = DateComponentsFormatter()
        formatter.allowedUnits = duration >= 24 * 3600 ? [.day, .hour] : [.hour]
        formatter.unitsStyle = .full
        formatter.maximumUnitCount = 2
        formatter.zeroFormattingBehavior = [.dropAll]
        formatter.calendar = Calendar.current
        formatter.calendar?.locale = Locale(identifier: localization.effectiveLocaleId)
        let formatted = formatter.string(from: duration) ?? ""
        return localization.tf("planned_duration.fast_format", formatted)
    }
}
