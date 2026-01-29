import SwiftUI

struct HistoryView: View {

    @ObservedObject var historyStore: FastingHistoryStore
    @Environment(\.dismiss) private var dismiss

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

            Text("History")
                .font(.system(size: 20, weight: .semibold, design: .rounded))
                .foregroundStyle(AppColors.primaryText)

            Spacer()

            if !historyStore.completedFasts.isEmpty {
                Button {
                    historyStore.clearHistory()
                } label: {
                    Text("Clear")
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

            Text("No fasts yet")
                .font(.system(size: 18, weight: .semibold))
                .foregroundStyle(AppColors.primaryText)

            Text("Your completed fasts will appear here")
                .font(.system(size: 14))
                .foregroundStyle(AppColors.secondaryText)

            Spacer()
        }
    }
}

struct HistoryRow: View {

    let fast: CompletedFast
    let onDelete: () -> Void

    var body: some View {
        GlassCard {
            HStack {
                VStack(alignment: .leading, spacing: 6) {
                    Text(fast.formattedDate)
                        .font(.system(size: 14, weight: .medium))
                        .foregroundStyle(AppColors.secondaryText)

                    HStack(spacing: 8) {
                        Text(fast.formattedActualDuration)
                            .font(.system(size: 20, weight: .semibold))
                            .foregroundStyle(AppColors.primaryText)

                        Text("/ \(fast.formattedPlannedDuration)")
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
}
