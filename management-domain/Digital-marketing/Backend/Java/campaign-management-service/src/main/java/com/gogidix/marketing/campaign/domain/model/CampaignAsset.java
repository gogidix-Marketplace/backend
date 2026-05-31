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

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * CampaignAsset - Creative assets for campaigns
 *
 * <p>Represents a creative asset (image, video, document, etc.)
 * used in marketing campaigns.</p>
 */
@Document(collection = "campaign_assets")
@TypeAlias("campaign_asset")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@CompoundIndex(name = "asset_tenant_campaign_idx", def = "{'tenantId': 1, 'campaignId': 1, 'assetType': 1}")
@CompoundIndex(name = "asset_tenant_channel_idx", def = "{'tenantId': 1, 'channelId': 1}")
public class CampaignAsset extends BaseEntity {

    /**
     * Campaign ID this asset belongs to
     */
    @Indexed
    private String campaignId;

    /**
     * Channel ID this asset is specific to (optional)
     */
    @Indexed
    private String channelId;

    /**
     * Asset name
     */
    @Indexed
    private String assetName;

    /**
     * Asset type (IMAGE, VIDEO, DOCUMENT, HTML, TEXT, AUDIO)
     */
    @Indexed
    private String assetType;

    /**
     * MIME type
     */
    private String mimeType;

    /**
     * File size in bytes
     */
    private Long fileSize;

    /**
     * Asset URL or path
     */
    private String url;

    /**
     * Thumbnail URL (for images/videos)
     */
    private String thumbnailUrl;

    /**
     * Asset description
     */
    private String description;

    /**
     * Asset tags
     */
    private Map<String, String> tags;

    /**
     * Asset status (DRAFT, READY, PUBLISHED, ARCHIVED)
     */
    @Builder.Default
    private String status = "DRAFT";

    /**
     * Asset format (e.g., jpg, mp4, pdf)
     */
    private String format;

    /**
     * Asset dimensions (for images/videos)
     */
    private String dimensions;

    /**
     * Duration in seconds (for videos/audio)
     */
    private Long duration;

    /**
     * Alt text for accessibility
     */
    private String altText;

    /**
     * Asset variant (e.g., original, thumbnail, optimized)
     */
    private String variant;

    /**
     * Parent asset ID (for variants)
     */
    @Indexed
    private String parentAssetId;

    /**
     * Storage location reference
     */
    private String storageLocation;

    /**
     * Storage provider (S3, Azure, GCS, local)
     */
    private String storageProvider;

    /**
     * External storage key
     */
    private String storageKey;

    /**
     * Asset metadata (width, height, etc.)
     */
    private Map<String, Object> metadata;

    /**
     * Asset usage count
     */
    @Builder.Default
    private Integer usageCount = 0;

    /**
     * Last used date
     */
    private Instant lastUsedAt;

    /**
     * Asset expiration date
     */
    private Instant expiresAt;

    /**
     * Whether asset is approved for use
     */
    @Builder.Default
    private Boolean isApproved = false;

    /**
     * Approved by
     */
    private String approvedBy;

    /**
     * Approved at
     */
    private Instant approvedAt;

    /**
     * Asset version
     */
    @Builder.Default
    private String version = "1.0";

    /**
     * Additional attributes
     */
    private Map<String, Object> attributes;

    /**
     * Create a new CampaignAsset.
     *
     * @param tenantId  the tenant ID
     * @param campaignId the campaign ID
     * @param assetName the asset name
     * @param assetType the asset type
     */
    public CampaignAsset(String tenantId, String campaignId, String assetName, String assetType) {
        super(tenantId);
        this.campaignId = campaignId;
        this.assetName = assetName;
        this.assetType = assetType;
        this.status = "DRAFT";
        this.usageCount = 0;
        this.version = "1.0";
        this.isApproved = false;
        this.tags = new HashMap<>();
        this.metadata = new HashMap<>();
        this.attributes = new HashMap<>();
    }

    /**
     * Check if asset is an image.
     *
     * @return true if asset type is IMAGE
     */
    public boolean isImage() {
        return "IMAGE".equals(this.assetType);
    }

    /**
     * Check if asset is a video.
     *
     * @return true if asset type is VIDEO
     */
    public boolean isVideo() {
        return "VIDEO".equals(this.assetType);
    }

    /**
     * Check if asset is a document.
     *
     * @return true if asset type is DOCUMENT
     */
    public boolean isDocument() {
        return "DOCUMENT".equals(this.assetType);
    }

    /**
     * Check if asset is ready for use.
     *
     * @return true if asset status is READY or PUBLISHED
     */
    public boolean isReady() {
        return "READY".equals(this.status) || "PUBLISHED".equals(this.status);
    }

    /**
     * Check if asset is published.
     *
     * @return true if asset status is PUBLISHED
     */
    public boolean isPublished() {
        return "PUBLISHED".equals(this.status);
    }

    /**
     * Check if asset is archived.
     *
     * @return true if asset status is ARCHIVED
     */
    public boolean isArchived() {
        return "ARCHIVED".equals(this.status);
    }

    /**
     * Check if asset is approved.
     *
     * @return true if asset is approved
     */
    public boolean isApproved() {
        return Boolean.TRUE.equals(this.isApproved);
    }

    /**
     * Mark asset as ready.
     */
    public void markAsReady() {
        this.status = "READY";
        this.touch();
    }

    /**
     * Publish the asset.
     */
    public void publish() {
        this.status = "PUBLISHED";
        this.touch();
    }

    /**
     * Archive the asset.
     */
    public void archive() {
        this.status = "ARCHIVED";
        this.touch();
    }

    /**
     * Approve the asset.
     *
     * @param approvedBy the user approving the asset
     */
    public void approve(String approvedBy) {
        this.isApproved = true;
        this.approvedBy = approvedBy;
        this.approvedAt = Instant.now();
        this.touch();
    }

    /**
     * Reject approval for the asset.
     */
    public void rejectApproval() {
        this.isApproved = false;
        this.approvedBy = null;
        this.approvedAt = null;
        this.touch();
    }

    /**
     * Record usage of the asset.
     */
    public void recordUsage() {
        this.usageCount = (this.usageCount == null ? 0 : this.usageCount) + 1;
        this.lastUsedAt = Instant.now();
        this.touch();
    }

    /**
     * Set storage information.
     *
     * @param provider the storage provider
     * @param location the storage location
     * @param key      the storage key
     */
    public void setStorageInfo(String provider, String location, String key) {
        this.storageProvider = provider;
        this.storageLocation = location;
        this.storageKey = key;
        this.touch();
    }

    /**
     * Get full URL for the asset.
     *
     * @return the URL or null if not set
     */
    public String getFullUrl() {
        return this.url;
    }

    /**
     * Get thumbnail URL or fall back to main URL.
     *
     * @return thumbnail URL or main URL
     */
    public String getThumbnailOrFullUrl() {
        return this.thumbnailUrl != null ? this.thumbnailUrl : this.url;
    }

    /**
     * Add a tag to the asset.
     *
     * @param key   the tag key
     * @param value the tag value
     */
    public void addTag(String key, String value) {
        if (this.tags == null) {
            this.tags = new HashMap<>();
        }
        this.tags.put(key, value);
        this.touch();
    }

    /**
     * Get a tag value.
     *
     * @param key the tag key
     * @return the tag value or null if not set
     */
    public String getTag(String key) {
        if (this.tags == null) {
            return null;
        }
        return this.tags.get(key);
    }

    /**
     * Add metadata to the asset.
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
     * Add an attribute to the asset.
     *
     * @param key   the attribute key
     * @param value the attribute value
     */
    public void addAttribute(String key, Object value) {
        if (this.attributes == null) {
            this.attributes = new HashMap<>();
        }
        this.attributes.put(key, value);
        this.touch();
    }

    /**
     * Get an attribute value.
     *
     * @param key the attribute key
     * @return the attribute value or null if not set
     */
    public Object getAttribute(String key) {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.get(key);
    }

    /**
     * Check if asset is expired.
     *
     * @return true if expiration date has passed
     */
    public boolean isExpired() {
        return this.expiresAt != null && Instant.now().isAfter(this.expiresAt);
    }

    /**
     * Check if asset is a variant.
     *
     * @return true if this asset has a parent asset ID
     */
    public boolean isVariant() {
        return this.parentAssetId != null;
    }

    /**
     * Create a new version of this asset.
     *
     * @return a new CampaignAsset with incremented version
     */
    public CampaignAsset createNewVersion() {
        CampaignAsset newAsset = new CampaignAsset(
            this.getTenantId(),
            this.campaignId,
            this.assetName + " (v" + (Integer.parseInt(this.version.replace(".", "")) + 1) + ")",
            this.assetType
        );
        newAsset.setChannelId(this.channelId);
        newAsset.setParentAssetId(this.getId());
        newAsset.setDescription(this.description);
        newAsset.setMimeType(this.mimeType);
        newAsset.setTags(new HashMap<>(this.tags));
        newAsset.setMetadata(new HashMap<>(this.metadata));
        return newAsset;
    }
}
