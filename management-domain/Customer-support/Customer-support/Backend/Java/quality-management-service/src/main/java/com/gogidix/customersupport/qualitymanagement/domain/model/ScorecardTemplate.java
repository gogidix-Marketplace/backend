package com.gogidix.customersupport.qualitymanagement.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * ScorecardTemplate - Domain model representing a QA scorecard template
 *
 * Defines the structure and criteria for evaluating agent interactions,
 * including scoring categories, weights, and passing thresholds.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ScorecardTemplate extends BaseEntity {

    // Template Identification
    @Field("template_id")
    private String templateId;

    @Field("template_name")
    private String templateName;

    @Field("template_code")
    private String templateCode;

    @Field("description")
    private String description;

    // Template Classification
    @Field("template_type")
    private TemplateType templateType;

    @Field("channel_type")
    private QaReview.ChannelType channelType;

    @Field("category")
    private String category;

    @Field("version")
    private String version;

    @Field("is_active")
    private Boolean isActive;

    @Field("is_default")
    private Boolean isDefault;

    // Scoring Configuration
    @Field("max_score")
    private Double maxScore;

    @Field("passing_score")
    private Double passingScore;

    @Field("passing_percentage")
    private Double passingPercentage;

    @Field("weight")
    private Double weight;

    @Field("allow_partial_credit")
    private Boolean allowPartialCredit;

    // Criteria Sections
    @Field("criteria_sections")
    private List<CriteriaSection> criteriaSections;

    @Field("total_criteria_count")
    private Integer totalCriteriaCount;

    // Critical Failure Configuration
    @Field("critical_failure_enabled")
    private Boolean criticalFailureEnabled;

    @Field("critical_failure_threshold")
    private Integer criticalFailureThreshold;

    // Approval and Ownership
    @Field("created_by")
    private String createdBy;

    @Field("created_by_name")
    private String createdByName;

    @Field("approved_by")
    private String approvedBy;

    @Field("approved_at")
    private Instant approvedAt;

    @Field("template_status")
    private TemplateStatus templateStatus;

    // Usage Tracking
    @Field("usage_count")
    private Long usageCount;

    @Field("last_used_at")
    private Instant lastUsedAt;

    // Validity
    @Field("effective_from")
    private Instant effectiveFrom;

    @Field("effective_until")
    private Instant effectiveUntil;

    // Metadata
    @Field("tags")
    private List<String> tags;

    @Field("linked_calibration_sessions")
    private List<String> linkedCalibrationSessions;

    /**
     * Enums for Template Type
     */
    public enum TemplateType {
        CALL_SCORING,
        CHAT_SCORING,
        EMAIL_SCORING,
        GENERAL_SCORING,
        TECHNICAL_SCORING,
        COMPLIANCE_SCORING,
        CUSTOMER_SATISFACTION_SCORING
    }

    /**
     * Enums for Template Status
     */
    public enum TemplateStatus {
        DRAFT,
        PENDING_APPROVAL,
        ACTIVE,
        INACTIVE,
        ARCHIVED,
        DEPRECATED
    }

    /**
     * Inner class for Criteria Section
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CriteriaSection {
        private String sectionId;
        private String sectionName;
        private String description;
        private Integer order;
        private Double weight;
        private Double maxScore;
        private List<Criteria> criteria;
        private Boolean isRequired;
        private String instructions;
    }

    /**
     * Inner class for Criteria
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Criteria {
        private String criteriaId;
        private String criteriaName;
        private String description;
        private String categoryId;
        private Double maxScore;
        private Double weight;
        private Boolean isCritical;
        private Boolean isRequired;
        private Integer order;
        private String scoringGuidance;
        private List<String> examples;
        private List<String> redFlags;
        private CriteriaType criteriaType;

        public enum CriteriaType {
            PASS_FAIL,
            SCALE_1_5,
            SCALE_1_10,
            PERCENTAGE,
            CUSTOM
        }
    }

    /**
     * Constructor with tenant ID
     */
    public ScorecardTemplate(String tenantId) {
        super(tenantId);
        this.templateId = generateTemplateId();
        this.isActive = false;
        this.isDefault = false;
        this.templateStatus = TemplateStatus.DRAFT;
        this.criteriaSections = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.linkedCalibrationSessions = new ArrayList<>();
        this.usageCount = 0L;
        this.allowPartialCredit = true;
        this.criticalFailureEnabled = false;
        this.criticalFailureThreshold = 1;
        this.version = "1.0";
    }

    /**
     * Generate unique template ID
     */
    private String generateTemplateId() {
        return "TPL-" + System.currentTimeMillis() + "-" + (int)(Math.random() * 1000);
    }

    /**
     * Calculate total criteria count
     */
    public void calculateTotalCriteriaCount() {
        if (criteriaSections != null) {
            this.totalCriteriaCount = criteriaSections.stream()
                    .mapToInt(section -> section.getCriteria() != null ? section.getCriteria().size() : 0)
                    .sum();
        }
    }

    /**
     * Check if template is currently valid
     */
    public boolean isValid() {
        Instant now = Instant.now();
        boolean afterEffectiveStart = effectiveFrom == null || !now.isBefore(effectiveFrom);
        boolean beforeEffectiveEnd = effectiveUntil == null || now.isBefore(effectiveUntil);
        return isActive && TemplateStatus.ACTIVE.equals(templateStatus) && afterEffectiveStart && beforeEffectiveEnd;
    }

    /**
     * Check if template requires approval
     */
    public boolean requiresApproval() {
        return TemplateStatus.PENDING_APPROVAL.equals(templateStatus);
    }

    /**
     * Increment usage count
     */
    public void incrementUsageCount() {
        this.usageCount = (this.usageCount != null ? this.usageCount : 0L) + 1;
        this.lastUsedAt = Instant.now();
        updateTimestamp();
    }

    /**
     * Add criteria section
     */
    public void addCriteriaSection(CriteriaSection section) {
        if (this.criteriaSections == null) {
            this.criteriaSections = new ArrayList<>();
        }
        this.criteriaSections.add(section);
        calculateTotalCriteriaCount();
        updateTimestamp();
    }

    /**
     * Add tag
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        this.tags.add(tag);
        updateTimestamp();
    }

    /**
     * Link calibration session
     */
    public void linkCalibrationSession(String sessionId) {
        if (this.linkedCalibrationSessions == null) {
            this.linkedCalibrationSessions = new ArrayList<>();
        }
        if (!this.linkedCalibrationSessions.contains(sessionId)) {
            this.linkedCalibrationSessions.add(sessionId);
            updateTimestamp();
        }
    }

    /**
     * Calculate passing score based on percentage
     */
    public void calculatePassingScore() {
        if (maxScore != null && passingPercentage != null) {
            this.passingScore = maxScore * (passingPercentage / 100.0);
        }
    }

    /**
     * Get all criteria from all sections
     */
    public List<Criteria> getAllCriteria() {
        List<Criteria> allCriteria = new ArrayList<>();
        if (criteriaSections != null) {
            for (CriteriaSection section : criteriaSections) {
                if (section.getCriteria() != null) {
                    allCriteria.addAll(section.getCriteria());
                }
            }
        }
        return allCriteria;
    }

    /**
     * Get criteria by ID
     */
    public Criteria getCriteriaById(String criteriaId) {
        return getAllCriteria().stream()
                .filter(c -> criteriaId.equals(c.getCriteriaId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Check if has critical criteria
     */
    public boolean hasCriticalCriteria() {
        return getAllCriteria().stream().anyMatch(c -> Boolean.TRUE.equals(c.getIsCritical()));
    }
}
