package com.gogidix.digitalmarketing.leadgeneration.domain.model;

import com.gogidix.digitalmarketing.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * LeadQualification - Qualification criteria and scoring rules
 *
 * <p>Defines how leads are qualified and scored based on various criteria.</p>
 *
 * <p>Qualification criteria include:</p>
 * <ul>
 *   <li>Company size</li>
 *   <li>Budget range</li>
 *   <li>Timeline</li>
 *   <li>Job title/seniority</li>
 *   <li>Industry</li>
 *   <li>Geographic location</li>
 *   <li>Engagement level</li>
 * </ul>
 */
@Document(collection = "lead_qualifications")
@TypeAlias("lead_qualification")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "qual_tenant_active_idx", def = "{'tenantId': 1, 'active': 1}")
@CompoundIndex(name = "qual_tenant_priority_idx", def = "{'tenantId': 1, 'priority': -1}")
public class LeadQualification extends BaseEntity {

    /**
     * Qualification rule name
     */
    @Indexed
    private String name;

    /**
     * Rule description
     */
    private String description;

    /**
     * Whether this rule is active
     */
    @Builder.Default
    private Boolean active = true;

    /**
     * Rule priority (higher = evaluated first)
     */
    @Builder.Default
    private Integer priority = 0;

    /**
     * Minimum score threshold for qualification
     */
    @Builder.Default
    private Integer scoreThreshold = 60;

    /**
     * Company size criteria (ranges with scores)
     */
    private Map<String, Integer> companySizeCriteria;

    /**
     * Budget range criteria (ranges with scores)
     */
    private Map<String, Integer> budgetCriteria;

    /**
     * Timeline criteria (ranges with scores)
     */
    private Map<String, Integer> timelineCriteria;

    /**
     * Job title criteria (titles with scores)
     */
    private Map<String, Integer> jobTitleCriteria;

    /**
     * Industry criteria (industries with scores)
     */
    private Map<String, Integer> industryCriteria;

    /**
     * Geographic criteria (regions with scores)
     */
    private Map<String, Integer> geographicCriteria;

    /**
     * Source criteria (sources with scores)
     */
    private Map<String, Integer> sourceCriteria;

    /**
     * Engagement score criteria (activities with scores)
     */
    private Map<String, Integer> engagementCriteria;

    /**
     * Custom scoring rules
     */
    private Map<String, Object> customRules;

    /**
     * Weight configuration for scoring
     */
    private Map<String, Double> scoringWeights;

    /**
     * Default score for unqualified leads
     */
    @Builder.Default
    private Integer defaultScore = 0;

    /**
     * Maximum possible score
     */
    @Builder.Default
    private Integer maxScore = 100;

    /**
     * Auto-assign to sales when qualified
     */
    @Builder.Default
    private Boolean autoAssignOnQualify = false;

    /**
     * Default sales representative for qualified leads
     */
    private String defaultSalesRepId;

    /**
     * Notification settings for qualified leads
     */
    private Map<String, Object> notificationSettings;

    /**
     * Qualification tags to apply
     */
    private java.util.List<String> qualificationTags;

    /**
     * Disqualification criteria
     */
    private Map<String, Object> disqualificationCriteria;

    /**
     * Last updated timestamp for rules
     */
    private Instant rulesUpdatedAt;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Create a new LeadQualification for a tenant.
     *
     * @param tenantId the tenant ID
     * @param name the rule name
     */
    public LeadQualification(String tenantId, String name) {
        super(tenantId);
        this.name = name;
        this.active = true;
        this.priority = 0;
        this.scoreThreshold = 60;
        this.defaultScore = 0;
        this.maxScore = 100;
        this.autoAssignOnQualify = false;
        this.companySizeCriteria = new HashMap<>();
        this.budgetCriteria = new HashMap<>();
        this.timelineCriteria = new HashMap<>();
        this.jobTitleCriteria = new HashMap<>();
        this.industryCriteria = new HashMap<>();
        this.geographicCriteria = new HashMap<>();
        this.sourceCriteria = new HashMap<>();
        this.engagementCriteria = new HashMap<>();
        this.customRules = new HashMap<>();
        this.scoringWeights = new HashMap<>();
        this.notificationSettings = new HashMap<>();
        this.qualificationTags = new java.util.ArrayList<>();
        this.disqualificationCriteria = new HashMap<>();
        this.metadata = new HashMap<>();
        this.rulesUpdatedAt = Instant.now();
        initializeDefaultWeights();
    }

    /**
     * Initialize default scoring weights.
     */
    private void initializeDefaultWeights() {
        this.scoringWeights.put("companySize", 0.2);
        this.scoringWeights.put("budget", 0.25);
        this.scoringWeights.put("timeline", 0.2);
        this.scoringWeights.put("jobTitle", 0.15);
        this.scoringWeights.put("industry", 0.1);
        this.scoringWeights.put("engagement", 0.1);
    }

    /**
     * Check if rule is active.
     *
     * @return true if rule is active
     */
    public boolean isActive() {
        return this.active != null && this.active;
    }

    /**
     * Calculate lead score based on criteria.
     *
     * @param companySize the company size
     * @param budget the budget
     * @param timeline the timeline
     * @param jobTitle the job title
     * @param industry the industry
     * @param source the lead source
     * @param engagementScore the engagement score
     * @return calculated score
     */
    public int calculateScore(String companySize, String budget, String timeline,
                             String jobTitle, String industry, String source,
                             Integer engagementScore) {
        double totalScore = 0.0;

        // Company size score
        totalScore += getCriteriaScore(companySizeCriteria, companySize) *
                     getScoringWeight("companySize");

        // Budget score
        totalScore += getCriteriaScore(budgetCriteria, budget) *
                     getScoringWeight("budget");

        // Timeline score
        totalScore += getCriteriaScore(timelineCriteria, timeline) *
                     getScoringWeight("timeline");

        // Job title score
        totalScore += getCriteriaScore(jobTitleCriteria, jobTitle) *
                     getScoringWeight("jobTitle");

        // Industry score
        totalScore += getCriteriaScore(industryCriteria, industry) *
                     getScoringWeight("industry");

        // Source score
        totalScore += getCriteriaScore(sourceCriteria, source) *
                     getScoringWeight("source");

        // Engagement score
        if (engagementScore != null && engagementCriteria != null && !engagementCriteria.isEmpty()) {
            String engagementLevel = getEngagementLevel(engagementScore);
            totalScore += getCriteriaScore(engagementCriteria, engagementLevel) *
                         getScoringWeight("engagement");
        }

        return (int) Math.round(Math.min(totalScore, maxScore));
    }

    /**
     * Get score for a criteria map.
     *
     * @param criteria the criteria map
     * @param value the value to look up
     * @return the score or 0 if not found
     */
    private int getCriteriaScore(Map<String, Integer> criteria, String value) {
        if (criteria == null || value == null || value.isBlank()) {
            return 0;
        }
        return criteria.getOrDefault(value, 0);
    }

    /**
     * Get scoring weight.
     *
     * @param key the weight key
     * @return the weight value or 0
     */
    private double getScoringWeight(String key) {
        if (scoringWeights == null) {
            return 0.0;
        }
        return scoringWeights.getOrDefault(key, 0.0);
    }

    /**
     * Get engagement level from score.
     *
     * @param score the engagement score
     * @return engagement level string
     */
    private String getEngagementLevel(int score) {
        if (score >= 80) {
            return "HIGH";
        } else if (score >= 50) {
            return "MEDIUM";
        } else {
            return "LOW";
        }
    }

    /**
     * Check if lead meets qualification threshold.
     *
     * @param score the lead score
     * @return true if qualified
     */
    public boolean isQualified(int score) {
        return score >= this.scoreThreshold;
    }

    /**
     * Check if lead should be disqualified.
     *
     * @param leadData the lead data map
     * @return true if should disqualify
     */
    public boolean shouldDisqualify(Map<String, Object> leadData) {
        if (disqualificationCriteria == null || disqualificationCriteria.isEmpty()) {
            return false;
        }

        // Check email domain blocks
        @SuppressWarnings("unchecked")
        java.util.List<String> blockedDomains = (java.util.List<String>) disqualificationCriteria.get("blockedEmailDomains");
        if (blockedDomains != null && !blockedDomains.isEmpty()) {
            String email = (String) leadData.get("email");
            if (email != null) {
                for (String domain : blockedDomains) {
                    if (email.toLowerCase().endsWith("@" + domain.toLowerCase())) {
                        return true;
                    }
                }
            }
        }

        // Check country blocks
        @SuppressWarnings("unchecked")
        java.util.List<String> blockedCountries = (java.util.List<String>) disqualificationCriteria.get("blockedCountries");
        if (blockedCountries != null && !blockedCountries.isEmpty()) {
            String country = (String) leadData.get("country");
            if (country != null && blockedCountries.contains(country.toUpperCase())) {
                return true;
            }
        }

        // Check opt-out
        Boolean optOut = (Boolean) leadData.get("optOut");
        if (optOut != null && optOut) {
            return true;
        }

        return false;
    }

    /**
     * Add company size criteria.
     *
     * @param size the size category
     * @param score the score
     */
    public void addCompanySizeCriteria(String size, int score) {
        if (this.companySizeCriteria == null) {
            this.companySizeCriteria = new HashMap<>();
        }
        this.companySizeCriteria.put(size, score);
        this.markRulesUpdated();
    }

    /**
     * Add budget criteria.
     *
     * @param range the budget range
     * @param score the score
     */
    public void addBudgetCriteria(String range, int score) {
        if (this.budgetCriteria == null) {
            this.budgetCriteria = new HashMap<>();
        }
        this.budgetCriteria.put(range, score);
        this.markRulesUpdated();
    }

    /**
     * Add timeline criteria.
     *
     * @param timeline the timeline
     * @param score the score
     */
    public void addTimelineCriteria(String timeline, int score) {
        if (this.timelineCriteria == null) {
            this.timelineCriteria = new HashMap<>();
        }
        this.timelineCriteria.put(timeline, score);
        this.markRulesUpdated();
    }

    /**
     * Add job title criteria.
     *
     * @param title the job title
     * @param score the score
     */
    public void addJobTitleCriteria(String title, int score) {
        if (this.jobTitleCriteria == null) {
            this.jobTitleCriteria = new HashMap<>();
        }
        this.jobTitleCriteria.put(title, score);
        this.markRulesUpdated();
    }

    /**
     * Add industry criteria.
     *
     * @param industry the industry
     * @param score the score
     */
    public void addIndustryCriteria(String industry, int score) {
        if (this.industryCriteria == null) {
            this.industryCriteria = new HashMap<>();
        }
        this.industryCriteria.put(industry, score);
        this.markRulesUpdated();
    }

    /**
     * Add source criteria.
     *
     * @param source the source
     * @param score the score
     */
    public void addSourceCriteria(String source, int score) {
        if (this.sourceCriteria == null) {
            this.sourceCriteria = new HashMap<>();
        }
        this.sourceCriteria.put(source, score);
        this.markRulesUpdated();
    }

    /**
     * Add engagement criteria.
     *
     * @param level the engagement level
     * @param score the score
     */
    public void addEngagementCriteria(String level, int score) {
        if (this.engagementCriteria == null) {
            this.engagementCriteria = new HashMap<>();
        }
        this.engagementCriteria.put(level, score);
        this.markRulesUpdated();
    }

    /**
     * Set scoring weight.
     *
     * @param key the weight key
     * @param value the weight value
     */
    public void setScoringWeight(String key, double value) {
        if (this.scoringWeights == null) {
            this.scoringWeights = new HashMap<>();
        }
        this.scoringWeights.put(key, value);
        this.markRulesUpdated();
    }

    /**
     * Add qualification tag.
     *
     * @param tag the tag to add
     */
    public void addQualificationTag(String tag) {
        if (this.qualificationTags == null) {
            this.qualificationTags = new java.util.ArrayList<>();
        }
        if (!this.qualificationTags.contains(tag)) {
            this.qualificationTags.add(tag);
            this.touch();
        }
    }

    /**
     * Add custom rule.
     *
     * @param key the rule key
     * @param value the rule value
     */
    public void addCustomRule(String key, Object value) {
        if (this.customRules == null) {
            this.customRules = new HashMap<>();
        }
        this.customRules.put(key, value);
        this.markRulesUpdated();
    }

    /**
     * Add metadata.
     *
     * @param key the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
    }

    /**
     * Mark rules as updated.
     */
    public void markRulesUpdated() {
        this.rulesUpdatedAt = Instant.now();
        this.touch();
    }

    /**
     * Activate this rule.
     */
    public void activate() {
        this.active = true;
        this.touch();
    }

    /**
     * Deactivate this rule.
     */
    public void deactivate() {
        this.active = false;
        this.touch();
    }
}
