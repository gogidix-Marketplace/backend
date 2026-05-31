package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;

/**
 * DTO for Scorecard Template operations
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScorecardTemplateDto {

    private String id;
    private String tenantId;
    private String templateId;

    // Basic Info
    @NotBlank(message = "Template name is required")
    private String templateName;

    private String templateCode;
    private String description;

    // Classification
    private String templateType;
    private String channelType;
    private String category;
    private String version;
    private Boolean isActive;
    private Boolean isDefault;

    // Scoring
    @NotNull(message = "Max score is required")
    private Double maxScore;

    private Double passingScore;
    private Double passingPercentage;
    private Double weight;
    private Boolean allowPartialCredit;

    // Criteria
    private List<CriteriaSectionDto> criteriaSections;
    private Integer totalCriteriaCount;

    // Critical Failure
    private Boolean criticalFailureEnabled;
    private Integer criticalFailureThreshold;

    // Ownership
    private String createdBy;
    private String createdByName;
    private String approvedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant approvedAt;

    private String templateStatus;

    // Usage
    private Long usageCount;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant lastUsedAt;

    // Validity
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant effectiveFrom;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant effectiveUntil;

    // Metadata
    private List<String> tags;
    private List<String> linkedCalibrationSessions;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    /**
     * DTO for Criteria Section
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CriteriaSectionDto {
        private String sectionId;
        private String sectionName;
        private String description;
        private Integer order;
        private Double weight;
        private Double maxScore;
        private List<CriteriaDto> criteria;
        private Boolean isRequired;
        private String instructions;
    }

    /**
     * DTO for Criteria
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class CriteriaDto {
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
        private String criteriaType;
    }

    /**
     * Request DTO for creating Scorecard Template
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateScorecardTemplateRequest {
        @NotBlank(message = "Tenant ID is required")
        private String tenantId;

        @NotBlank(message = "Template name is required")
        private String templateName;

        private String templateCode;
        private String description;

        @NotBlank(message = "Template type is required")
        private String templateType;

        private String channelType;
        private String category;

        @NotNull(message = "Max score is required")
        private Double maxScore;

        private Double passingPercentage;
        private Double weight;
        private Boolean allowPartialCredit;

        @NotNull(message = "At least one criteria section is required")
        private List<CriteriaSectionDto> criteriaSections;

        private Boolean criticalFailureEnabled;
        private Integer criticalFailureThreshold;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant effectiveFrom;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant effectiveUntil;

        private List<String> tags;
    }

    /**
     * Request DTO for updating Scorecard Template
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UpdateScorecardTemplateRequest {
        private String templateName;
        private String description;
        private String templateType;
        private String channelType;
        private String category;
        private Double maxScore;
        private Double passingPercentage;
        private Double weight;
        private Boolean allowPartialCredit;
        private List<CriteriaSectionDto> criteriaSections;
        private Boolean criticalFailureEnabled;
        private Integer criticalFailureThreshold;
        private Instant effectiveFrom;
        private Instant effectiveUntil;
        private List<String> tags;
    }

    /**
     * Response DTO for Scorecard Template summary
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScorecardTemplateSummaryDto {
        private String templateId;
        private String templateName;
        private String templateCode;
        private String templateType;
        private String channelType;
        private Double maxScore;
        private Double passingScore;
        private String templateStatus;
        private Boolean isActive;
        private Boolean isDefault;
        private Long usageCount;

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
        private Instant lastUsedAt;

        private Integer totalCriteriaCount;
        private Boolean hasCriticalCriteria;
        private Boolean isValid;
    }
}
