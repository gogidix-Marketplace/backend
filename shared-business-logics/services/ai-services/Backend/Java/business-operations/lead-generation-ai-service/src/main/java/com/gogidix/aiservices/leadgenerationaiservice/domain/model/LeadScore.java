package com.gogidix.aiservices.leadgenerationaiservice.domain.model;

import lombok.Builder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Builder
public class LeadScore implements Comparable<LeadScore> {
    private final String leadId;
    private double score;
    private final LeadTier tier;
    private final String reason;

    private LeadScore(String leadId, double score, LeadTier tier, String reason) {
        if (leadId == null || leadId.trim().isEmpty()) {
            throw new IllegalArgumentException("Lead ID cannot be null or empty");
        }
        if (score < 0.0 || score > 100.0) {
            throw new IllegalArgumentException("Score must be between 0 and 100");
        }
        this.leadId = leadId;
        this.score = BigDecimal.valueOf(score).setScale(2, RoundingMode.HALF_UP).doubleValue();
        this.tier = tier != null ? tier : LeadTier.fromScore(this.score);
        this.reason = reason;
    }

    public String getLeadId() {
        return leadId;
    }

    public double getScore() {
        return score;
    }

    public LeadTier getTier() {
        return tier;
    }

    public String getReason() {
        return reason;
    }

    public LeadTier getClassifiedTier() {
        return LeadTier.fromScore(this.score);
    }

    public boolean isHighQuality() {
        return score >= 70;
    }

    public boolean isMediumQuality() {
        return score >= 50 && score < 70;
    }

    public boolean isLowQuality() {
        return score < 50;
    }

    public boolean isHotLead() {
        return score >= 90;
    }

    public boolean isActionable() {
        return score >= 50;
    }

    public LeadScore withBoost(double percentage) {
        double newScore = Math.min(100.0, this.score * (1 + percentage / 100));
        return new LeadScore(leadId, newScore, tier, reason);
    }

    public LeadScore withReduction(double percentage) {
        double newScore = Math.max(0.0, this.score * (1 - percentage / 100));
        return new LeadScore(leadId, newScore, tier, reason);
    }

    public LeadScore withAbsoluteScore(double newScore) {
        return new LeadScore(leadId, Math.max(0, Math.min(100, newScore)), tier, reason);
    }

    @Override
    public int compareTo(LeadScore other) {
        return Double.compare(other.score, this.score);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LeadScore leadScore = (LeadScore) o;
        return Objects.equals(leadId, leadScore.leadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leadId);
    }
}
