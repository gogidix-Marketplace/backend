package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import lombok.Builder;

import java.time.Instant;
import java.util.*;

@Builder
public class LeadSource {
    private final String name;
    private final LeadChannel channel;
    private final String campaignId;
    private final String referrer;
    private final Double costPerLead;
    private final Double conversionRate;
    private final Instant createdAt;

    private LeadSource(String name, LeadChannel channel, String campaignId,
                      String referrer, Double costPerLead, Double conversionRate,
                      Instant createdAt) {
        if (name == null) {
            throw new IllegalArgumentException("Source name cannot be null");
        }
        if (name.trim().isEmpty()) {
            throw new IllegalArgumentException("Source name cannot be empty");
        }
        if (channel == null) {
            throw new IllegalArgumentException("Channel cannot be null");
        }
        this.name = name;
        this.channel = channel;
        this.campaignId = campaignId;
        this.referrer = referrer;
        this.costPerLead = costPerLead;
        this.conversionRate = conversionRate;
        this.createdAt = createdAt != null ? createdAt : Instant.now();
    }

    public String getName() {
        return name;
    }

    public LeadChannel getChannel() {
        return channel;
    }

    public String getCampaignId() {
        return campaignId;
    }

    public String getReferrer() {
        return referrer;
    }

    public Double getCostPerLead() {
        return costPerLead;
    }

    public Double getConversionRate() {
        return conversionRate;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public double getQualityScore() {
        double baseScore = switch (channel) {
            case REFERRAL -> 85.0;
            case ORGANIC_SEARCH -> 75.0;
            case DIRECT -> 60.0;
            case CONTENT_MARKETING, WEBINAR -> 70.0;
            case EMAIL, EVENT, PARTNER -> 65.0;
            case WEBSITE, SOCIAL_MEDIA -> 55.0;
            case PAID_ADVERTISING, DISPLAY_AD -> 50.0;
            case PHONE -> 60.0;
            case COLD_OUTREACH -> 30.0;
            case OTHER -> 40.0;
        };

        if (conversionRate != null) {
            baseScore += conversionRate * 100;
        }

        return Math.min(100, Math.max(0, baseScore));
    }

    public boolean isOrganic() {
        return channel.isOrganic();
    }

    public boolean isPaid() {
        return channel.isPaid();
    }

    public boolean isHighPerforming() {
        return conversionRate != null && conversionRate > 0.25;
    }

    public LeadSource withConversionData(int visitors, int conversions) {
        double rate = visitors > 0 ? (double) conversions / visitors : 0;
        return new LeadSource(name, channel, campaignId, referrer,
                costPerLead, rate, createdAt);
    }

    public LeadSource withCostData(double totalCost, int leadCount) {
        double cpl = leadCount > 0 ? totalCost / leadCount : 0;
        return new LeadSource(name, channel, campaignId, referrer,
                cpl, conversionRate, createdAt);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeadSource that = (LeadSource) o;
        return Objects.equals(name, that.name) && channel == that.channel;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, channel);
    }
}
