package com.gogidix.digitalmarketing.brandmanagement.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "brand_assets")
public class BrandAsset {

    @Id
    private String id;
    private String tenantId;
    private String name;
    private String type;
    private String url;
    private String category;
    private String description;
    private String status;
    private String version;
    private String fileFormat;
    private Long fileSize;
    private String storageLocation;
    private String approvedBy;
    private Instant approvedAt;
    private String uploadedBy;
    private Boolean isActive;
    private Boolean isPublic;
    private String createdBy;
    private Instant createdAt;
    private Instant updatedAt;
    private List<String> tags;
    private List<BrandAssetUsage> usages;
    private Map<String, Object> metadata;

    public BrandAsset(String tenantId, String name, String type, String url) {
        this.id = UUID.randomUUID().toString();
        this.tenantId = tenantId;
        this.name = name;
        this.type = type;
        this.url = url;
        this.status = "DRAFT";
        this.isActive = true;
        this.isPublic = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        this.tags = new ArrayList<>();
        this.usages = new ArrayList<>();
        this.metadata = new HashMap<>();
    }

    public void approve(String userId) {
        this.status = "APPROVED";
        this.approvedBy = userId;
        this.approvedAt = Instant.now();
        this.updatedAt = Instant.now();
    }

    public boolean isApproved() {
        return "APPROVED".equals(status);
    }

    public void archive() {
        this.status = "ARCHIVED";
        this.isActive = false;
        this.updatedAt = Instant.now();
    }

    public void addTag(String tag) {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        if (!this.tags.contains(tag)) {
            this.tags.add(tag);
        }
        this.updatedAt = Instant.now();
    }

    public void addUsage(String context, String reference) {
        if (this.usages == null) {
            this.usages = new ArrayList<>();
        }
        this.usages.add(new BrandAssetUsage(context, reference, Instant.now()));
        this.updatedAt = Instant.now();
    }

    public List<String> getTags() {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }
        return this.tags;
    }

    public List<BrandAssetUsage> getUsages() {
        if (this.usages == null) {
            this.usages = new ArrayList<>();
        }
        return this.usages;
    }

    public Map<String, Object> getMetadata() {
        if (this.metadata == null) {
            this.metadata = new HashMap<>();
        }
        return this.metadata;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BrandAssetUsage {
        private String context;
        private String reference;
        private Instant usedAt;
    }
}
