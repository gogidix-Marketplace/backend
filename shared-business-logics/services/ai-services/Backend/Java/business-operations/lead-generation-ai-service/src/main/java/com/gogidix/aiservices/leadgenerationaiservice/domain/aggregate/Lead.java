package com.gogidix.aiservices.leadgenerationaiservice.domain.aggregate;

import com.gogidix.aiservices.leadgenerationaiservice.domain.event.LeadActivity;
import com.gogidix.aiservices.leadgenerationaiservice.domain.model.*;
import com.gogidix.aiservices.leadgenerationaiservice.shared.exception.LeadGenerationException;

import java.time.Duration;
import java.time.Instant;
import java.util.*;

public class Lead {
    private static final int MAX_ACTIVITIES = 100;
    private static final int STALE_DAYS = 7;
    private static final double MIN_QUALIFIED_SCORE = 50.0;
    private static final int MIN_QUALIFICATION_CRITERIA = 3;

    private final UUID leadId;
    private final ContactInfo contactInfo;
    private LeadStatus status;
    private LeadSource source;
    private LeadScore score;
    private final List<LeadActivity> activities;
    private final Set<QualificationCriteria> qualificationCriteria;
    private final Instant createdAt;
    private Instant updatedAt;
    private String ownerId;
    private String ownerName;
    private Instant assignedAt;
    private LeadLossReason lossReason;
    private final Map<String, Object> customFields;
    private Double conversionValue;
    private String conversionCurrency;

    private Lead(ContactInfo contactInfo) {
        this(UUID.randomUUID(), contactInfo, Instant.now());
    }

    private Lead(UUID leadId, ContactInfo contactInfo, Instant createdAt) {
        if (contactInfo == null) {
            throw new IllegalArgumentException("Contact info cannot be null");
        }
        this.leadId = leadId != null ? leadId : UUID.randomUUID();
        this.contactInfo = contactInfo;
        this.status = LeadStatus.NEW;
        this.activities = new ArrayList<>();
        this.qualificationCriteria = new HashSet<>();
        this.createdAt = createdAt != null ? createdAt : Instant.now();
        this.updatedAt = Instant.now();
        this.customFields = new HashMap<>();
    }

    public static Lead create(ContactInfo contactInfo) {
        return new Lead(contactInfo);
    }

    public static Lead reconstruct(UUID leadId, ContactInfo contactInfo, LeadStatus status,
            Instant createdAt) {
        Lead lead = new Lead(leadId, contactInfo, createdAt);
        if (status != null) lead.status = status;
        return lead;
    }

    // Getters
    public UUID getLeadId() {
        return leadId;
    }

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public LeadStatus getStatus() {
        return status;
    }

    public LeadSource getSource() {
        return source;
    }

    public LeadScore getScore() {
        return score;
    }

    public List<LeadActivity> getActivities() {
        return Collections.unmodifiableList(activities);
    }

    public Set<QualificationCriteria> getQualificationCriteria() {
        return Collections.unmodifiableSet(qualificationCriteria);
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public Instant getAssignedAt() {
        return assignedAt;
    }

    public LeadLossReason getLossReason() {
        return lossReason;
    }

    public Map<String, Object> getCustomFields() {
        return Collections.unmodifiableMap(customFields);
    }

    // Setters for infrastructure use
    public void setSource(LeadSource source) {
        this.source = source;
        touch();
    }

    public void setScore(LeadScore score) {
        this.score = score;
        touch();
    }

    // Business methods
    public void markAsContacted() {
        if (status == LeadStatus.LOST || status == LeadStatus.CONVERTED) {
            throw new LeadGenerationException("Cannot transition from " + status);
        }
        this.status = LeadStatus.CONTACTED;
        touch();
    }

    public void markAsQualified() {
        if (status != LeadStatus.CONTACTED && status != LeadStatus.NEW) {
            throw new LeadGenerationException("Only NEW or CONTACTED leads can be marked as qualified");
        }
        this.status = LeadStatus.QUALIFIED;
        touch();
    }

    public void markAsConverted() {
        if (status != LeadStatus.QUALIFIED) {
            throw new LeadGenerationException("Lead must be qualified before conversion");
        }
        this.status = LeadStatus.CONVERTED;
        touch();
    }

    public void markAsLost(LeadLossReason reason) {
        if (status == LeadStatus.CONVERTED) {
            throw new LeadGenerationException("Cannot mark converted lead as lost");
        }
        this.status = LeadStatus.LOST;
        this.lossReason = reason;
        touch();
    }

    public void addActivity(LeadActivity activity) {
        if (activity == null) {
            throw new IllegalArgumentException("Activity cannot be null");
        }

        // Check for duplicates
        if (activities.stream().anyMatch(a -> a.equals(activity))) {
            return;
        }

        // Insert in chronological order
        int insertIndex = 0;
        for (int i = activities.size() - 1; i >= 0; i--) {
            if (activities.get(i).getTimestamp().isBefore(activity.getTimestamp())) {
                insertIndex = i + 1;
                break;
            }
        }
        activities.add(insertIndex, activity);

        // Limit history size
        while (activities.size() > MAX_ACTIVITIES) {
            activities.remove(0);
        }

        touch();
    }

    public void addQualificationCriteria(QualificationCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Criteria cannot be null");
        }
        qualificationCriteria.add(criteria);
        touch();
    }

    public void assignTo(String ownerId, String ownerName) {
        if (ownerId == null || ownerId.trim().isEmpty()) {
            throw new IllegalArgumentException("Owner ID cannot be empty");
        }
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.assignedAt = Instant.now();
        touch();
    }

    public void setCustomField(String key, Object value) {
        customFields.put(key, value);
        touch();
    }

    public void setConversionValue(Double value, String currency) {
        this.conversionValue = value;
        this.conversionCurrency = currency;
        touch();
    }

    public double getQualificationProgress() {
        int total = QualificationCriteria.values().length;
        return (double) qualificationCriteria.size() / total * 100;
    }

    public boolean isFullyQualified() {
        return qualificationCriteria.size() >= MIN_QUALIFICATION_CRITERIA;
    }

    public boolean isHighQuality() {
        return score != null && score.isHighQuality();
    }

    public boolean isStale() {
        return getAgeInDays() > STALE_DAYS;
    }

    public boolean isRecent() {
        return getAgeInDays() <= STALE_DAYS;
    }

    public int getAgeInDays() {
        return (int) Duration.between(createdAt, Instant.now()).toDays();
    }

    public void calculateScore() {
        // Basic scoring logic - will be enhanced by LeadScoringPolicy
        double baseScore = 50.0;

        // Source contribution
        if (source != null) {
            baseScore += source.getQualityScore() * 0.3;
        }

        // Qualification contribution
        baseScore += qualificationCriteria.size() * 5.0;

        // Activity contribution
        long engagementCount = activities.stream()
                .filter(a -> a.getType().isEngagement())
                .count();
        baseScore += Math.min(20.0, engagementCount * 2.0);

        // Cap at 100
        this.score = LeadScore.builder()
                .leadId(leadId.toString())
                .score(Math.min(100, baseScore))
                .build();

        touch();
    }

    public int getEngagementCount() {
        return (int) activities.stream()
                .filter(a -> a.getType().isEngagement())
                .count();
    }

    public int getOutreachCount() {
        return (int) activities.stream()
                .filter(a -> a.getType().isOutreach())
                .count();
    }

    private void touch() {
        this.updatedAt = Instant.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lead lead = (Lead) o;
        return Objects.equals(leadId, lead.leadId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(leadId);
    }

    @Override
    public String toString() {
        return "Lead{" +
                "leadId=" + leadId +
                ", email=" + contactInfo.getEmail() +
                ", status=" + status +
                ", score=" + (score != null ? score.getScore() : "N/A") +
                '}';
    }
}
