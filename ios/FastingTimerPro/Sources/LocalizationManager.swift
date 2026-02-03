import Combine
import Foundation

@MainActor
final class LocalizationManager: ObservableObject {

    static let shared = LocalizationManager()

    static let supportedLocaleIds: [String] = [
        "en",
        "es-ES",
        "es-419",
        "pt-BR",
        "de",
        "fr",
        "ja",
        "ko",
        "zh-Hans",
        "pl"
    ]

    @Published private(set) var effectiveLocaleId: String

    private let overrideKey = "app.languageOverride"

    private init() {
        effectiveLocaleId = "en"
        applyCurrentSelection()
    }

    var languageOverride: String? {
        UserDefaults.standard.string(forKey: overrideKey)
    }

    func setLanguageOverride(_ localeId: String?) {
        if let localeId {
            UserDefaults.standard.set(localeId, forKey: overrideKey)
        } else {
            UserDefaults.standard.removeObject(forKey: overrideKey)
        }
        applyCurrentSelection()
    }

    func useDeviceLanguage() {
        setLanguageOverride(nil)
    }

    func t(_ key: String) -> String {
        localizedString(key, localeId: effectiveLocaleId)
    }

    func tf(_ key: String, _ args: CVarArg...) -> String {
        let format = t(key)
        return String(format: format, locale: Locale(identifier: effectiveLocaleId), arguments: args)
    }

    func localizedString(_ key: String, localeId: String) -> String {
        let bundle = bundleForLocale(localeId)
        let value = bundle.localizedString(forKey: key, value: nil, table: nil)
        if value != key {
            return value
        }
        if localeId != "en" {
            return bundleForLocale("en").localizedString(forKey: key, value: nil, table: nil)
        }
        return value
    }

    private func bundleForLocale(_ localeId: String) -> Bundle {
        if let path = Bundle.main.path(forResource: localeId, ofType: "lproj"), let bundle = Bundle(path: path) {
            return bundle
        }
        return Bundle.main
    }

    private func applyCurrentSelection() {
        if let override = languageOverride, Self.supportedLocaleIds.contains(override) {
            effectiveLocaleId = override
        } else {
            effectiveLocaleId = selectAutoLocaleId()
        }
    }

    private func selectAutoLocaleId(preferredLanguages: [String] = Locale.preferredLanguages) -> String {
        for tag in preferredLanguages {
            if let mapped = mapDeviceLanguageTagToSupportedLocaleId(tag), Self.supportedLocaleIds.contains(mapped) {
                return mapped
            }
        }
        return "en"
    }

    private func mapDeviceLanguageTagToSupportedLocaleId(_ tag: String) -> String? {
        let parts = tag.replacingOccurrences(of: "_", with: "-").split(separator: "-")
        guard let language = parts.first?.lowercased() else { return nil }

        var region: String?
        var script: String?
        for part in parts.dropFirst() {
            if part.count == 4 {
                script = String(part)
            } else if part.count == 2 || part.count == 3 {
                region = String(part).uppercased()
            }
        }

        switch language {
        case "es":
            if region == "ES" { return "es-ES" }
            return "es-419"
        case "pt":
            return "pt-BR"
        case "zh":
            if script?.lowercased() == "hans" { return "zh-Hans" }
            if region == "CN" || region == "SG" { return "zh-Hans" }
            return "zh-Hans"
        case "de":
            return "de"
        case "fr":
            return "fr"
        case "ja":
            return "ja"
        case "ko":
            return "ko"
        case "pl":
            return "pl"
        case "en":
            return "en"
        default:
            return nil
        }
    }
}
