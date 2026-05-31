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
 * LeadAssignment - Assignment rules and tracking for leads to sales teams
 *
 * <p>Manages how leads are automatically assigned to sales representatives
 * based on rules, round-robin, or manual assignment.</p>
 *
 * <p>Assignment strategies:</p>
 * <ul>
 *   <li>ROUND_ROBIN - Distribute evenly among available reps</li>
 *   <li>LEAST_LOADED - Assign to rep with fewest active leads</li>
 *   <li>GEOGRAPHIC - Assign based on lead location</li>
 *   <li>INDUSTRY_EXPERT - Assign based on rep expertise</li>
 *   <li>MANUAL - Manual assignment by marketing team</li>
 *   <li>SCORE_BASED - High score leads to senior reps</li>
 * </ul>
 */
@Document(collection = "lead_assignments")
@TypeAlias("lead_assignment")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "assign_tenant_lead_idx", def = "{'tenantId': 1, 'leadId': 1, 'assignedAt': -1}")
@CompoundIndex(name = "assign_tenant_rep_idx", def = "{'tenantId': 1, 'assignedSalesRepId': 1, 'status': 1}")
@CompoundIndex(name = "assign_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'assignedAt': -1}")
public class LeadAssignment extends BaseEntity {

    /**
     * Lead ID being assigned
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
     * Sales representative ID assigned to
     */
    @Indexed
    private String assignedSalesRepId;

    /**
     * Sales representative name
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
     * Assignment status (ACTIVE, REASSIGNED, UNASSIGNED, RETURNED)
     */
    @Indexed
    private String status;

    /**
     * Assignment strategy used (ROUND_ROBIN, LEAST_LOADED, GEOGRAPHIC, etc.)
     */
    private String assignmentStrategy;

    /**
     * Assignment date
     */
    private Instant assignedAt;

    /**
     * Assigned by user ID (system or manual)
     */
    private String assignedBy;

    /**
     * Assigned by user role (SYSTEM, ADMIN, MARKETING_MANAGER)
     */
    private String assignedByRole;

    /**
     * Reassignment date (if reassigned)
     */
    private Instant reassignedAt;

    /**
     * Previous sales rep ID (if reassigned)
     */
    private String previousSalesRepId;

    /**
     * Reassignment reason (if reassigned)
     */
    private String reassignmentReason;

    /**
     * Assignment priority (HIGH, MEDIUM, LOW)
     */
    private String priority;

    /**
     * Lead score at assignment
     */
    private Integer leadScore;

    /**
     * Lead temperature at assignment
     */
    private String leadTemperature;

    /**
     * Expected response time (SLA in hours)
     */
    private Integer expectedResponseHours;

    /**
     * Actual response time (in hours)
     */
    private Integer actualResponseHours;

    /**
     * First contact timestamp
     */
    private Instant firstContactAt;

    /**
     * Assignment accepted timestamp
     */
    private Instant acceptedAt;

    /**
     * Assignment declined timestamp
     */
    private Instant declinedAt;

    /**
     * Decline reason (if declined)
     */
    private String declineReason;

    /**
     * Assignment notes
     */
    private String notes;

    /**
     * Assignment rules applied
     */
    private Map<String, Object> rulesApplied;

    /**
     * Geographic criteria matched (if applicable)
     */
    private String geographicMatch;

    /**
     * Industry expertise matched (if applicable)
     */
    private String industryExpertiseMatch;

    /**
     * Lead source at assignment
     */
    private String leadSource;

    /**
     * Campaign ID at assignment
     */
    @Indexed
    private String campaignId;

    /**
     * Assignment metadata
     */
    private Map<String, Object> metadata;

    /**
     * Current workload of assigned rep (number of active leads)
     */
    private Integer repWorkload;

    /**
     * Maximum workload for this rep
     */
    private Integer repMaxWorkload;

    /**
     * Rep availability status (AVAILABLE, BUSY, UNAVAILABLE)
     */
    private String repAvailability;

    /**
     * Assignment tags
     */
    private java.util.List<String> tags;

    /**
     * Follow-up date assigned by sales rep
     */
    private Instant followUpDate;

    /**
     * Number of follow-ups completed
     */
    @Builder.Default
    private Integer followUpCount = 0;

    /**
     * Last follow-up timestamp
     */
    private Instant lastFollowUpAt;

    /**
     * Assignment expiration date (if applicable)
     */
    private Instant expiresAt;

    /**
     * Auto-reassign if not contacted by expiration
     */
    @Builder.Default
    private Boolean autoReassign = false;

    /**
     * Create a new LeadAssignment.
     *
     * @param tenantId the tenant ID
     * @param leadId the lead ID
     * @param assignedSalesRepId the sales rep ID
     */
    public LeadAssignment(String tenantId, String leadId, String assignedSalesRepId) {
        super(tenantId);
        this.leadId = leadId;
        this.assignedSalesRepId = assignedSalesRepId;
        this.status = "ACTIVE";
        this.assignedAt = Instant.now();
        this.priority = "MEDIUM";
        this.followUpCount = 0;
        this.autoReassign = false;
        this.rulesApplied = new HashMap<>();
        this.metadata = new HashMap<>();
        this.tags = new java.util.ArrayList<>();
    }

    /**
     * Create a system assignment.
     *
     * @param tenantId the tenant ID
     * @param leadId the lead ID
     * @param leadEmail the lead email
     * @param assignedSalesRepId the sales rep ID
     * @param strategy the assignment strategy
     * @return new LeadAssignment instance
     */
    public static LeadAssignment createSystemAssignment(String tenantId, String leadId,
                                                        String leadEmail, String assignedSalesRepId,
                                                        String strategy) {
        LeadAssignment assignment = new LeadAssignment(tenantId, leadId, assignedSalesRepId);
        assignment.setLeadEmail(leadEmail);
        assignment.setAssignmentStrategy(strategy);
        assignment.setAssignedBy("SYSTEM");
        assignment.setAssignedByRole("SYSTEM");
        return assignment;
    }

    /**
     * Create a manual assignment.
     *
     * @param tenantId the tenant ID
     * @param leadId the lead ID
     * @param leadEmail the lead email
     * @param assignedSalesRepId the sales rep ID
     * @param assignedBy the assigning user
     * @return new LeadAssignment instance
     */
    public static LeadAssignment createManualAssignment(String tenantId, String leadId,
                                                       String leadEmail, String assignedSalesRepId,
                                                       String assignedBy) {
        LeadAssignment assignment = new LeadAssignment(tenantId, leadId, assignedSalesRepId);
        assignment.setLeadEmail(leadEmail);
        assignment.setAssignmentStrategy("MANUAL");
        assignment.setAssignedBy(assignedBy);
        assignment.setAssignedByRole("MARKETING_MANAGER");
        return assignment;
    }

    /**
     * Check if assignment is active.
     *
     * @return true if status is ACTIVE
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Check if assignment is reassigned.
     *
     * @return true if status is REASSIGNED
     */
    public boolean isReassigned() {
        return "REASSIGNED".equals(this.status);
    }

    /**
     * Check if assignment is high priority.
     *
     * @return true if priority is HIGH
     */
    public boolean isHighPriority() {
        return "HIGH".equals(this.priority);
    }

    /**
     * Check if rep is available.
     *
     * @return true if rep availability is AVAILABLE
     */
    public boolean isRepAvailable() {
        return "AVAILABLE".equals(this.repAvailability);
    }

    /**
     * Check if assignment is expired.
     *
     * @return true if expiresAt is past
     */
    public boolean isExpired() {
        return this.expiresAt != null && Instant.now().isAfter(this.expiresAt);
    }

    /**
     * Check if rep is at max workload.
     *
     * @return true if workload is at or above max
     */
    public boolean isRepAtMaxWorkload() {
        if (this.repWorkload == null || this.repMaxWorkload == null) {
            return false;
        }
        return this.repWorkload >= this.repMaxWorkload;
    }

    /**
     * Reassign to a different sales rep.
     *
     * @param newSalesRepId the new sales rep ID
     * @param newSalesRepName the new sales rep name
     * @param reason the reassignment reason
     * @param reassignedBy the user reassigning
     */
    public void reassign(String newSalesRepId, String newSalesRepName, String reason, String reassignedBy) {
        this.previousSalesRepId = this.assignedSalesRepId;
        this.assignedSalesRepId = newSalesRepId;
        this.assignedSalesRepName = newSalesRepName;
        this.reassignmentReason = reason;
        this.reassignedAt = Instant.now();
        this.assignedBy = reassignedBy;
        this.status = "REASSIGNED";
        this.touch();
    }

    /**
     * Accept the assignment.
     *
     * @param acceptedBy the accepting user
     */
    public void accept(String acceptedBy) {
        this.acceptedAt = Instant.now();
        this.assignedBy = acceptedBy;
        this.status = "ACTIVE";
        this.touch();
    }

    /**
     * Decline the assignment.
     *
     * @param declinedBy the declining user
     * @param reason the decline reason
     */
    public void decline(String declinedBy, String reason) {
        this.declinedAt = Instant.now();
        this.declineReason = reason;
        this.assignedBy = declinedBy;
        this.status = "UNASSIGNED";
        this.touch();
    }

    /**
     * Record first contact.
     */
    public void recordFirstContact() {
        if (this.firstContactAt == null) {
            this.firstContactAt = Instant.now();
            if (this.assignedAt != null) {
                long hours = java.time.Duration.between(this.assignedAt, this.firstContactAt).toHours();
                this.actualResponseHours = (int) hours;
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
     * Update rep workload.
     *
     * @param currentWorkload the current workload
     * @param maxWorkload the maximum workload
     */
    public void updateRepWorkload(int currentWorkload, int maxWorkload) {
        this.repWorkload = currentWorkload;
        this.repMaxWorkload = maxWorkload;

        // Update availability based on workload
        if (currentWorkload >= maxWorkload) {
            this.repAvailability = "UNAVAILABLE";
        } else if (currentWorkload >= maxWorkload * 0.8) {
            this.repAvailability = "BUSY";
        } else {
            this.repAvailability = "AVAILABLE";
        }
        this.touch();
    }

    /**
     * Set lead score and temperature.
     *
     * @param score the lead score
     * @param temperature the lead temperature
     */
    public void setLeadScoreAndTemperature(Integer score, String temperature) {
        this.leadScore = score;
        this.leadTemperature = temperature;

        // Auto-set priority based on score
        if (score != null) {
            if (score >= 80) {
                this.priority = "HIGH";
            } else if (score >= 60) {
                this.priority = "MEDIUM";
            } else {
                this.priority = "LOW";
            }
        }
        this.touch();
    }

    /**
     * Add assignment tag.
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
     * Add rule applied.
     *
     * @param key the rule key
     * @param value the rule value
     */
    public void addRuleApplied(String key, Object value) {
        if (this.rulesApplied == null) {
            this.rulesApplied = new HashMap<>();
        }
        this.rulesApplied.put(key, value);
        this.touch();
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
     * Set follow-up date.
     *
     * @param date the follow-up date
     */
    public void setFollowUpDate(Instant date) {
        this.followUpDate = date;
        this.touch();
    }

    /**
     * Mark as expired.
     */
    public void markAsExpired() {
        this.status = "UNASSIGNED";
        this.expiresAt = Instant.now();
        this.touch();
    }

    /**
     * Enable auto-reassignment on expiration.
     *
     * @param enabled whether to auto-reassign
     */
    public void setAutoReassign(boolean enabled) {
        this.autoReassign = enabled;
        this.touch();
    }

    /**
     * Calculate response time compliance.
     *
     * @return true if response was within expected time
     */
    public boolean isResponseCompliant() {
        if (this.expectedResponseHours == null || this.actualResponseHours == null) {
            return true;
        }
        return this.actualResponseHours <= this.expectedResponseHours;
    }
}
