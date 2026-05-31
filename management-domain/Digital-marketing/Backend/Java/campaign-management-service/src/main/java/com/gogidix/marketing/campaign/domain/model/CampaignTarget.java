package com.gogidix.marketing.campaign.domain.model;

import com.gogidix.marketing.campaign.shared.domain.BaseEntity;
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

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CampaignTarget - Target audience definitions for campaigns
 *
 * <p>Represents a target audience with demographic, geographic,
 * and behavioral criteria for campaign targeting.</p>
 */
@Document(collection = "campaign_targets")
@TypeAlias("campaign_target")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "target_tenant_idx", def = "{'tenantId': 1, 'name': 1}")
@CompoundIndex(name = "target_tenant_type_idx", def = "{'tenantId': 1, 'targetType': 1}")
public class CampaignTarget extends BaseEntity {

    /**
     * Target audience name
     */
    @Indexed
    private String name;

    /**
     * Target description
     */
    private String description;

    /**
     * Target type (DEMOGRAPHIC, GEOGRAPHIC, BEHAVIORAL, CUSTOM, LOOKALIKE)
     */
    @Indexed
    private String targetType;

    /**
     * Estimated audience size
     */
    private Long estimatedSize;

    /**
     * Actual audience size (calculated)
     */
    private Long actualSize;

    /**
     * Minimum age
     */
    private Integer minAge;

    /**
     * Maximum age
     */
    private Integer maxAge;

    /**
     * Gender filter (MALE, FEMALE, ALL, OTHER)
     */
    private String gender;

    /**
     * Target locations (countries, regions, cities)
     */
    private List<String> locations;

    /**
     * Location type (COUNTRY, REGION, CITY, POSTAL_CODE)
     */
    private String locationType;

    /**
     * Languages
     */
    private List<String> languages;

    /**
     * Interests (hobbies, topics, categories)
     */
    private List<String> interests;

    /**
     * Industries
     */
    private List<String> industries;

    /**
     * Job titles
     */
    private List<String> jobTitles;

    /**
     * Job levels
     */
    private List<String> jobLevels;

    /**
     * Income ranges
     */
    private List<String> incomeRanges;

    /**
     * Education levels
     */
    private List<String> educationLevels;

    /**
     * marital status
     */
    private List<String> maritalStatus;

    /**
     * Parental status
     */
    private List<String> parentalStatus;

    /**
     * Device types (MOBILE, DESKTOP, TABLET)
     */
    private List<String> deviceTypes;

    /**
     * Operating systems
     */
    private List<String> operatingSystems;

    /**
     * Browser types
     */
    private List<String> browserTypes;

    /**
     * Custom attributes
     */
    private Map<String, List<String>> customAttributes;

    /**
     * Behavioral segments
     */
    private List<String> behavioralSegments;

    /**
     * Purchase behaviors
     */
    private List<String> purchaseBehaviors;

    /**
     * Engagement level (LOW, MEDIUM, HIGH)
     */
    private String engagementLevel;

    /**
     * Customer lifecycle stage (AWARENESS, CONSIDERATION, CONVERSION, RETENTION, ADVOCACY)
     */
    private List<String> lifecycleStages;

    /**
     * Previous purchasers only
     */
    @Builder.Default
    private Boolean previousPurchasersOnly = false;

    /**
     * Lookalike source audience ID
     */
    @Indexed
    private String lookalikeSourceId;

    /**
     * Lookalike similarity percentage (1-100)
     */
    private Integer lookalikeSimilarity;

    /**
     * Exclusion criteria (audience to exclude)
     */
    private List<String> exclusionIds;

    /**
     * Target is active and available for use
     */
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Target is verified
     */
    @Builder.Default
    private Boolean isVerified = false;

    /**
     * Last calculated timestamp
     */
    private Instant lastCalculatedAt;

    /**
     * Calculation status (PENDING, CALCULATING, COMPLETED, FAILED)
     */
    private String calculationStatus;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Tags for categorization
     */
    private List<String> tags;

    /**
     * Priority for targeting
     */
    @Builder.Default
    private Integer priority = 0;

    /**
     * Expected reach percentage
     */
    private BigDecimal expectedReachPercentage;

    /**
     * Cost factor (multiplier for targeting cost)
     */
    private BigDecimal costFactor;

    /**
     * Create a new CampaignTarget.
     *
     * @param tenantId  the tenant ID
     * @param name      the target name
     * @param targetType the target type
     */
    public CampaignTarget(String tenantId, String name, String targetType) {
        super(tenantId);
        this.name = name;
        this.targetType = targetType;
        this.isActive = true;
        this.isVerified = false;
        this.previousPurchasersOnly = false;
        this.priority = 0;
        this.locations = new ArrayList<>();
        this.languages = new ArrayList<>();
        this.interests = new ArrayList<>();
        this.industries = new ArrayList<>();
        this.jobTitles = new ArrayList<>();
        this.jobLevels = new ArrayList<>();
        this.incomeRanges = new ArrayList<>();
        this.educationLevels = new ArrayList<>();
        this.maritalStatus = new ArrayList<>();
        this.parentalStatus = new ArrayList<>();
        this.deviceTypes = new ArrayList<>();
        this.operatingSystems = new ArrayList<>();
        this.browserTypes = new ArrayList<>();
        this.customAttributes = new HashMap<>();
        this.behavioralSegments = new ArrayList<>();
        this.purchaseBehaviors = new ArrayList<>();
        this.lifecycleStages = new ArrayList<>();
        this.exclusionIds = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    /**
     * Check if target is active.
     *
     * @return true if target is active
     */
    public boolean isActive() {
        return Boolean.TRUE.equals(this.isActive);
    }

    /**
     * Check if target is verified.
     *
     * @return true if target is verified
     */
    public boolean isVerified() {
        return Boolean.TRUE.equals(this.isVerified);
    }

    /**
     * Activate the target.
     */
    public void activate() {
        this.isActive = true;
        this.touch();
    }

    /**
     * Deactivate the target.
     */
    public void deactivate() {
        this.isActive = false;
        this.touch();
    }

    /**
     * Mark as verified.
     */
    public void markAsVerified() {
        this.isVerified = true;
        this.lastCalculatedAt = Instant.now();
        this.calculationStatus = "COMPLETED";
        this.touch();
    }

    /**
     * Add a location.
     *
     * @param location the location to add
     */
    public void addLocation(String location) {
        if (this.locations == null) {
            this.locations = new ArrayList<>();
        }
        if (!this.locations.contains(location)) {
            this.locations.add(location);
            this.touch();
        }
    }

    /**
     * Add an interest.
     *
     * @param interest the interest to add
     */
    public void addInterest(String interest) {
        if (this.interests == null) {
            this.interests = new ArrayList<>();
        }
        if (!this.interests.contains(interest)) {
            this.interests.add(interest);
            this.touch();
        }
    }

    /**
     * Add a behavioral segment.
     *
     * @param segment the segment to add
     */
    public void addBehavioralSegment(String segment) {
        if (this.behavioralSegments == null) {
            this.behavioralSegments = new ArrayList<>();
        }
        if (!this.behavioralSegments.contains(segment)) {
            this.behavioralSegments.add(segment);
            this.touch();
        }
    }

    /**
     * Add a custom attribute.
     *
     * @param name       the attribute name
     * @param values     the attribute values
     */
    public void addCustomAttribute(String name, List<String> values) {
        if (this.customAttributes == null) {
            this.customAttributes = new HashMap<>();
        }
        this.customAttributes.put(name, values);
        this.touch();
    }

    /**
     * Get custom attribute values.
     *
     * @param name the attribute name
     * @return the attribute values or null
     */
    public List<String> getCustomAttribute(String name) {
        if (this.customAttributes == null) {
            return null;
        }
        return this.customAttributes.get(name);
    }

    /**
     * Add metadata.
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
     * Get metadata.
     *
     * @param key the metadata key
     * @return the metadata value or null
     */
    public Object getMetadata(String key) {
        if (this.metadata == null) {
            return null;
        }
        return this.metadata.get(key);
    }

    /**
     * Add a tag.
     *
     * @param tag the tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
            this.touch();
        }
    }

    /**
     * Add an exclusion.
     *
     * @param exclusionId the exclusion ID to add
     */
    public void addExclusion(String exclusionId) {
        if (this.exclusionIds == null) {
            this.exclusionIds = new ArrayList<>();
        }
        if (!this.exclusionIds.contains(exclusionId)) {
            this.exclusionIds.add(exclusionId);
            this.touch();
        }
    }

    /**
     * Check if has demographic criteria.
     *
     * @return true if any demographic criteria is set
     */
    public boolean hasDemographicCriteria() {
        return (this.minAge != null || this.maxAge != null || this.gender != null ||
                this.incomeRanges != null && !this.incomeRanges.isEmpty() ||
                this.educationLevels != null && !this.educationLevels.isEmpty());
    }

    /**
     * Check if has geographic criteria.
     *
     * @return true if any geographic criteria is set
     */
    public boolean hasGeographicCriteria() {
        return this.locations != null && !this.locations.isEmpty();
    }

    /**
     * Check if has behavioral criteria.
     *
     * @return true if any behavioral criteria is set
     */
    public boolean hasBehavioralCriteria() {
        return (this.behavioralSegments != null && !this.behavioralSegments.isEmpty() ||
                this.purchaseBehaviors != null && !this.purchaseBehaviors.isEmpty() ||
                this.lifecycleStages != null && !this.lifecycleStages.isEmpty());
    }

    /**
     * Check if has device criteria.
     *
     * @return true if any device criteria is set
     */
    public boolean hasDeviceCriteria() {
        return (this.deviceTypes != null && !this.deviceTypes.isEmpty() ||
                this.operatingSystems != null && !this.operatingSystems.isEmpty() ||
                this.browserTypes != null && !this.browserTypes.isEmpty());
    }

    /**
     * Check if is lookalike audience.
     *
     * @return true if this is a lookalike audience
     */
    public boolean isLookalike() {
        return "LOOKALIKE".equals(this.targetType) && this.lookalikeSourceId != null;
    }

    /**
     * Calculate expected reach.
     *
     * @return expected reach based on percentage and estimated size
     */
    public Long calculateExpectedReach() {
        if (this.estimatedSize == null || this.estimatedSize == 0) {
            return 0L;
        }
        if (this.expectedReachPercentage == null) {
            return this.estimatedSize;
        }
        return BigDecimal.valueOf(this.estimatedSize).multiply(this.expectedReachPercentage)
            .divide(new BigDecimal("100"), 0, java.math.RoundingMode.HALF_UP).longValue();
    }

    /**
     * Get complexity score based on number of criteria.
     *
     * @return complexity score (higher = more complex)
     */
    public int getComplexityScore() {
        int score = 0;
        if (this.minAge != null || this.maxAge != null) score++;
        if (this.gender != null) score++;
        if (this.locations != null) score += this.locations.size();
        if (this.languages != null) score += this.languages.size();
        if (this.interests != null) score += this.interests.size();
        if (this.behavioralSegments != null) score += this.behavioralSegments.size() * 2;
        if (this.customAttributes != null) score += this.customAttributes.size() * 3;
        return score;
    }

    /**
     * Mark calculation as started.
     */
    public void markCalculationStarted() {
        this.calculationStatus = "CALCULATING";
        this.touch();
    }

    /**
     * Mark calculation as completed.
     *
     * @param actualSize the calculated actual size
     */
    public void markCalculationCompleted(Long actualSize) {
        this.calculationStatus = "COMPLETED";
        this.actualSize = actualSize;
        this.lastCalculatedAt = Instant.now();
        this.touch();
    }

    /**
     * Mark calculation as failed.
     */
    public void markCalculationFailed() {
        this.calculationStatus = "FAILED";
        this.lastCalculatedAt = Instant.now();
        this.touch();
    }
}
