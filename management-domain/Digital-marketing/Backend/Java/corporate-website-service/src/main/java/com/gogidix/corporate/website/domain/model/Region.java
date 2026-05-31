package com.gogidix.corporate.website.domain.model;

public enum Region {
    NG("Nigeria", "en", "+234"),
    KE("Kenya", "en", "+254"),
    GH("Ghana", "en", "+233"),
    ZA("South Africa", "en", "+27"),
    US("United States", "en", "+1");

    private final String displayName;
    private final String defaultLanguage;
    private final String phonePrefix;

    Region(String displayName, String defaultLanguage, String phonePrefix) {
        this.displayName = displayName;
        this.defaultLanguage = defaultLanguage;
        this.phonePrefix = phonePrefix;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public String getPhonePrefix() {
        return phonePrefix;
    }
}
