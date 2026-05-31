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
 * LeadHandoff - Handoff records for leads transferred to Sales teams
 *
 * <p>Tracks when leads are handed off from Marketing to Sales,
 * including handoff status, acceptance, and feedback.</p>
 *
 * <p>Handoff states:</p>
 * <ul>
 *   <li>PENDING - Handoff initiated, awaiting acceptance</li>
 *   <li>ACCEPTED - Sales team accepted the lead</li>
 *   <li>REJECTED - Sales team rejected the lead</li>
 *   <li>RETURNED - Lead returned to marketing for more nurturing</li>
 *   <li>CONVERTED - Lead converted to customer</li>
 * </ul>
 */
@Document(collection = "lead_handoffs")
@TypeAlias("lead_handoff")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "handoff_tenant_lead_idx", def = "{'tenantId': 1, 'leadId': 1, 'initiatedAt': -1}")
@CompoundIndex(name = "handoff_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'initiatedAt': -1}")
@CompoundIndex(name = "handoff_tenant_sales_idx", def = "{'tenantId': 1, 'assignedSalesRepId': 1, 'status': 1}")
public class LeadHandoff extends BaseEntity {

    /**
     * Lead ID being handed off
     */
    @Indexed
    private String leadId;

    /**
     * Lead email (for reference)
     */
    @Indexed
    private String leadEmail;

    /**
     * Lead name (for reference)
     */
    private String leadName;

    /**
     * Handoff status (PENDING, ACCEPTED, REJECTED, RETURNED, CONVERTED)
     */
    @Indexed
    private String status;

    /**
     * Assigned sales representative ID
     */
    @Indexed
    private String assignedSalesRepId;

    /**
     * Assigned sales representative name
     */
    private String assignedSalesRepName;

    /**
     * Sales team ID
     */
    @Indexed
    private String salesTeamId;

    /**
     * Sales team name
     */
    private String salesTeamName;

    /**
     * Handoff initiation timestamp
     */
    private Instant initiatedAt;

    /**
     * Handoff accepted timestamp
     */
    private Instant acceptedAt;

    /**
     * Handoff rejected timestamp
     */
    private Instant rejectedAt;

    /**
     * Handoff returned timestamp
     */
    private Instant returnedAt;

    /**
     * Lead converted timestamp
     */
    private Instant convertedAt;

    /**
     * Initiated by user ID
     */
    private String initiatedBy;

    /**
     * Initiated by user role
     */
    private String initiatedByRole;

    /**
     * Handoff priority (HIGH, MEDIUM, LOW)
     */
    private String priority;

    /**
     * Lead qualification score at handoff
     */
    private Integer qualificationScore;

    /**
     * Lead temperature at handoff (HOT, WARM, COLD)
     */
    private String temperature;

    /**
     * Rejection reason (if rejected)
     */
    private String rejectionReason;

    /**
     * Return reason (if returned to marketing)
     */
    private String returnReason;

    /**
     * Suggested actions for returned leads
     */
    private java.util.List<String> suggestedActions;

    /**
     * Follow-up date suggested by sales
     */
    private Instant suggestedFollowUpDate;

    /**
     * Sales notes on the lead
     */
    private String salesNotes;

    /**
     * Marketing notes provided at handoff
     */
    private String marketingNotes;

    /**
     * Lead activity summary at handoff
     */
    private Map<String, Object> activitySummary;

    /**
     * Lead source information
     */
    private String leadSource;

    /**
     * Campaign ID (if from campaign)
     */
    @Indexed
    private String campaignId;

    /**
     * Handoff method (AUTO, MANUAL, BATCH)
     */
    private String handoffMethod;

    /**
     * Service level agreement (SLA) deadline
     */
    private Instant slaDeadline;

    /**
     * SLA compliance status (MET, MISSED, PENDING)
     */
    private String slaCompliance;

    /**
     * Time to accept (in hours)
     */
    private Long timeToAcceptHours;

    /**
     * Time to first contact (in hours)
     */
    private Long timeToFirstContactHours;

    /**
     * First contact timestamp
     */
    private Instant firstContactAt;

    /**
     * Number of follow-ups made by sales
     */
    @Builder.Default
    private Integer followUpCount = 0;

    /**
     * Last follow-up timestamp
     */
    private Instant lastFollowUpAt;

    /**
     * Conversion value (if converted)
     */
    private java.math.BigDecimal conversionValue;

    /**
     * Currency code for conversion value
     */
    private String currency;

    /**
     * Handoff metadata
     */
    private Map<String, Object> metadata;

    /**
     * External CRM reference ID
     */
    private String crmReferenceId;

    /**
     * Integration status (PENDING, SYNCED, FAILED)
     */
    private String integrationStatus;

    /**
     * Integration error message (if failed)
     */
    private String integrationError;

    /**
     * Handoff tags
     */
    private java.util.List<String> tags;

    /**
     * Lead qualification criteria met
     */
    private java.util.List<String> qualificationCriteriaMet;

    /**
     * Risk level (HIGH, MEDIUM, LOW)
     */
    private String riskLevel;

    /**
     * Create a new LeadHandoff.
     *
     * @param tenantId the tenant ID
     * @param leadId the lead ID
     * @param assignedSalesRepId the sales rep ID
     */
    public LeadHandoff(String tenantId, String leadId, String assignedSalesRepId) {
        super(tenantId);
        this.leadId = leadId;
        this.assignedSalesRepId = assignedSalesRepId;
        this.status = "PENDING";
        this.initiatedAt = Instant.now();
        this.priority = "MEDIUM";
        this.followUpCount = 0;
        this.handoffMethod = "AUTO";
        this.slaCompliance = "PENDING";
        this.integrationStatus = "PENDING";
        this.suggestedActions = new java.util.ArrayList<>();
        this.tags = new java.util.ArrayList<>();
        this.qualificationCriteriaMet = new java.util.ArrayList<>();
        this.activitySummary = new HashMap<>();
        this.metadata = new HashMap<>();
        setDefaultSLADeadline();
    }

    /**
     * Set default SLA deadline (24 hours from initiation).
     */
    private void setDefaultSLADeadline() {
        this.slaDeadline = Instant.now().plusSeconds(24 * 60 * 60);
    }

    /**
     * Create a manual handoff.
     *
     * @param tenantId the tenant ID
     * @param leadId the lead ID
     * @param leadEmail the lead email
     * @param leadName the lead name
     * @param assignedSalesRepId the sales rep ID
     * @param initiatedBy the initiating user
     * @return new LeadHandoff instance
     */
    public static LeadHandoff createManualHandoff(String tenantId, String leadId,
                                                   String leadEmail, String leadName,
                                                   String assignedSalesRepId, String initiatedBy) {
        LeadHandoff handoff = new LeadHandoff(tenantId, leadId, assignedSalesRepId);
        handoff.setLeadEmail(leadEmail);
        handoff.setLeadName(leadName);
        handoff.setInitiatedBy(initiatedBy);
        handoff.setHandoffMethod("MANUAL");
        return handoff;
    }

    /**
     * Check if handoff is pending.
     *
     * @return true if status is PENDING
     */
    public boolean isPending() {
        return "PENDING".equals(this.status);
    }

    /**
     * Check if handoff is accepted.
     *
     * @return true if status is ACCEPTED
     */
    public boolean isAccepted() {
        return "ACCEPTED".equals(this.status);
    }

    /**
     * Check if handoff is rejected.
     *
     * @return true if status is REJECTED
     */
    public boolean isRejected() {
        return "REJECTED".equals(this.status);
    }

    /**
     * Check if handoff is returned.
     *
     * @return true if status is RETURNED
     */
    public boolean isReturned() {
        return "RETURNED".equals(this.status);
    }

    /**
     * Check if handoff is converted.
     *
     * @return true if status is CONVERTED
     */
    public boolean isConverted() {
        return "CONVERTED".equals(this.status);
    }

    /**
     * Check if SLA is met.
     *
     * @return true if SLA compliance is MET
     */
    public boolean isSLAMet() {
        return "MET".equals(this.slaCompliance);
    }

    /**
     * Check if SLA is missed.
     *
     * @return true if SLA compliance is MISSED
     */
    public boolean isSLAMissed() {
        return "MISSED".equals(this.slaCompliance);
    }

    /**
     * Check if handoff is high priority.
     *
     * @return true if priority is HIGH
     */
    public boolean isHighPriority() {
        return "HIGH".equals(this.priority);
    }

    /**
     * Check if lead is hot.
     *
     * @return true if temperature is HOT
     */
    public boolean isHotLead() {
        return "HOT".equals(this.temperature);
    }

    /**
     * Accept the handoff.
     *
     * @param acceptedBy the user accepting
     */
    public void accept(String acceptedBy) {
        this.status = "ACCEPTED";
        this.acceptedAt = Instant.now();
        calculateTimeToAccept();
        updateSLACompliance();
        this.touch();
    }

    /**
     * Reject the handoff.
     *
     * @param rejectedBy the user rejecting
     * @param reason the rejection reason
     */
    public void reject(String rejectedBy, String reason) {
        this.status = "REJECTED";
        this.rejectedAt = Instant.now();
        this.rejectionReason = reason;
        updateSLACompliance();
        this.touch();
    }

    /**
     * Return to marketing.
     *
     * @param returnedBy the user returning
     * @param reason the return reason
     * @param suggestedActions suggested actions
     */
    public void returnToMarketing(String returnedBy, String reason, java.util.List<String> suggestedActions) {
        this.status = "RETURNED";
        this.returnedAt = Instant.now();
        this.returnReason = reason;
        this.suggestedActions = suggestedActions;
        updateSLACompliance();
        this.touch();
    }

    /**
     * Mark as converted.
     *
     * @param value the conversion value
     * @param currency the currency code
     */
    public void markAsConverted(java.math.BigDecimal value, String currency) {
        this.status = "CONVERTED";
        this.convertedAt = Instant.now();
        this.conversionValue = value;
        this.currency = currency;
        this.touch();
    }

    /**
     * Record first contact.
     */
    public void recordFirstContact() {
        if (this.firstContactAt == null) {
            this.firstContactAt = Instant.now();
            if (this.initiatedAt != null) {
                long hours = java.time.Duration.between(this.initiatedAt, this.firstContactAt).toHours();
                this.timeToFirstContactHours = hours;
            }
            this.touch();
        }
    }

    /**
     * Increment follow-up count.
     */
    public void incrementFollowUpCount() {
        this.followUpCount = (this.followUpCount != null ? this.followUpCount : 0) + 1;
        this.lastFollowUpAt = Instant.now();
        this.touch();
    }

    /**
     * Calculate time to accept.
     */
    private void calculateTimeToAccept() {
        if (this.initiatedAt != null && this.acceptedAt != null) {
            long hours = java.time.Duration.between(this.initiatedAt, this.acceptedAt).toHours();
            this.timeToAcceptHours = hours;
        }
    }

    /**
     * Update SLA compliance status.
     */
    public void updateSLACompliance() {
        if (this.slaDeadline == null) {
            return;
        }

        Instant now = Instant.now();
        if (this.acceptedAt != null && this.acceptedAt.isBefore(this.slaDeadline)) {
            this.slaCompliance = "MET";
        } else if ((this.acceptedAt != null && this.acceptedAt.isAfter(this.slaDeadline)) ||
                   (this.rejectedAt != null && this.rejectedAt.isAfter(this.slaDeadline)) ||
                   (this.isPending() && now.isAfter(this.slaDeadline))) {
            this.slaCompliance = "MISSED";
        }
    }

    /**
     * Set custom SLA deadline.
     *
     * @param hours hours from initiation
     */
    public void setSLADeadline(int hours) {
        if (this.initiatedAt != null) {
            this.slaDeadline = this.initiatedAt.plusSeconds(hours * 3600L);
        } else {
            this.slaDeadline = Instant.now().plusSeconds(hours * 3600L);
        }
    }

    /**
     * Add suggested action.
     *
     * @param action the suggested action
     */
    public void addSuggestedAction(String action) {
        if (this.suggestedActions == null) {
            this.suggestedActions = new java.util.ArrayList<>();
        }
        if (!this.suggestedActions.contains(action)) {
            this.suggestedActions.add(action);
            this.touch();
        }
    }

    /**
     * Add qualification criterion met.
     *
     * @param criterion the criterion
     */
    public void addQualificationCriterion(String criterion) {
        if (this.qualificationCriteriaMet == null) {
            this.qualificationCriteriaMet = new java.util.ArrayList<>();
        }
        if (!this.qualificationCriteriaMet.contains(criterion)) {
            this.qualificationCriteriaMet.add(criterion);
            this.touch();
        }
    }

    /**
     * Add tag.
     *
     * @param tag the tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new java.util.ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
            this.touch();
        }
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

    /**
     * Mark integration as synced.
     *
     * @param crmRefId the CRM reference ID
     */
    public void markIntegrationSynced(String crmRefId) {
        this.integrationStatus = "SYNCED";
        this.crmReferenceId = crmRefId;
        this.integrationError = null;
        this.touch();
    }

    /**
     * Mark integration as failed.
     *
     * @param error the error message
     */
    public void markIntegrationFailed(String error) {
        this.integrationStatus = "FAILED";
        this.integrationError = error;
        this.touch();
    }

    /**
     * Calculate risk level based on various factors.
     */
    public void calculateRiskLevel() {
        int riskScore = 0;

        // High time to accept increases risk
        if (this.timeToAcceptHours != null && this.timeToAcceptHours > 24) {
            riskScore += 2;
        }

        // Hot leads have lower risk
        if ("COLD".equals(this.temperature)) {
            riskScore += 2;
        } else if ("HOT".equals(this.temperature)) {
            riskScore -= 1;
        }

        // Low qualification score increases risk
        if (this.qualificationScore != null && this.qualificationScore < 60) {
            riskScore += 2;
        }

        // Set risk level
        if (riskScore >= 4) {
            this.riskLevel = "HIGH";
        } else if (riskScore >= 2) {
            this.riskLevel = "MEDIUM";
        } else {
            this.riskLevel = "LOW";
        }
    }
}
