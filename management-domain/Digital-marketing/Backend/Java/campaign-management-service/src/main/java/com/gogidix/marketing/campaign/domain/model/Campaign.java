package com.gogidix.marketing.campaign.domain.model;

import com.gogidix.marketing.campaign.shared.domain.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Campaign - Main marketing campaign entity
 *
 * <p>Represents a marketing campaign with multi-channel support,
 * budget tracking, and performance monitoring.</p>
 *
 * <p>Campaign Status Transitions:</p>
 * <ul>
 *   <li>DRAFT -> SCHEDULED, CANCELLED</li>
 *   <li>SCHEDULED -> ACTIVE, PAUSED, CANCELLED</li>
 *   <li>ACTIVE -> PAUSED, COMPLETED, CANCELLED</li>
 *   <li>PAUSED -> ACTIVE, CANCELLED</li>
 *   <li>COMPLETED -> ARCHIVED</li>
 *   <li>CANCELLED -> ARCHIVED</li>
 * </ul>
 */
@Document(collection = "campaigns")
@TypeAlias("campaign")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "campaign_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'startDate': -1}")
@CompoundIndex(name = "campaign_tenant_type_idx", def = "{'tenantId': 1, 'campaignType': 1, 'status': 1}")
public class Campaign extends BaseEntity {

    /**
     * Campaign name
     */
    @Indexed
    private String name;

    /**
     * Campaign description
     */
    private String description;

    /**
     * Campaign type (AWARENESS, ACQUISITION, RETENTION, CONVERSION, etc.)
     */
    @Indexed
    private String campaignType;

    /**
     * Campaign scope (GLOBAL, REGIONAL, LOCAL)
     */
    @Indexed
    private String scope;

    /**
     * Target regions for this campaign
     */
    private List<String> regions;

    /**
     * Target countries for this campaign
     */
    private List<String> countries;

    /**
     * Campaign status (DRAFT, SCHEDULED, ACTIVE, PAUSED, COMPLETED, CANCELLED, ARCHIVED)
     */
    @Indexed
    private String status;

    /**
     * Campaign start date
     */
    @Indexed
    private Instant startDate;

    /**
     * Campaign end date
     */
    @Indexed
    private Instant endDate;

    /**
     * Total budget for the campaign
     */
    private BigDecimal totalBudget;

    /**
     * Amount spent so far
     */
    private BigDecimal spentAmount;

    /**
     * Currency code (e.g., USD, EUR, GBP)
     */
    private String currency;

    /**
     * Target audience ID
     */
    @Indexed
    private String targetAudienceId;

    /**
     * Campaign channels (EMAIL, SOCIAL_MEDIA, SMS, etc.)
     */
    private List<String> channels;

    /**
     * Campaign objective
     */
    private String objective;

    /**
     * Key performance indicators
     */
    private Map<String, BigDecimal> kpis;

    /**
     * Campaign owner
     */
    private String owner;

    /**
     * Campaign team members
     */
    private List<String> teamMembers;

    /**
     * Tags for categorization
     */
    private List<String> tags;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Priority level (LOW, MEDIUM, HIGH, URGENT)
     */
    @Builder.Default
    private String priority = "MEDIUM";

    /**
     * Whether this is a template campaign
     */
    @Builder.Default
    private Boolean isTemplate = false;

    /**
     * Parent campaign ID (for sub-campaigns)
     */
    @Indexed
    private String parentCampaignId;

    /**
     * Template ID (if created from template)
     */
    private String templateId;

    /**
     * Campaign approval status (PENDING, APPROVED, REJECTED)
     */
    private String approvalStatus;

    /**
     * Approved by
     */
    private String approvedBy;

    /**
     * Approved at
     */
    private Instant approvedAt;

    /**
     * Last calculated metrics timestamp
     */
    private Instant metricsCalculatedAt;

    /**
     * Create a new Campaign for a tenant.
     *
     * @param tenantId the tenant ID
     * @param name     the campaign name
     * @param type     the campaign type
     */
    public Campaign(String tenantId, String name, String type) {
        super(tenantId);
        this.name = name;
        this.campaignType = type;
        this.status = "DRAFT";
        this.channels = new ArrayList<>();
        this.regions = new ArrayList<>();
        this.countries = new ArrayList<>();
        this.teamMembers = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.kpis = new HashMap<>();
        this.metadata = new HashMap<>();
        this.spentAmount = BigDecimal.ZERO;
        this.priority = "MEDIUM";
        this.isTemplate = false;
    }

    /**
     * Create a new Campaign with dates.
     *
     * @param tenantId the tenant ID
     * @param name      the campaign name
     * @param type      the campaign type
     * @param startDate the start date
     * @param endDate   the end date
     */
    public Campaign(String tenantId, String name, String type, Instant startDate, Instant endDate) {
        this(tenantId, name, type);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * Check if campaign is currently active.
     *
     * @return true if campaign is active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Check if campaign is draft.
     *
     * @return true if campaign is draft
     */
    public boolean isDraft() {
        return "DRAFT".equals(this.status);
    }

    /**
     * Check if campaign is completed.
     *
     * @return true if campaign is completed
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(this.status);
    }

    /**
     * Check if campaign is archived.
     *
     * @return true if campaign is archived
     */
    public boolean isArchived() {
        return "ARCHIVED".equals(this.status);
    }

    /**
     * Check if campaign can be activated.
     *
     * @return true if campaign can transition to ACTIVE
     */
    public boolean canActivate() {
        return "SCHEDULED".equals(this.status) || "PAUSED".equals(this.status);
    }

    /**
     * Check if campaign can be paused.
     *
     * @return true if campaign can be paused
     */
    public boolean canPause() {
        return "ACTIVE".equals(this.status) || "SCHEDULED".equals(this.status);
    }

    /**
     * Check if campaign can be cancelled.
     *
     * @return true if campaign can be cancelled
     */
    public boolean canCancel() {
        return !("COMPLETED".equals(this.status) || "ARCHIVED".equals(this.status) || "CANCELLED".equals(this.status));
    }

    /**
     * Check if campaign can be edited.
     *
     * @return true if campaign can be edited
     */
    public boolean canEdit() {
        return "DRAFT".equals(this.status) || "PAUSED".equals(this.status);
    }

    /**
     * Transition campaign status.
     *
     * @param newStatus the new status
     * @return true if transition is valid
     */
    public boolean transitionTo(String newStatus) {
        if (!isValidTransition(this.status, newStatus)) {
            return false;
        }
        this.status = newStatus;
        this.touch();
        return true;
    }

    /**
     * Validate status transition.
     */
    private boolean isValidTransition(String currentStatus, String newStatus) {
        return switch (currentStatus) {
            case "DRAFT" -> List.of("SCHEDULED", "CANCELLED").contains(newStatus);
            case "SCHEDULED" -> List.of("ACTIVE", "PAUSED", "CANCELLED").contains(newStatus);
            case "ACTIVE" -> List.of("PAUSED", "COMPLETED", "CANCELLED").contains(newStatus);
            case "PAUSED" -> List.of("ACTIVE", "CANCELLED").contains(newStatus);
            case "COMPLETED" -> "ARCHIVED".equals(newStatus);
            case "CANCELLED" -> "ARCHIVED".equals(newStatus);
            case "ARCHIVED" -> false;
            default -> false;
        };
    }

    /**
     * Add a channel to the campaign.
     *
     * @param channel the channel to add
     */
    public void addChannel(String channel) {
        if (this.channels == null) {
            this.channels = new ArrayList<>();
        }
        if (!this.channels.contains(channel)) {
            this.channels.add(channel);
            this.touch();
        }
    }

    /**
     * Remove a channel from the campaign.
     *
     * @param channel the channel to remove
     */
    public void removeChannel(String channel) {
        if (this.channels != null) {
            this.channels.remove(channel);
            this.touch();
        }
    }

    /**
     * Add a region to the campaign.
     *
     * @param region the region to add
     */
    public void addRegion(String region) {
        if (this.regions == null) {
            this.regions = new ArrayList<>();
        }
        if (!this.regions.contains(region)) {
            this.regions.add(region);
            this.touch();
        }
    }

    /**
     * Add a country to the campaign.
     *
     * @param country the country to add
     */
    public void addCountry(String country) {
        if (this.countries == null) {
            this.countries = new ArrayList<>();
        }
        if (!this.countries.contains(country)) {
            this.countries.add(country);
            this.touch();
        }
    }

    /**
     * Add a team member to the campaign.
     *
     * @param userId the user ID to add
     */
    public void addTeamMember(String userId) {
        if (this.teamMembers == null) {
            this.teamMembers = new ArrayList<>();
        }
        if (!this.teamMembers.contains(userId)) {
            this.teamMembers.add(userId);
            this.touch();
        }
    }

    /**
     * Record spending against the campaign budget.
     *
     * @param amount the amount spent
     */
    public void recordSpending(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        if (this.spentAmount == null) {
            this.spentAmount = BigDecimal.ZERO;
        }
        this.spentAmount = this.spentAmount.add(amount);
        this.touch();
    }

    /**
     * Get remaining budget.
     *
     * @return remaining budget or null if no budget set
     */
    public BigDecimal getRemainingBudget() {
        if (this.totalBudget == null || this.spentAmount == null) {
            return this.totalBudget;
        }
        return this.totalBudget.subtract(this.spentAmount);
    }

    /**
     * Check if budget has been exhausted.
     *
     * @return true if spent amount equals or exceeds total budget
     */
    public boolean isBudgetExhausted() {
        if (this.totalBudget == null || this.spentAmount == null) {
            return false;
        }
        return this.spentAmount.compareTo(this.totalBudget) >= 0;
    }

    /**
     * Get budget utilization as percentage.
     *
     * @return percentage spent (0-100) or null if no budget set
     */
    public BigDecimal getBudgetUtilization() {
        if (this.totalBudget == null || this.spentAmount == null || this.totalBudget.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }
        return this.spentAmount.divide(this.totalBudget, 4, java.math.RoundingMode.HALF_UP)
            .multiply(new BigDecimal("100"));
    }

    /**
     * Check if campaign is currently running based on dates.
     *
     * @return true if current date is within start and end dates
     */
    public boolean isCurrentlyRunning() {
        Instant now = Instant.now();
        if (this.startDate != null && now.isBefore(this.startDate)) {
            return false;
        }
        if (this.endDate != null && now.isAfter(this.endDate)) {
            return false;
        }
        return true;
    }

    /**
     * Check if campaign has ended.
     *
     * @return true if end date has passed
     */
    public boolean hasEnded() {
        return this.endDate != null && Instant.now().isAfter(this.endDate);
    }

    /**
     * Check if campaign has started.
     *
     * @return true if start date has passed
     */
    public boolean hasStarted() {
        return this.startDate != null && Instant.now().isAfter(this.startDate);
    }

    /**
     * Set KPI value.
     *
     * @param kpiName the KPI name
     * @param value   the KPI value
     */
    public void setKpi(String kpiName, BigDecimal value) {
        if (this.kpis == null) {
            this.kpis = new HashMap<>();
        }
        this.kpis.put(kpiName, value);
        this.touch();
    }

    /**
     * Get KPI value.
     *
     * @param kpiName the KPI name
     * @return the KPI value or null if not set
     */
    public BigDecimal getKpi(String kpiName) {
        if (this.kpis == null) {
            return null;
        }
        return this.kpis.get(kpiName);
    }

    /**
     * Add metadata to the campaign.
     *
     * @param key   the metadata key
     * @param value the metadata value
     */
    public void addMetadata(String key, Object value) {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        this.metadata.put(key, value);
        this.touch();
    }

    /**
     * Get metadata value.
     *
     * @param key the metadata key
     * @return the metadata value or null if not set
     */
    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }

    /**
     * Mark campaign as approved.
     *
     * @param approvedBy the user who approved
     */
    public void approve(String approvedBy) {
        this.approvalStatus = "APPROVED";
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
        this.touch();
    }

    /**
     * Mark campaign as rejected.
     *
     * @param rejectedBy the user who rejected
     */
    public void reject(String rejectedBy) {
        this.approvalStatus = "REJECTED";
        this.approvedBy = rejectedBy;
        this.approvedAt = Instant.now();
        this.touch();
    }

    /**
     * Check if campaign is approved.
     *
     * @return true if campaign is approved
     */
    public boolean isApproved() {
        return "APPROVED".equals(this.approvalStatus);
    }

    /**
     * Check if approval is pending.
     *
     * @return true if approval is pending
     */
    public boolean isApprovalPending() {
        return "PENDING".equals(this.approvalStatus) || this.approvalStatus == null;
    }

    /**
     * Mark metrics as calculated.
     */
    public void markMetricsAsCalculated() {
        this.metricsCalculatedAt = Instant.now();
        this.touch();
    }

    /**
     * Get campaign duration in days.
     *
     * @return duration in days or null if dates not set
     */
    public Long getDurationInDays() {
        if (this.startDate == null || this.endDate == null) {
            return null;
        }
        long seconds = java.time.Duration.between(this.startDate, this.endDate).getSeconds();
        return seconds / 86400;
    }
}
