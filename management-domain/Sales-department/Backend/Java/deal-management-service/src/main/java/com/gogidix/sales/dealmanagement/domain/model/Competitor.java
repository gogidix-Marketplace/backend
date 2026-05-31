package com.gogidix.sales.dealmanagement.domain.model;

import com.gogidix.sales.dealmanagement.shared.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

/**
 * Competitor Domain Entity
 * Tracks competitors involved in a deal
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "competitors")
public class Competitor extends BaseEntity {

    @Indexed
    private String competitorId;

    @Indexed
    private String tenantId;

    @Indexed
    private String dealId;

    private String competitorName;

    private String competitorLogo;

    private String competitorWebsite;

    private StrengthLevel strength;

    private ThreatLevel threat;

    private BigDecimal estimatedDealValue;

    private String currency;

    private String competingProduct;

    private String competingProductFeatures;

    private String competitorStrengths;

    private String competitorWeaknesses;

    private String ourAdvantage;

    private String ourDisadvantage;

    private Integer probabilityOfWin;

    private String positioning;

    private String pricingStrategy;

    private String lastUpdateNotes;

    public enum StrengthLevel {
        VERY_WEAK,
        WEAK,
        MODERATE,
        STRONG,
        DOMINANT
    }

    public enum ThreatLevel {
        VERY_LOW,
        LOW,
        MEDIUM,
        HIGH,
        CRITICAL
    }

    /**
     * Creates a new competitor entry
     */
    public static Competitor create(String tenantId, String dealId, String competitorName) {
        Competitor competitor = Competitor.builder()
                .tenantId(tenantId)
                .dealId(dealId)
                .competitorName(competitorName)
                .strength(StrengthLevel.MODERATE)
                .threat(ThreatLevel.MEDIUM)
                .probabilityOfWin(50)
                .build();

        return competitor;
    }

    /**
     * Updates the threat level
     */
    public void updateThreatLevel(ThreatLevel newThreat, String reason) {
        this.threat = newThreat;
        this.lastUpdateNotes = reason;
    }

    /**
     * Updates the strength level
     */
    public void updateStrengthLevel(StrengthLevel newStrength, String reason) {
        this.strength = newStrength;
        this.lastUpdateNotes = reason;
    }

    /**
     * Updates the win probability
     */
    public void updateWinProbability(Integer probability) {
        if (probability < 0 || probability > 100) {
            throw new IllegalArgumentException("Probability must be between 0 and 100");
        }
        this.probabilityOfWin = probability;
    }

    /**
     * Adds competitor strengths
     */
    public void addStrength(String strength) {
        if (this.competitorStrengths == null || this.competitorStrengths.isEmpty()) {
            this.competitorStrengths = strength;
        } else {
            this.competitorStrengths += "; " + strength;
        }
    }

    /**
     * Adds competitor weaknesses
     */
    public void addWeakness(String weakness) {
        if (this.competitorWeaknesses == null || this.competitorWeaknesses.isEmpty()) {
            this.competitorWeaknesses = weakness;
        } else {
            this.competitorWeaknesses += "; " + weakness;
        }
    }

    /**
     * Sets our competitive advantage
     */
    public void setOurAdvantage(String advantage) {
        this.ourAdvantage = advantage;
    }

    /**
     * Sets our competitive disadvantage
     */
    public void setOurDisadvantage(String disadvantage) {
        this.ourDisadvantage = disadvantage;
    }
}
