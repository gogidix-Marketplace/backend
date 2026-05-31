package com.gogidix.digitalmarketing.emailmarketing.domain.model;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * EmailList - Subscriber lists and segments for targeted email campaigns.
 *
 * <p>Email lists contain subscribers who have opted in to receive emails.
 * Lists can be segmented based on various criteria.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "email_lists")
@TypeAlias("email_list")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "list_tenant_status_idx", def = "{'tenantId': 1, 'status': 1, 'name': 1}")
@CompoundIndex(name = "list_tenant_type_idx", def = "{'tenantId': 1, 'listType': 1, 'name': 1}")
public class EmailList extends BaseEntity {

    /**
     * List name (e.g., "Newsletter Subscribers", "Active Customers")
     */
    @Indexed
    private String name;

    /**
     * List description
     */
    private String description;

    /**
     * List status (ACTIVE, INACTIVE, ARCHIVED, CLEANING)
     */
    @Indexed
    private String status;

    /**
     * List type (STATIC, DYNAMIC, SEGMENT, SUPPRESSION)
     */
    @Indexed
    private String listType;

    /**
     * Default from name for emails to this list
     */
    private String defaultFromName;

    /**
     * Default from email for emails to this list
     */
    private String defaultFromEmail;

    /**
     * Default reply-to email
     */
    private String defaultReplyToEmail;

    /**
     * List tags
     */
    private List<String> tags;

    /**
     * Segment criteria (for dynamic lists)
     */
    private SegmentCriteria segmentCriteria;

    /**
     * Custom fields for this list
     */
    private List<CustomField> customFields;

    /**
     * Double opt-in required
     */
    @Builder.Default
    private Boolean doubleOptIn = false;

    /**
     * Confirmation page URL
     */
    private String confirmationUrl;

    /**
     * Thank you page URL
     */
    private String thankYouUrl;

    /**
     * Welcome email template ID
     */
    @Indexed
    private String welcomeTemplateId;

    /**
     * Goodbye email template ID
     */
    @Indexed
    private String goodbyeTemplateId;

    /**
     * Total subscriber count
     */
    private Integer subscriberCount;

    /**
     * Active subscriber count
     */
    private Integer activeCount;

    /**
     * Unsubscribed count
     */
    private Integer unsubscribedCount;

    /**
     * Bounced count
     */
    private Integer bouncedCount;

    /**
     * Last cleaned date
     */
    private Instant lastCleanedAt;

    /**
     * Last statistics update
     */
    private Instant statsUpdatedAt;

    /**
     * Whether to track subscriber activity
     */
    @Builder.Default
    private Boolean trackActivity = true;

    /**
     * List visibility (PUBLIC, PRIVATE, HIDDEN)
     */
    private String visibility;

    /**
     * Owner user ID
     */
    @Indexed
    private String ownerId;

    /**
     * Parent list ID (for derived segments)
     */
    @Indexed
    private String parentListId;

    /**
     * Associated list IDs
     */
    private List<String> relatedListIds;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Whether this is a system list (cannot be deleted)
     */
    @Builder.Default
    private Boolean isSystem = false;

    /**
     * List color (for UI display)
     */
    private String color;

    /**
     * Default locale for this list
     */
    private String defaultLocale;

    /**
     * Timezone for this list
     */
    private String timezone;

    /**
     * Segment criteria inner class
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SegmentCriteria {
        private List<FilterRule> rules;
        private String matchType; // AND or OR

        public List<FilterRule> getRules() { return rules; }
        public void setRules(List<FilterRule> rules) { this.rules = rules; }
        public String getMatchType() { return matchType; }
        public void setMatchType(String matchType) { this.matchType = matchType; }

        @NoArgsConstructor
        @AllArgsConstructor
        public static class FilterRule {
            private String field;
            private String operator; // equals, contains, startsWith, endsWith, greaterThan, lessThan
            private Object value;

            public String getField() { return field; }
            public void setField(String field) { this.field = field; }
            public String getOperator() { return operator; }
            public void setOperator(String operator) { this.operator = operator; }
            public Object getValue() { return value; }
            public void setValue(Object value) { this.value = value; }
        }
    }

    /**
     * Custom field inner class
     */
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CustomField {
        private String name;
        private String type; // text, number, date, boolean, select
        private List<String> options; // for select type
        private Boolean required;
        private Boolean unique;
        private String defaultValue;
        private String description;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        public List<String> getOptions() { return options; }
        public void setOptions(List<String> options) { this.options = options; }
        public Boolean getRequired() { return required; }
        public void setRequired(Boolean required) { this.required = required; }
        public Boolean getUnique() { return unique; }
        public void setUnique(Boolean unique) { this.unique = unique; }
        public String getDefaultValue() { return defaultValue; }
        public void setDefaultValue(String defaultValue) { this.defaultValue = defaultValue; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
    }

    /**
     * Create a new list for a tenant.
     *
     * @param tenantId the tenant ID
     * @param name     the list name
     * @param listType the list type
     */
    public EmailList(String tenantId, String name, String listType) {
        super(tenantId);
        this.name = name;
        this.listType = listType;
        this.status = "ACTIVE";
        this.tags = new ArrayList<>();
        this.customFields = new ArrayList<>();
        this.relatedListIds = new ArrayList<>();
        this.metadata = new HashMap<>();
        this.subscriberCount = 0;
        this.activeCount = 0;
        this.unsubscribedCount = 0;
        this.bouncedCount = 0;
    }

    /**
     * Create a new static list.
     *
     * @param tenantId the tenant ID
     * @param name     the list name
     */
    public EmailList(String tenantId, String name) {
        this(tenantId, name, "STATIC");
    }

    /**
     * Check if list is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Check if list is archived.
     *
     * @return true if archived
     */
    public boolean isArchived() {
        return "ARCHIVED".equals(this.status);
    }

    /**
     * Check if list is a segment.
     *
     * @return true if segment
     */
    public boolean isSegment() {
        return "SEGMENT".equals(this.listType) || "DYNAMIC".equals(this.listType);
    }

    /**
     * Check if list is a suppression list.
     *
     * @return true if suppression list
     */
    public boolean isSuppressionList() {
        return "SUPPRESSION".equals(this.listType);
    }

    /**
     * Check if list is editable.
     *
     * @return true if editable
     */
    public boolean isEditable() {
        return !Boolean.TRUE.equals(this.isSystem) && !"ARCHIVED".equals(this.status);
    }

    /**
     * Check if requires double opt-in.
     *
     * @return true if double opt-in required
     */
    public boolean requiresDoubleOptIn() {
        return Boolean.TRUE.equals(this.doubleOptIn);
    }

    /**
     * Calculate engagement rate.
     *
     * @return engagement percentage
     */
    public double getEngagementRate() {
        if (subscriberCount == null || subscriberCount == 0) {
            return 0.0;
        }
        int active = activeCount != null ? activeCount : 0;
        return ((double) active / subscriberCount) * 100.0;
    }

    /**
     * Calculate bounce rate.
     *
     * @return bounce percentage
     */
    public double getBounceRate() {
        int total = subscriberCount != null ? subscriberCount : 0;
        if (total == 0) {
            return 0.0;
        }
        int bounced = bouncedCount != null ? bouncedCount : 0;
        return ((double) bounced / total) * 100.0;
    }

    /**
     * Calculate unsubscribe rate.
     *
     * @return unsubscribe percentage
     */
    public double getUnsubscribeRate() {
        int total = subscriberCount != null ? subscriberCount : 0;
        if (total == 0) {
            return 0.0;
        }
        int unsubscribed = unsubscribedCount != null ? unsubscribedCount : 0;
        return ((double) unsubscribed / total) * 100.0;
    }

    /**
     * Update subscriber counts.
     *
     * @param total       total count
     * @param active      active count
     * @param unsubscribed unsubscribed count
     * @param bounced     bounced count
     */
    public void updateCounts(Integer total, Integer active, Integer unsubscribed, Integer bounced) {
        this.subscriberCount = total;
        this.activeCount = active;
        this.unsubscribedCount = unsubscribed;
        this.bouncedCount = bounced;
        this.statsUpdatedAt = Instant.now();
        this.touch();
    }

    /**
     * Increment active count.
     */
    public void incrementActive() {
        this.activeCount = (this.activeCount != null ? this.activeCount : 0) + 1;
        this.subscriberCount = (this.subscriberCount != null ? this.subscriberCount : 0) + 1;
        this.touch();
    }

    /**
     * Increment unsubscribed count.
     */
    public void incrementUnsubscribed() {
        this.unsubscribedCount = (this.unsubscribedCount != null ? this.unsubscribedCount : 0) + 1;
        int active = this.activeCount != null ? this.activeCount : 0;
        if (active > 0) {
            this.activeCount = active - 1;
        }
        this.touch();
    }

    /**
     * Increment bounced count.
     */
    public void incrementBounced() {
        this.bouncedCount = (this.bouncedCount != null ? this.bouncedCount : 0) + 1;
        int active = this.activeCount != null ? this.activeCount : 0;
        if (active > 0) {
            this.activeCount = active - 1;
        }
        this.touch();
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
        }
    }

    /**
     * Remove a tag.
     *
     * @param tag the tag to remove
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Add a custom field.
     *
     * @param field the custom field
     */
    public void addCustomField(CustomField field) {
        if (this.customFields == null) {
            this.customFields = new ArrayList<>();
        }
        this.customFields.add(field);
    }

    /**
     * Get a custom field by name.
     *
     * @param name the field name
     * @return the custom field, or null if not found
     */
    public CustomField getCustomField(String name) {
        if (this.customFields == null) {
            return null;
        }
        return this.customFields.stream()
            .filter(f -> name.equals(f.getName()))
            .findFirst()
            .orElse(null);
    }

    /**
     * Add segment rule.
     *
     * @param field    the field name
     * @param operator the operator
     * @param value    the value
     */
    public void addSegmentRule(String field, String operator, Object value) {
        if (this.segmentCriteria == null) {
            this.segmentCriteria = new SegmentCriteria();
            this.segmentCriteria.setRules(new ArrayList<>());
            this.segmentCriteria.setMatchType("AND");
        }
        SegmentCriteria.FilterRule rule = new SegmentCriteria.FilterRule();
        rule.setField(field);
        rule.setOperator(operator);
        rule.setValue(value);
        this.segmentCriteria.getRules().add(rule);
    }

    /**
     * Clear segment rules.
     */
    public void clearSegmentRules() {
        if (this.segmentCriteria != null) {
            this.segmentCriteria.getRules().clear();
        }
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
    }

    /**
     * Add related list ID.
     *
     * @param listId the related list ID
     */
    public void addRelatedList(String listId) {
        if (this.relatedListIds == null) {
            this.relatedListIds = new ArrayList<>();
        }
        if (!this.relatedListIds.contains(listId)) {
            this.relatedListIds.add(listId);
        }
    }

    /**
     * Archive the list.
     */
    public void archive() {
        this.status = "ARCHIVED";
        this.touch();
    }

    /**
     * Activate the list.
     */
    public void activate() {
        this.status = "ACTIVE";
        this.touch();
    }

    /**
     * Deactivate the list.
     */
    public void deactivate() {
        this.status = "INACTIVE";
        this.touch();
    }

    /**
     * Mark list as being cleaned.
     */
    public void markAsCleaning() {
        this.status = "CLEANING";
        this.touch();
    }

    /**
     * Mark list as cleaned.
     */
    public void markAsCleaned() {
        this.lastCleanedAt = Instant.now();
        this.status = "ACTIVE";
        this.touch();
    }

    /**
     * Check if list needs cleaning.
     *
     * @param daysThreshold days since last clean
     * @return true if needs cleaning
     */
    public boolean needsCleaning(int daysThreshold) {
        if (lastCleanedAt == null) {
            return true;
        }
        Instant threshold = Instant.now().minusSeconds(daysThreshold * 86400L);
        return lastCleanedAt.isBefore(threshold);
    }

    /**
     * Get list health score (0-100).
     *
     * @return health score
     */
    public int getHealthScore() {
        double bounceRate = getBounceRate();
        double unsubscribeRate = getUnsubscribeRate();

        // Start with 100
        int score = 100;

        // Deduct for bounces (max 40 points)
        score -= Math.min(40, (int) (bounceRate * 2));

        // Deduct for unsubscribes (max 30 points)
        score -= Math.min(30, (int) (unsubscribeRate * 3));

        // Bonus for high engagement (max 30 points)
        double engagementRate = getEngagementRate();
        if (engagementRate > 50) {
            score += 30;
        } else if (engagementRate > 30) {
            score += 20;
        } else if (engagementRate > 10) {
            score += 10;
        }

        return Math.max(0, Math.min(100, score));
    }
}
