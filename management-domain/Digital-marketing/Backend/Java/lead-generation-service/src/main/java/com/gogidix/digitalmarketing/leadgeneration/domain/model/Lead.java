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
 * Lead - Represents a potential customer
 *
 * <p>Leads are captured from various sources and go through qualification
 * and assignment processes before being handed off to sales teams.</p>
 */
@Document(collection = "leads")
@TypeAlias("lead")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "lead_tenant_email_idx", def = "{'tenantId': 1, 'email': 1}")
@CompoundIndex(name = "lead_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'createdAt': -1}")
@CompoundIndex(name = "lead_tenant_source_idx", def = "{'tenantId': 1, 'source': 1, 'createdAt': -1}")
public class Lead extends BaseEntity {

    public enum LeadStatus {
        NEW, CONTACTED, QUALIFIED, CONVERTED, LOST, NURTURE
    }

    /**
     * Lead's first name
     */
    @Indexed
    private String firstName;

    /**
     * Lead's last name
     */
    @Indexed
    private String lastName;

    /**
     * Lead's email address (primary identifier)
     */
    @Indexed
    private String email;

    /**
     * Lead's phone number
     */
    private String phone;

    /**
     * Lead's company name
     */
    @Indexed
    private String company;

    /**
     * Job title
     */
    private String jobTitle;

    /**
     * Industry
     */
    private String industry;

    /**
     * Company size (number of employees)
     */
    private String companySize;

    /**
     * Country code
     */
    @Indexed
    private String country;

    /**
     * Region/State
     */
    private String region;

    /**
     * Lead source (WEBSITE, EMAIL_CAMPAIGN, SOCIAL_MEDIA, etc.)
     */
    @Indexed
    private String source;

    /**
     * Specific source detail (e.g., campaign name, referrer)
     */
    private String sourceDetail;

    /**
     * Lead status (NEW, CONTACTED, QUALIFIED, CONVERTED, LOST, NURTURE)
     */
    @Indexed
    private String status;

    /**
     * Qualification score (0-100)
     */
    @Indexed
    private Integer score;

    /**
     * Lead temperature (HOT, WARM, COLD)
     */
    @Indexed
    private String temperature;

    /**
     * Estimated budget
     */
    private String budget;

    /**
     * Purchase timeline
     */
    private String timeline;

    /**
     * Assigned sales representative ID
     */
    @Indexed
    private String assignedTo;

    /**
     * Assignment date
     */
    private Instant assignedAt;

    /**
     * Last activity date
     */
    private Instant lastActivityAt;

    /**
     * Conversion date
     */
    private Instant convertedAt;

    /**
     * Lost/Rejected reason
     */
    private String lostReason;

    /**
     * Notes and comments
     */
    private String notes;

    /**
     * Additional metadata as key-value pairs
     */
    private Map<String, Object> metadata;

    /**
     * Custom fields for lead-specific data
     */
    private Map<String, String> customFields;

    /**
     * Whether this lead has opted out of communications
     */
    @Builder.Default
    private Boolean optOut = false;

    /**
     * GDPR consent granted
     */
    @Builder.Default
    private Boolean consentGranted = false;

    /**
     * Consent granted date
     */
    private Instant consentGrantedAt;

    /**
     * Lead campaign ID (if from specific campaign)
     */
    @Indexed
    private String campaignId;

    /**
     * Referring lead ID (if from referral)
     */
    private String referredBy;

    /**
     * Number of engagement activities
     */
    @Builder.Default
    private Integer engagementCount = 0;

    /**
     * Last engagement score
     */
    private Double lastEngagementScore;

    /**
     * Create a new Lead for a tenant.
     *
     * @param tenantId the tenant ID
     * @param email the lead's email
     * @param source the lead source
     */
    public Lead(String tenantId, String email, String source) {
        super(tenantId);
        this.email = email;
        this.source = source;
        this.status = "NEW";
        this.score = 0;
        this.temperature = "COLD";
        this.engagementCount = 0;
        this.optOut = false;
        this.consentGranted = false;
        this.metadata = new HashMap<>();
        this.customFields = new HashMap<>();
    }

    /**
     * Get full name.
     *
     * @return full name or empty string if both first and last name are null
     */
    public String getFullName() {
        if (firstName != null && lastName != null) {
            return firstName + " " + lastName;
        } else if (firstName != null) {
            return firstName;
        } else if (lastName != null) {
            return lastName;
        }
        return "";
    }

    /**
     * Check if lead is qualified.
     *
     * @return true if score is above qualification threshold
     */
    public boolean isQualified() {
        return this.score != null && this.score >= 60;
    }

    /**
     * Check if lead is hot.
     *
     * @return true if temperature is HOT
     */
    public boolean isHot() {
        return "HOT".equals(this.temperature);
    }

    /**
     * Check if lead is warm.
     *
     * @return true if temperature is WARM
     */
    public boolean isWarm() {
        return "WARM".equals(this.temperature);
    }

    /**
     * Check if lead is cold.
     *
     * @return true if temperature is COLD
     */
    public boolean isCold() {
        return "COLD".equals(this.temperature);
    }

    /**
     * Check if lead is assigned.
     *
     * @return true if assignedTo is not null
     */
    public boolean isAssigned() {
        return this.assignedTo != null && !this.assignedTo.isBlank();
    }

    /**
     * Check if lead is converted.
     *
     * @return true if status is CONVERTED
     */
    public boolean isConverted() {
        return "CONVERTED".equals(this.status);
    }

    /**
     * Check if lead is lost.
     *
     * @return true if status is LOST
     */
    public boolean isLost() {
        return "LOST".equals(this.status);
    }

    /**
     * Check if lead is new.
     *
     * @return true if status is NEW
     */
    public boolean isNew() {
        return "NEW".equals(this.status);
    }

    /**
     * Calculate lead temperature based on score.
     */
    public void calculateTemperature() {
        if (this.score == null) {
            this.temperature = "COLD";
            return;
        }

        if (this.score >= 80) {
            this.temperature = "HOT";
        } else if (this.score >= 60) {
            this.temperature = "WARM";
        } else {
            this.temperature = "COLD";
        }
    }

    /**
     * Update lead score.
     *
     * @param newScore the new score
     */
    public void updateScore(Integer newScore) {
        this.score = newScore;
        calculateTemperature();
        this.touch();
    }

    /**
     * Assign to sales representative.
     *
     * @param salesRepId the sales representative ID
     */
    public void assignTo(String salesRepId) {
        this.assignedTo = salesRepId;
        this.assignedAt = Instant.now();
        this.status = "CONTACTED";
        this.touch();
    }

    /**
     * Mark as converted.
     */
    public void markAsConverted() {
        this.status = "CONVERTED";
        this.convertedAt = Instant.now();
        this.touch();
    }

    /**
     * Mark as lost with reason.
     *
     * @param reason the reason for losing the lead
     */
    public void markAsLost(String reason) {
        this.status = "LOST";
        this.lostReason = reason;
        this.touch();
    }

    /**
     * Record activity.
     */
    public void recordActivity() {
        this.engagementCount = (this.engagementCount != null ? this.engagementCount : 0) + 1;
        this.lastActivityAt = Instant.now();
        this.touch();
    }

    /**
     * Grant consent.
     */
    public void grantConsent() {
        this.consentGranted = true;
        this.consentGrantedAt = Instant.now();
        this.touch();
    }

    /**
     * Revoke consent.
     */
    public void revokeConsent() {
        this.consentGranted = false;
        this.optOut = true;
        this.touch();
    }

    /**
     * Add custom field.
     *
     * @param key the field key
     * @param value the field value
     */
    public void addCustomField(String key, String value) {
        if (this.customFields == null) {
            this.customFields = new HashMap<>();
        }
        this.customFields.put(key, value);
    }

    /**
     * Get custom field value.
     *
     * @param key the field key
     * @return the field value, or null if not set
     */
    public String getCustomField(String key) {
        if (this.customFields == null) {
            return null;
        }
        return this.customFields.get(key);
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
     * Get metadata value.
     *
     * @param key the metadata key
     * @return the metadata value, or null if not set
     */
    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }
}
