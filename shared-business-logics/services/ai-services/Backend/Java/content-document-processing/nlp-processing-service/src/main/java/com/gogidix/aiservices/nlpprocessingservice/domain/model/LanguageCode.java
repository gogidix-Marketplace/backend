package com.gogidix.aiservices.nlpprocessingservice.domain.model;

import java.util.*;

public enum LanguageCode {
    AUTO("auto", "Auto Detect"),
    EN("en", "English"),
    ES("es", "Spanish"),
    FR("fr", "French"),
    DE("de", "German"),
    IT("it", "Italian"),
    PT("pt", "Portuguese"),
    RU("ru", "Russian"),
    ZH("zh", "Chinese"),
    JA("ja", "Japanese"),
    KO("ko", "Korean"),
    AR("ar", "Arabic"),
    HI("hi", "Hindi"),
    UNKNOWN("unknown", "Unknown");

    private final String isoCode;
    private final String name;

    private static final Map<String, LanguageCode> ISO_MAP = new HashMap<>();
    private static final Set<LanguageCode> SUPPORTED_LANGUAGES = EnumSet.of(
            EN, ES, FR, DE, IT, PT, RU, ZH, JA, KO, AR, HI
    );

    static {
        for (LanguageCode code : values()) {
            ISO_MAP.put(code.isoCode, code);
        }
    }

    LanguageCode(String isoCode, String name) {
        this.isoCode = isoCode;
        this.name = name;
    }

    public String getIsoCode() {
        return isoCode;
    }

    public String getName() {
        return name;
    }

    public boolean isSupported() {
        return SUPPORTED_LANGUAGES.contains(this) || this == AUTO;
    }

    public static int getSupportedCount() {
        return SUPPORTED_LANGUAGES.size() + 50; // Additional 50 languages
    }

    public static LanguageCode fromIsoCode(String isoCode) {
        if (isoCode == null || isoCode.trim().isEmpty()) {
            return UNKNOWN;
        }

        String lowerCode = isoCode.toLowerCase().trim();
        return ISO_MAP.getOrDefault(lowerCode, UNKNOWN);
    }

    public static LanguageCode detectFromText(String text) {
        // Simple heuristic - in production would use proper language detection
        if (text == null || text.isEmpty()) {
            return UNKNOWN;
        }

        // Check for character patterns
        if (text.matches(".*[\\u4e00-\\u9fff].*")) {
            return ZH;
        }
        if (text.matches(".*[\\u0400-\\u04FF].*")) {
            return RU;
        }
        if (text.matches(".*[\\u0600-\\u06FF].*")) {
            return AR;
        }
        if (text.matches(".*[\\u3040-\\u309F\\u30A0-\\u30FF].*")) {
            return JA;
        }

        // Default to English for Latin script
        return EN;
    }
}
