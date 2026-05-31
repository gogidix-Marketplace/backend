package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import lombok.Getter;

@Getter
public enum LeadTier {
    HOT_LEAD("hot_lead", 90),
    HIGH_QUALITY("high_quality", 70),
    MEDIUM_QUALITY("medium_quality", 50),
    LOW_QUALITY("low_quality", 0);

    private final String value;
    private final int minScore;

    LeadTier(String value, int minScore) {
        this.value = value;
        this.minScore = minScore;
    }

    @Override
    public String toString() {
        return value;
    }

    public static LeadTier fromScore(double score) {
        if (score >= HOT_LEAD.minScore) {
            return HOT_LEAD;
        } else if (score >= HIGH_QUALITY.minScore) {
            return HIGH_QUALITY;
        } else if (score >= MEDIUM_QUALITY.minScore) {
            return MEDIUM_QUALITY;
        } else {
            return LOW_QUALITY;
        }
    }

    public static LeadTier fromString(String value) {
        for (LeadTier tier : LeadTier.values()) {
            if (tier.value.equalsIgnoreCase(value)) {
                return tier;
            }
        }
        throw new IllegalArgumentException("Unknown lead tier: " + value);
    }
}
