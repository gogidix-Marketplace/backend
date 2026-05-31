package com.gogidix.digitalmarketing.socialmedia.domain.model;

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
 * SocialContent - Content library for social posts
 *
 * <p>Represents reusable content templates and assets for social media posts.
 * Content can be organized by categories, tags, and campaigns.</p>
 *
 * <p>Tenant Isolation: All queries MUST filter by tenantId</p>
 */
@Document(collection = "social_content")
@TypeAlias("social_content")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "content_tenant_type_idx", def = "{'tenantId': 1, 'contentType': 1, 'status': 1}")
@CompoundIndex(name = "content_tenant_category_idx", def = "{'tenantId': 1, 'category': 1, 'createdAt': -1}")
public class SocialContent extends BaseEntity {

    /**
     * Content name/title
     */
    @Indexed
    private String name;

    /**
     * Content type (TEXT, IMAGE, VIDEO, MIXED, TEMPLATE)
     */
    @Indexed
    private String contentType;

    /**
     * Content category
     */
    @Indexed
    private String category;

    /**
     * Content status (DRAFT, ACTIVE, ARCHIVED, DELETED)
     */
    @Indexed
    @Builder.Default
    private String status = "DRAFT";

    /**
     * Text content/body
     */
    private String content;

    /**
     * Plain text content (without formatting)
     */
    private String plainText;

    /**
     * HTML content
     */
    private String htmlContent;

    /**
     * Media URLs
     */
    private java.util.List<String> mediaUrls;

    /**
     * Media types
     */
    private java.util.List<String> mediaTypes;

    /**
     * Thumbnail URL
     */
    private String thumbnailUrl;

    /**
     * Tags for categorization
     */
    private java.util.List<String> tags;

    /**
     * Content template (if this is a template)
     */
    private String template;

    /**
     * Template variables (for template content)
     */
    private Map<String, Object> templateVariables;

    /**
     * Is template flag
     */
    @Builder.Default
    private Boolean isTemplate = false;

    /**
     * Campaign ID if associated with campaign
     */
    private String campaignId;

    /**
     * Content approval status
     */
    @Builder.Default
    private String approvalStatus = "DRAFT";

    /**
     * Content visibility (PUBLIC, PRIVATE, ORGANIZATION)
     */
    @Builder.Default
    private String visibility = "PRIVATE";

    /**
     * Content language
     */
    @Builder.Default
    private String language = "en";

    /**
     * Content locale
     */
    private String locale;

    /**
     * Character count
     */
    private Integer characterCount;

    /**
     * Word count
     */
    private Integer wordCount;

    /**
     * Estimated read time (seconds)
     */
    private Integer estimatedReadTime;

    /**
     * Platform-specific variations
     */
    private Map<String, Object> platformVariations;

    /**
     * Content version
     */
    @Builder.Default
    private Integer version = 1;

    /**
     * Parent content ID (for versions)
     */
    private String parentContentId;

    /**
     * Additional metadata
     */
    private Map<String, Object> metadata;

    /**
     * Content creator ID
     */
    private String createdById;

    /**
     * Content owner ID
     */
    private String ownerUserId;

    /**
     * Usage count (how many times used in posts)
     */
    @Builder.Default
    private Long usageCount = 0L;

    /**
     * Last used timestamp
     */
    private Instant lastUsedAt;

    /**
     * Performance score (0-100)
     */
    private Integer performanceScore;

    /**
     * Content expiration date
     */
    private Instant expiresAt;

    /**
     * Featured flag
     */
    @Builder.Default
    private Boolean featured = false;

    /**
     * Create a new SocialContent.
     *
     * @param tenantId the tenant ID
     * @param name the content name
     * @param contentType the content type
     * @param category the category
     */
    public SocialContent(String tenantId, String name, String contentType, String category) {
        super(tenantId);
        this.name = name;
        this.contentType = contentType;
        this.category = category;
        this.status = "DRAFT";
        this.isTemplate = false;
        this.visibility = "PRIVATE";
        this.language = "en";
        this.version = 1;
        this.usageCount = 0L;
        this.featured = false;
    }

    /**
     * Activate content.
     */
    public void activate() {
        this.status = "ACTIVE";
        this.touch();
    }

    /**
     * Archive content.
     */
    public void archive() {
        this.status = "ARCHIVED";
        this.touch();
    }

     /**
     * Delete content (soft delete).
     */
    public void softDelete() {
        this.status = "DELETED";
        this.touch();
    }

    /**
     * Check if content is active.
     *
     * @return true if active
     */
    public boolean isActive() {
        return "ACTIVE".equals(this.status);
    }

    /**
     * Check if content is expired.
     *
     * @return true if expired
     */
    public boolean isExpired() {
        return expiresAt != null && Instant.now().isAfter(expiresAt);
    }

    /**
     * Check if content is usable.
     *
     * @return true if can be used
     */
    public boolean isUsable() {
        return isActive() && !isExpired();
    }

    /**
     * Record usage.
     */
    public void recordUsage() {
        this.usageCount++;
        this.lastUsedAt = Instant.now();
        this.touch();
    }

    /**
     * Set performance score.
     *
     * @param score score (0-100)
     */
    public void setPerformanceScore(int score) {
        this.performanceScore = Math.max(0, Math.min(100, score));
        this.touch();
    }

    /**
     * Create a new version.
     *
     * @return new version content
     */
    public SocialContent createNewVersion() {
        SocialContent newVersion = new SocialContent(
            this.tenantId, this.name + " (v" + (this.version + 1) + ")",
            this.contentType, this.category
        );
        newVersion.parentContentId = this.getId();
        newVersion.version = this.version + 1;
        newVersion.content = this.content;
        newVersion.plainText = this.plainText;
        newVersion.htmlContent = this.htmlContent;
        newVersion.mediaUrls = this.mediaUrls;
        newVersion.mediaTypes = this.mediaTypes;
        newVersion.tags = this.tags;
        newVersion.template = this.template;
        newVersion.templateVariables = this.templateVariables;
        newVersion.isTemplate = this.isTemplate;
        newVersion.campaignId = this.campaignId;
        newVersion.language = this.language;
        newVersion.locale = this.locale;
        newVersion.platformVariations = this.platformVariations;
        newVersion.metadata = this.metadata;
        return newVersion;
    }

    /**
     * Calculate text metrics.
     */
    public void calculateMetrics() {
        if (plainText != null && !plainText.isEmpty()) {
            this.characterCount = plainText.length();
            this.wordCount = plainText.split("\\s+").length;
            this.estimatedReadTime = Math.max(1, (int) Math.ceil(this.wordCount / 200.0 * 60));
        }
    }

    /**
     * Add tag to content.
     *
     * @param tag tag to add
     */
    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new java.util.ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
    }

    /**
     * Remove tag from content.
     *
     * @param tag tag to remove
     */
    public void removeTag(String tag) {
        if (this.tags != null) {
            this.tags.remove(tag);
        }
    }

    /**
     * Set platform variation.
     *
     * @param platform platform code
     * @param content platform-specific content
     */
    public void setPlatformVariation(String platform, Object content) {
        if (this.platformVariations == null) {
            this.platformVariations = new HashMap<>();
        }
        this.platformVariations.put(platform, content);
    }

    /**
     * Get platform variation.
     *
     * @param platform platform code
     * @return platform-specific content or null
     */
    public Object getPlatformVariation(String platform) {
        if (this.platformVariations == null) {
            return null;
        }
        return this.platformVariations.get(platform);
    }

    /**
     * Add metadata to content.
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
     * Get a metadata value.
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
     * Approve content.
     *
     * @param approvedBy user approving
     */
    public void approve(String approvedBy) {
        this.approvalStatus = "APPROVED";
        this.touch();
    }

    /**
     * Reject content.
     *
     * @param reason rejection reason
     */
    public void reject(String reason) {
        this.approvalStatus = "REJECTED";
        addMetadata("rejectionReason", reason);
        this.touch();
    }

    /**
     * Check if content is approved.
     *
     * @return true if approved
     */
    public boolean isApproved() {
        return "APPROVED".equals(this.approvalStatus) || "DRAFT".equals(this.approvalStatus);
    }
}
