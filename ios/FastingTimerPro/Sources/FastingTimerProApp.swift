import SwiftUI

@main
struct FastingTimerProApp: App {
    @StateObject private var localization = LocalizationManager.shared

    var body: some Scene {
        WindowGroup {
            DashboardView()
                .environmentObject(localization)
                .environment(\.locale, Locale(identifier: localization.effectiveLocaleId))
        }
    }
}
