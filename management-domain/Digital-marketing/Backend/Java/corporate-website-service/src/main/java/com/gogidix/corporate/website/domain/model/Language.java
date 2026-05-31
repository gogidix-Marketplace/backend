package com.gogidix.corporate.website.domain.model;

public enum Language {
    EN("English"),
    FR("French"),
    ES("Spanish"),
    PT("Portuguese"),
    AR("Arabic");

    private final String displayName;

    Language(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
