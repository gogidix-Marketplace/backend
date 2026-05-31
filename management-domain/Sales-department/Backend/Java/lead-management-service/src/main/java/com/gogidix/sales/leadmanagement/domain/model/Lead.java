package com.gogidix.sales.leadmanagement.domain.model;

import com.gogidix.sales.leadmanagement.domain.event.LeadCreatedEvent;
import com.gogidix.sales.leadmanagement.shared.base.BaseEntity;
import com.gogidix.sales.leadmanagement.shared.exception.ValidationException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Lead Domain Entity
 * Multi-tenant lead management with scoring, qualification, and assignment
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "leads")
@CompoundIndex(name = "tenant_email_idx", def = "{'tenantId': 1, 'email': 1}", unique = false)
@CompoundIndex(name = "tenant_phone_idx", def = "{'tenantId': 1, 'phone': 1}", unique = false)
public class Lead extends BaseEntity {

    @Indexed
    private String leadId;

    @Indexed
    private String tenantId;

    // Contact Information
    private String firstName;
    private String lastName;
    @Indexed
    private String email;
    @Indexed
    private String phone;
    private String mobilePhone;

    // Professional Information
    private String company;
    private String title;
    private String industry;
    private String companySize;
    private String website;
    private String linkedInUrl;

    // Lead Classification
    private LeadSource source;
    private String sourceDetails;
    private String campaign;
    private LeadStage stage;
    private LeadStatus status;
    private LeadQuality quality;
    private Integer score;

    // Assignment
    private String ownerId;
    private String ownerName;
    @Builder.Default
    private List<String> teamMemberIds = new ArrayList<>();

    // Territory and Segmentation
    private String territory;
    private String region;
    private String segment;

    // Lead Qualification
    private Integer budget;
    private Integer authority;
    private Integer need;
    private Integer timeline;
    private Integer bantScore;

    // Engagement Metrics
    private Integer emailOpens;
    private Integer emailClicks;
    private Integer webVisits;
    private Integer formSubmissions;
    private LocalDate lastActivityDate;
    private LocalDate firstContactDate;

    // Conversion Information
    private String convertedDealId;
    private LocalDate convertedDate;
    private String conversionReason;

    // Loss Information
    private String lossReason;
    private String lossReasonDetails;
    private LocalDate lostDate;

    // Enrichment Data
    private Boolean enriched;
    private LocalDate enrichedDate;
    private String enrichmentSource;

    // Duplicate Detection
    private Boolean isDuplicate;
    private String duplicateOfLeadId;
    private Double duplicateMatchScore;

    // Additional Information
    private String notes;
    private String tags;
    @Builder.Default
    private List<String> tagList = new ArrayList<>();

    // Expected Revenue
    private java.math.BigDecimal estimatedValue;
    private String currency;
    private Integer probability;

    // Timeline
    private LocalDate expectedCloseDate;

    // Relationships
    @DBRef(lazy = true)
    @Builder.Default
    private List<LeadActivity> activities = new ArrayList<>();

    @Transient
    @Builder.Default
    private List<Object> domainEvents = new ArrayList<>();

    /**
     * Lead Source Enum
     */
    public enum LeadSource {
        WEB("Website Form"),
        EMAIL("Email Campaign"),
        SOCIAL("Social Media"),
        EVENT("Event/Conference"),
        REFERRAL("Referral"),
        OUTBOUND("Outbound Sales"),
        PARTNER("Partner Channel"),
        PAID_SEARCH("Paid Advertisement"),
        CONTENT("Content Download"),
        WEBINAR("Webinar Registration"),
        TRIAL("Free Trial Signup"),
        DEMO("Demo Request"),
        OTHER("Other");

        private final String displayName;

        LeadSource(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    /**
     * Lead Stage Enum
     */
    public enum LeadStage {
        NEW(1, 10),
        CONTACTED(2, 25),
        QUALIFIED(3, 50),
        PROPOSAL(4, 75),
        CONVERTED(5, 100),
        LOST(6, 0);

        private final int order;
        private final int defaultProbability;

        LeadStage(int order, int defaultProbability) {
            this.order = order;
            this.defaultProbability = defaultProbability;
        }

        public int getOrder() {
            return order;
        }

        public int getDefaultProbability() {
            return defaultProbability;
        }

        public static LeadStage fromOrder(int order) {
            for (LeadStage stage : values()) {
                if (stage.order == order) {
                    return stage;
                }
            }
            return null;
        }
    }

    /**
     * Lead Status Enum
     */
    public enum LeadStatus {
        ACTIVE,
        CONTACTED,
        ENGAGED,
        STALLED,
        RECYCLED,
        CONVERTED,
        LOST,
        SPAM
    }

    /**
     * Lead Quality Enum
     */
    public enum LeadQuality {
        HOT("High Priority - Immediate Follow-up"),
        WARM("Medium Priority - Nurture Campaign"),
        COLD("Low Priority - Periodic Review"),
        UNQUALIFIED("Does not meet criteria");

        private final String description;

        LeadQuality(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Creates a new lead
     */
    public static Lead create(String tenantId, String firstName, String lastName,
                              String email, String phone, String company,
                              LeadSource source, String ownerId) {
        Lead lead = Lead.builder()
                .tenantId(tenantId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .phone(phone)
                .company(company)
                .source(source)
                .stage(LeadStage.NEW)
                .status(LeadStatus.ACTIVE)
                .ownerId(ownerId)
                .score(50)
                .probability(LeadStage.NEW.getDefaultProbability())
                .activities(new ArrayList<>())
                .teamMemberIds(new ArrayList<>())
                .tagList(new ArrayList<>())
                .build();

        lead.generateLeadId();
        lead.calculateBantScore();
        lead.determineQuality();

        lead.addDomainEvent(LeadCreatedEvent.builder()
                .leadId(lead.getLeadId())
                .tenantId(tenantId)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .company(company)
                .source(source != null ? source.name() : null)
                .ownerId(ownerId)
                .timestamp(Instant.now())
                .eventType("LEAD_CREATED")
                .build());

        return lead;
    }

    /**
     * Advances the lead to the next stage
     */
    public void advanceStage(String userId, String notes) {
        if (this.stage == LeadStage.CONVERTED || this.stage == LeadStage.LOST) {
            throw new ValidationException("Cannot advance stage of " + this.stage + " lead");
        }

        LeadStage nextStage = LeadStage.fromOrder(this.stage.getOrder() + 1);

        if (nextStage == null) {
            throw new ValidationException("Already at final stage");
        }

        LeadStage previousStage = this.stage;
        this.stage = nextStage;
        this.probability = nextStage.getDefaultProbability();

        if (nextStage == LeadStage.CONVERTED) {
            this.status = LeadStatus.CONVERTED;
            this.convertedDate = LocalDate.now();
        } else if (nextStage == LeadStage.LOST) {
            throw new ValidationException("Use markAsLost method to mark lead as lost");
        } else {
            this.status = LeadStatus.CONTACTED;
        }

        addActivity(userId, LeadActivity.ActivityType.STAGE_CHANGE,
                "Stage changed from " + previousStage + " to " + nextStage, notes);
    }

    /**
     * Moves the lead back to a previous stage
     */
    public void regressStage(String userId, LeadStage targetStage, String reason) {
        if (this.stage == LeadStage.CONVERTED || this.stage == LeadStage.LOST) {
            throw new ValidationException("Cannot regress stage of " + this.stage + " lead");
        }

        if (targetStage.getOrder() >= this.stage.getOrder()) {
            throw new ValidationException("Target stage must be before current stage");
        }

        this.stage = targetStage;
        this.probability = targetStage.getDefaultProbability();
        this.status = LeadStatus.ACTIVE;

        addActivity(userId, LeadActivity.ActivityType.STAGE_CHANGE,
                "Stage regressed to " + targetStage, reason);
    }

    /**
     * Marks the lead as converted
     */
    public void markAsConverted(String userId, String dealId, String reason) {
        if (this.stage == LeadStage.CONVERTED) {
            throw new ValidationException("Lead is already converted");
        }

        LeadStage previousStage = this.stage;
        this.stage = LeadStage.CONVERTED;
        this.status = LeadStatus.CONVERTED;
        this.convertedDate = LocalDate.now();
        this.convertedDealId = dealId;
        this.conversionReason = reason;
        this.probability = 100;

        addActivity(userId, LeadActivity.ActivityType.CONVERSION,
                "Lead converted to deal: " + dealId, reason);
    }

    /**
     * Marks the lead as lost
     */
    public void markAsLost(String userId, String lossReason, String lossDetails) {
        if (this.stage == LeadStage.CONVERTED) {
            throw new ValidationException("Cannot mark converted lead as lost");
        }

        LeadStage previousStage = this.stage;
        this.stage = LeadStage.LOST;
        this.status = LeadStatus.LOST;
        this.lostDate = LocalDate.now();
        this.lossReason = lossReason;
        this.lossReasonDetails = lossDetails;
        this.probability = 0;

        addActivity(userId, LeadActivity.ActivityType.STATUS_CHANGE,
                "Lead marked as lost. Reason: " + lossReason, lossDetails);
    }

    /**
     * Assigns the lead to a new owner
     */
    public void assignTo(String newOwnerId, String newOwnerName, String assignedBy, String reason) {
        String previousOwner = this.ownerId;
        this.ownerId = newOwnerId;
        this.ownerName = newOwnerName;

        addActivity(assignedBy, LeadActivity.ActivityType.ASSIGNMENT,
                "Lead assigned from " + previousOwner + " to " + newOwnerName, reason);
    }

    /**
     * Updates the lead score
     */
    public void updateScore(Integer newScore) {
        if (newScore < 0 || newScore > 100) {
            throw new ValidationException("score", "Score must be between 0 and 100");
        }
        this.score = newScore;
        determineQuality();
    }

    /**
     * Adds points to the lead score
     */
    public void addScorePoints(Integer points) {
        Integer newScore = this.score + points;
        this.score = Math.min(100, Math.max(0, newScore));
        determineQuality();
    }

    /**
     * Records an email interaction
     */
    public void recordEmailInteraction(boolean opened, boolean clicked) {
        if (opened) {
            this.emailOpens = (this.emailOpens != null ? this.emailOpens : 0) + 1;
            addScorePoints(2);
        }
        if (clicked) {
            this.emailClicks = (this.emailClicks != null ? this.emailClicks : 0) + 1;
            addScorePoints(5);
        }
        this.lastActivityDate = LocalDate.now();
    }

    /**
     * Records a web visit
     */
    public void recordWebVisit() {
        this.webVisits = (this.webVisits != null ? this.webVisits : 0) + 1;
        this.lastActivityDate = LocalDate.now();
        addScorePoints(1);
    }

    /**
     * Records a form submission
     */
    public void recordFormSubmission(String formName) {
        this.formSubmissions = (this.formSubmissions != null ? this.formSubmissions : 0) + 1;
        this.lastActivityDate = LocalDate.now();
        addScorePoints(10);

        if (this.firstContactDate == null) {
            this.firstContactDate = LocalDate.now();
        }
    }

    /**
     * Marks the lead as a duplicate
     */
    public void markAsDuplicate(String originalLeadId, Double matchScore) {
        this.isDuplicate = true;
        this.duplicateOfLeadId = originalLeadId;
        this.duplicateMatchScore = matchScore;
        this.status = LeadStatus.RECYCLED;
    }

    /**
     * Enriches the lead with additional data
     */
    public void enrich(String enrichmentSource) {
        this.enriched = true;
        this.enrichedDate = LocalDate.now();
        this.enrichmentSource = enrichmentSource;
    }

    /**
     * Adds an activity to the lead
     */
    public void addActivity(String userId, LeadActivity.ActivityType type, String subject, String notes) {
        if (this.activities == null) {
            this.activities = new ArrayList<>();
        }
        LeadActivity activity = LeadActivity.create(this.leadId, this.tenantId, userId, type, subject, notes);
        this.activities.add(activity);
        this.lastActivityDate = LocalDate.now();
    }

    /**
     * Adds a team member
     */
    public void addTeamMember(String userId) {
        if (this.teamMemberIds == null) {
            this.teamMemberIds = new ArrayList<>();
        }
        if (!this.teamMemberIds.contains(userId)) {
            this.teamMemberIds.add(userId);
        }
    }

    /**
     * Removes a team member
     */
    public void removeTeamMember(String userId) {
        if (this.teamMemberIds != null) {
            this.teamMemberIds.remove(userId);
        }
    }

    /**
     * Adds a tag
     */
    public void addTag(String tag) {
        if (this.tagList == null) {
            this.tagList = new ArrayList<>();
        }
        if (!this.tagList.contains(tag)) {
            this.tagList.add(tag);
            updateTagsString();
        }
    }

    /**
     * Removes a tag
     */
    public void removeTag(String tag) {
        if (this.tagList != null) {
            this.tagList.remove(tag);
            updateTagsString();
        }
    }

    /**
     * Recycles the lead (moves back to new stage)
     */
    public void recycle(String userId, String reason) {
        this.stage = LeadStage.NEW;
        this.status = LeadStatus.ACTIVE;
        this.probability = LeadStage.NEW.getDefaultProbability();

        addActivity(userId, LeadActivity.ActivityType.STATUS_CHANGE,
                "Lead recycled", reason);
    }

    /**
     * Calculates BANT score
     */
    public void calculateBantScore() {
        int score = 0;

        if (this.budget != null) score += this.budget;
        if (this.authority != null) score += this.authority;
        if (this.need != null) score += this.need;
        if (this.timeline != null) score += this.timeline;

        this.bantScore = score;
    }

    /**
     * Determines lead quality based on score
     */
    private void determineQuality() {
        if (this.score == null) {
            this.score = 50;
        }

        if (this.score >= 80) {
            this.quality = LeadQuality.HOT;
        } else if (this.score >= 50) {
            this.quality = LeadQuality.WARM;
        } else if (this.score >= 20) {
            this.quality = LeadQuality.COLD;
        } else {
            this.quality = LeadQuality.UNQUALIFIED;
        }
    }

    /**
     * Generates a unique lead ID
     */
    private void generateLeadId() {
        if (this.leadId == null) {
            this.leadId = java.util.UUID.randomUUID().toString();
        }
    }

    /**
     * Updates tags string from list
     */
    private void updateTagsString() {
        if (this.tagList != null && !this.tagList.isEmpty()) {
            this.tags = String.join(",", this.tagList);
        } else {
            this.tags = null;
        }
    }

    /**
     * Adds a domain event
     */
    public void addDomainEvent(Object event) {
        if (this.domainEvents == null) {
            this.domainEvents = new ArrayList<>();
        }
        this.domainEvents.add(event);
    }

    /**
     * Clears domain events
     */
    public void clearDomainEvents() {
        if (this.domainEvents != null) {
            this.domainEvents.clear();
        }
    }

    /**
     * Checks if lead is active
     */
    public boolean isActive() {
        return this.status == LeadStatus.ACTIVE ||
                this.status == LeadStatus.CONTACTED ||
                this.status == LeadStatus.ENGAGED;
    }

    /**
     * Checks if lead is closed (converted or lost)
     */
    public boolean isClosed() {
        return this.stage == LeadStage.CONVERTED || this.stage == LeadStage.LOST;
    }

    /**
     * Gets the full name
     */
    public String getFullName() {
        return (this.firstName != null ? this.firstName : "") +
                (this.lastName != null ? " " + this.lastName : "");
    }
}
