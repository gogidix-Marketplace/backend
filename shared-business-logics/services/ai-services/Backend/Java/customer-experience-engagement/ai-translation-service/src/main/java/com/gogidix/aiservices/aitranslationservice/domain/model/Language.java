package com.gogidix.aiservices.aitranslationservice.domain.model;

public enum Language {
    ENGLISH("en", "English"),
    SPANISH("es", "Spanish"),
    FRENCH("fr", "French"),
    GERMAN("de", "German"),
    CHINESE("zh", "Chinese"),
    JAPANESE("ja", "Japanese"),
    KOREAN("ko", "Korean"),
    PORTUGUESE("pt", "Portuguese"),
    ITALIAN("it", "Italian"),
    RUSSIAN("ru", "Russian"),
    ARABIC("ar", "Arabic"),
    HINDI("hi", "Hindi");

    private final String code;
    private final String name;

    Language(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() { return code; }
    public String getName() { return name; }

    public static Language fromCode(String code) {
        for (Language lang : Language.values()) {
            if (lang.code.equalsIgnoreCase(code)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Unknown language code: " + code);
    }
}
