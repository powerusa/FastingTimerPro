import SwiftUI

struct SettingsView: View {

    @EnvironmentObject private var localization: LocalizationManager
    @Environment(\.dismiss) private var dismiss

    var body: some View {
        NavigationStack {
            ZStack {
                BackgroundGradient()

                Form {
                    Section {
                        Picker(localization.t("settings.language"), selection: Binding(
                            get: { localization.languageOverride ?? "" },
                            set: { newValue in
                                if newValue.isEmpty {
                                    localization.useDeviceLanguage()
                                } else {
                                    localization.setLanguageOverride(newValue)
                                }
                            }
                        )) {
                            Text(localization.t("settings.use_device_language"))
                                .tag("")

                            ForEach(LocalizationManager.supportedLocaleIds, id: \.self) { id in
                                Text(localeDisplayName(id))
                                    .tag(id)
                            }
                        }
                    }
                }
                .scrollContentBackground(.hidden)
            }
            .navigationTitle(localization.t("settings.title"))
            .toolbar {
                ToolbarItem(placement: .topBarTrailing) {
                    Button {
                        dismiss()
                    } label: {
                        Text(localization.t("common.done"))
                    }
                }
            }
        }
    }

    private func localeDisplayName(_ localeId: String) -> String {
        switch localeId {
        case "en": return "English"
        case "es-ES": return "Español (España)"
        case "es-419": return "Español (Latinoamérica)"
        case "pt-BR": return "Português (Brasil)"
        case "de": return "Deutsch"
        case "fr": return "Français"
        case "ja": return "日本語"
        case "ko": return "한국어"
        case "zh-Hans": return "简体中文"
        default: return localeId
        }
    }
}
