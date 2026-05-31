package com.gogidix.aiservices.aicontentgenerationservice.domain.model;

import lombok.Getter;

@Getter
public enum ContentTone {
    PROFESSIONAL("professional", "Formal business tone"),
    CASUAL("casual", "Relaxed and friendly tone"),
    PLAYFUL("playful", "Fun and entertaining tone"),
    AUTHORITATIVE("authoritative", "Expert and trustworthy tone"),
    EMPATHETIC("empathetic", "Understanding and caring tone"),
    PERSUASIVE("persuasive", "Convincing and sales-oriented tone"),
    NEUTRAL("neutral", "Objective and balanced tone"),
    ENTHUSIASTIC("enthusiastic", "Excited and energetic tone");

    private final String value;
    private final String description;

    ContentTone(String value, String description) {
        this.value = value;
        this.description = description;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ContentTone fromString(String value) {
        for (ContentTone tone : ContentTone.values()) {
            if (tone.value.equalsIgnoreCase(value)) {
                return tone;
            }
        }
        throw new IllegalArgumentException("Unknown content tone: " + value);
    }
}
