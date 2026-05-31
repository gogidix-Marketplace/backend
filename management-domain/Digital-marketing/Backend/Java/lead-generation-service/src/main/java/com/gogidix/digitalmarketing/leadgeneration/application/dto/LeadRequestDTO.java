package com.gogidix.digitalmarketing.leadgeneration.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;

/**
 * LeadRequestDTO - Request DTO for creating/updating leads
 *
 * <p>Used for capturing lead information from various sources.</p>
 */
@Schema(description = "Request DTO for creating or updating leads")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LeadRequestDTO {

    @Schema(description = "Lead's first name", example = "John")
    @JsonProperty("first_name")
    private String firstName;

    @Schema(description = "Lead's last name", example = "Doe")
    @JsonProperty("last_name")
    private String lastName;

    @Schema(description = "Lead's email address (required)", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Schema(description = "Lead's phone number", example = "+1-555-1234567")
    @Pattern(regexp = "^$|^\\+?[1-9]\\d{1,14}$", message = "Phone number must be valid")
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

    @Schema(description = "Country code (ISO 3166-1 alpha-2)", example = "US")
    @Pattern(regexp = "^[A-Z]{2}$", message = "Country code must be 2 uppercase letters")
    private String country;

    @Schema(description = "Region/State", example = "California")
    private String region;

    @Schema(description = "Lead source", example = "WEBSITE")
    private String source;

    @Schema(description = "Source detail (e.g., campaign name)", example = "Summer Campaign 2024")
    @JsonProperty("source_detail")
    private String sourceDetail;

    @Schema(description = "Estimated budget", example = "$10,000 - $50,000")
    private String budget;

    @Schema(description = "Purchase timeline", example = "1-3 months")
    private String timeline;

    @Schema(description = "Assigned sales representative ID", example = "rep-123")
    @JsonProperty("assigned_to")
    private String assignedTo;

    @Schema(description = "Notes and comments")
    private String notes;

    @Schema(description = "Custom fields for lead-specific data")
    @JsonProperty("custom_fields")
    private Map<String, String> customFields;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "GDPR consent granted")
    @JsonProperty("consent_granted")
    private Boolean consentGranted;

    @Schema(description = "Campaign ID (if from specific campaign)", example = "campaign-456")
    @JsonProperty("campaign_id")
    private String campaignId;

    @Schema(description = "Referring lead ID (if from referral)", example = "lead-789")
    @JsonProperty("referred_by")
    private String referredBy;

    @Schema(description = "Lead tags")
    private java.util.List<String> tags;

    @Schema(description = "UTM parameters for tracking")
    private Map<String, String> utmParameters;

    @Schema(description = "Referring URL")
    @JsonProperty("referrer_url")
    private String referrerUrl;

    @Schema(description = "Landing page URL")
    @JsonProperty("landing_page_url")
    private String landingPageUrl;

    @Schema(description = "IP address of the lead")
    @JsonProperty("ip_address")
    private String ipAddress;

    @Schema(description = "User agent string")
    @JsonProperty("user_agent")
    private String userAgent;

    @Schema(description = "Lead capture timestamp")
    @JsonProperty("captured_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant capturedAt;
}
