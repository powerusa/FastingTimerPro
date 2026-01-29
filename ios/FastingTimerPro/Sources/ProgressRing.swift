import SwiftUI

struct ProgressRing: View {
    let progress: Double
    let lineWidth: CGFloat
    let size: CGFloat

    var body: some View {
        ZStack {
            Circle()
                .stroke(
                    LinearGradient(
                        colors: [
                            AppColors.cardBorder,
                            AppColors.cardBackground
                        ],
                        startPoint: .topLeading,
                        endPoint: .bottomTrailing
                    ),
                    lineWidth: lineWidth
                )

            Circle()
                .trim(from: 0, to: CGFloat(Swift.min(1.0, Swift.max(0.0, progress))))
                .stroke(
                    LinearGradient(
                        colors: [
                            AppColors.primaryAccent,
                            AppColors.secondaryAccent
                        ],
                        startPoint: .topLeading,
                        endPoint: .bottomTrailing
                    ),
                    style: StrokeStyle(lineWidth: lineWidth, lineCap: .round)
                )
                .rotationEffect(.degrees(-90))
                .shadow(color: AppColors.primaryAccent.opacity(0.5), radius: 8, x: 0, y: 0)
                .animation(.easeInOut(duration: 0.4), value: progress)
        }
        .frame(width: size, height: size)
    }
}
