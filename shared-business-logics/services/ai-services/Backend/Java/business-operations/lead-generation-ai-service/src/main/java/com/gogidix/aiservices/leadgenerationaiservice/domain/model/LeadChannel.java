package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import lombok.Getter;

@Getter
public enum LeadChannel {
    WEBSITE("website"),
    EMAIL("email"),
    PHONE("phone"),
    SOCIAL_MEDIA("social_media"),
    PAID_ADVERTISING("paid_advertising"),
    ORGANIC_SEARCH("organic_search"),
    REFERRAL("referral"),
    DIRECT("direct"),
    EVENT("event"),
    PARTNER("partner"),
    COLD_OUTREACH("cold_outreach"),
    DISPLAY_AD("display_ad"),
    CONTENT_MARKETING("content_marketing"),
    WEBINAR("webinar"),
    OTHER("other");

    private final String value;

    LeadChannel(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static LeadChannel fromString(String value) {
        for (LeadChannel channel : LeadChannel.values()) {
            if (channel.value.equalsIgnoreCase(value) || channel.name().equalsIgnoreCase(value)) {
                return channel;
            }
        }
        throw new IllegalArgumentException("Unknown lead channel: " + value);
    }

    public boolean isOrganic() {
        return this == ORGANIC_SEARCH || this == REFERRAL || this == DIRECT || this == CONTENT_MARKETING;
    }

    public boolean isPaid() {
        return this == PAID_ADVERTISING || this == DISPLAY_AD || this == SOCIAL_MEDIA;
    }
}
