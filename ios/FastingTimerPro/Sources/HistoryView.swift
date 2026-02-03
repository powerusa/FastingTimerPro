import SwiftUI

struct HistoryView: View {

    @ObservedObject var historyStore: FastingHistoryStore
    @Environment(\.dismiss) private var dismiss
    @EnvironmentObject private var localization: LocalizationManager

    var body: some View {
        ZStack {
            BackgroundGradient()

            VStack(spacing: 0) {
                header

                if historyStore.completedFasts.isEmpty {
                    emptyState
                } else {
                    ScrollView {
                        LazyVStack(spacing: 12) {
                            ForEach(historyStore.completedFasts) { fast in
                                HistoryRow(fast: fast) {
                                    historyStore.deleteFast(id: fast.id)
                                }
                            }
                        }
                        .padding(20)
                    }
                }
            }
        }
    }

    private var header: some View {
        HStack {
            Button {
                dismiss()
            } label: {
                Image(systemName: "chevron.left")
                    .font(.system(size: 18, weight: .semibold))
                    .foregroundStyle(AppColors.primaryAccent)
            }

            Spacer()

            Text(localization.t("history.title"))
                .font(.system(size: 20, weight: .semibold, design: .rounded))
                .foregroundStyle(AppColors.primaryText)

            Spacer()

            if !historyStore.completedFasts.isEmpty {
                Button {
                    historyStore.clearHistory()
                } label: {
                    Text(localization.t("history.clear"))
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(AppColors.secondaryText)
                }
            } else {
                Color.clear.frame(width: 44)
            }
        }
        .padding(.horizontal, 20)
        .padding(.vertical, 16)
    }

    private var emptyState: some View {
        VStack(spacing: 16) {
            Spacer()

            Image(systemName: "clock.arrow.circlepath")
                .font(.system(size: 48))
                .foregroundStyle(AppColors.mutedText)

            Text(localization.t("history.empty.title"))
                .font(.system(size: 18, weight: .semibold))
                .foregroundStyle(AppColors.primaryText)

            Text(localization.t("history.empty.subtitle"))
                .font(.system(size: 14))
                .foregroundStyle(AppColors.secondaryText)

            Spacer()
        }
    }
}

struct HistoryRow: View {

    let fast: CompletedFast
    let onDelete: () -> Void

    @EnvironmentObject private var localization: LocalizationManager

    var body: some View {
        GlassCard {
            HStack {
                VStack(alignment: .leading, spacing: 6) {
                    Text(formattedDate(fast.startTime))
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(AppColors.secondaryText)

                    HStack(spacing: 8) {
                        Text(formattedDuration(fast.actualDuration))
                            .font(.system(size: 20, weight: .semibold))
                            .foregroundStyle(AppColors.primaryText)

                        Text(localization.tf("history.row.planned_format", formattedDuration(fast.plannedDuration)))
                            .font(.system(size: 14, weight: .medium))
                            .foregroundStyle(AppColors.mutedText)
                    }
                }

                Spacer()

                Button {
                    onDelete()
                } label: {
                    Image(systemName: "xmark.circle.fill")
                        .font(.system(size: 24))
                        .foregroundStyle(AppColors.mutedText)
                }
            }
        }
    }

    private func formattedDate(_ date: Date) -> String {
        let formatter = DateFormatter()
        formatter.locale = Locale(identifier: localization.effectiveLocaleId)
        formatter.dateStyle = .medium
        formatter.timeStyle = .short
        return formatter.string(from: date)
    }

    private func formattedDuration(_ seconds: TimeInterval) -> String {
        let formatter = DateComponentsFormatter()
        formatter.allowedUnits = seconds >= 24 * 3600 ? [.day, .hour, .minute] : [.hour, .minute]
        formatter.unitsStyle = .abbreviated
        formatter.maximumUnitCount = 3
        formatter.zeroFormattingBehavior = [.dropAll]
        formatter.calendar = Calendar.current
        formatter.calendar?.locale = Locale(identifier: localization.effectiveLocaleId)
        return formatter.string(from: seconds) ?? ""
    }
}
