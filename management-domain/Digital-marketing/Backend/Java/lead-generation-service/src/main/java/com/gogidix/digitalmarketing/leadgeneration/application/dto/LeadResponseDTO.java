package com.gogidix.digitalmarketing.leadgeneration.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * LeadResponseDTO - Response DTO for lead information
 *
 * <p>Used for returning lead details to API consumers.</p>
 */
@Schema(description = "Response DTO for lead information")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadResponseDTO {

    @Schema(description = "Lead ID", example = "lead-123")
    private String id;

    @Schema(description = "Tenant ID", example = "tenant-456")
    @JsonProperty("tenant_id")
    private String tenantId;

    @Schema(description = "Lead's first name", example = "John")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "Lead's last name", example = "Doe")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "Lead's full name", example = "John Doe")
    @JsonProperty("full_name")
    private String fullName;

    @Schema(description = "Lead's email address", example = "john.doe@example.com")
    private String email;

    @Schema(description = "Lead's phone number", example = "+1-555-1234567")
    private String phone;

    @Schema(description = "Lead's company name", example = "Acme Corporation")
    private String company;

    @Schema(description = "Job title", example = "Marketing Director")
    @JsonProperty("job_title")
    private String jobTitle;

    @Schema(description = "Industry", example = "Technology")
    private String industry;

    @Schema(description = "Company size", example = "MEDIUM")
    @JsonProperty("company_size")
    private String companySize;

    @Schema(description = "Country code", example = "US")
    private String country;

    @Schema(description = "Region/State", example = "California")
    private String region;

    @Schema(description = "Lead source", example = "WEBSITE")
    private String source;

    @Schema(description = "Source detail", example = "Summer Campaign 2024")
    @JsonProperty("source_detail")
    private String sourceDetail;

    @Schema(description = "Lead status", example = "NEW")
    private String status;

    @Schema(description = "Qualification score (0-100)", example = "75")
    private Integer score;

    @Schema(description = "Lead temperature", example = "WARM")
    private String temperature;

    @Schema(description = "Estimated budget", example = "$10,000 - $50,000")
    private String budget;

    @Schema(description = "Purchase timeline", example = "1-3 months")
    private String timeline;

    @Schema(description = "Assigned sales representative ID", example = "rep-123")
    @JsonProperty("assigned_to")
    private String assignedTo;

    @Schema(description = "Assignment date")
    @JsonProperty("assigned_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant assignedAt;

    @Schema(description = "Last activity date")
    @JsonProperty("last_activity_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant lastActivityAt;

    @Schema(description = "Conversion date")
    @JsonProperty("converted_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant convertedAt;

    @Schema(description = "Lost/Rejected reason")
    @JsonProperty("lost_reason")
    private String lostReason;

    @Schema(description = "Notes and comments")
    private String notes;

    @Schema(description = "Custom fields")
    @JsonProperty("custom_fields")
    private Map<String, String> customFields;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Whether lead has opted out")
    @JsonProperty("opt_out")
    private Boolean optOut;

    @Schema(description = "GDPR consent granted")
    @JsonProperty("consent_granted")
    private Boolean consentGranted;

    @Schema(description = "Consent granted date")
    @JsonProperty("consent_granted_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant consentGrantedAt;

    @Schema(description = "Campaign ID", example = "campaign-456")
    @JsonProperty("campaign_id")
    private String campaignId;

    @Schema(description = "Referring lead ID", example = "lead-789")
    @JsonProperty("referred_by")
    private String referredBy;

    @Schema(description = "Number of engagement activities", example = "5")
    @JsonProperty("engagement_count")
    private Integer engagementCount;

    @Schema(description = "Last engagement score", example = "85.5")
    @JsonProperty("last_engagement_score")
    private Double lastEngagementScore;

    @Schema(description = "Creation timestamp")
    @JsonProperty("created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @Schema(description = "Last update timestamp")
    @JsonProperty("updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Schema(description = "Created by user ID")
    @JsonProperty("created_by")
    private String createdBy;

    @Schema(description = "Updated by user ID")
    @JsonProperty("updated_by")
    private String updatedBy;

    @Schema(description = "Lead tags")
    private java.util.List<String> tags;

    @Schema(description = "Is lead qualified?")
    @JsonProperty("is_qualified")
    private Boolean isQualified;

    @Schema(description = "Is lead hot?")
    @JsonProperty("is_hot")
    private Boolean isHot;

    @Schema(description = "Is lead warm?")
    @JsonProperty("is_warm")
    private Boolean isWarm;

    @Schema(description = "Is lead cold?")
    @JsonProperty("is_cold")
    private Boolean isCold;

    @Schema(description = "Is lead assigned?")
    @JsonProperty("is_assigned")
    private Boolean isAssigned;

    @Schema(description = "Is lead converted?")
    @JsonProperty("is_converted")
    private Boolean isConverted;

    @Schema(description = "Is lead lost?")
    @JsonProperty("is_lost")
    private Boolean isLost;

    @Schema(description = "Is lead new?")
    @JsonProperty("is_new")
    private Boolean isNew;
}
