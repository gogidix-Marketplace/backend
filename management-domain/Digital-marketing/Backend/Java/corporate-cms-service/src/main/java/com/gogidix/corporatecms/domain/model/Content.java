package com.gogidix.corporatecms.domain.model;

import com.gogidix.corporatecms.domain.enums.ContentStatus;
import com.gogidix.corporatecms.domain.enums.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Domain model representing CMS content.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "content")
public class Content {

    @Id
    private String id;

    @Indexed
    private String slug;

    @Indexed
    private ContentType type;

    @Indexed
    private ContentStatus status;

    private String title;

    private String summary;

    private String body;

    private String featuredImageId;

    private List<String> mediaIds;

    private Map<String, Object> metadata;

    private Map<String, String> seoData;

    @Indexed
    private String authorId;

    private String authorName;

    @Indexed
    private String categoryId;

    private String categoryName;

    private List<String> tags;

    @Builder.Default
    private List<String> relatedContentIds = new ArrayList<>();

    private String template;

    private Map<String, Object> templateData;

    private Boolean allowComments;

    private Integer viewCount;

    private Integer readTimeMinutes;

    @Builder.Default
    private List<ContentVersion> versions = new ArrayList<>();

    private Integer currentVersion;

    @Indexed
    @Builder.Default
    private Boolean published = false;

    private LocalDateTime publishedAt;

    private LocalDateTime scheduledPublishAt;

    private LocalDateTime unpublishedAt;

    @Indexed
    private String tenantId;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    @Version
    private Long version;

    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContentVersion {
        private Integer versionNumber;
        private String title;
        private String body;
        private String summary;
        private String comment;
        private String authorId;
        private LocalDateTime createdAt;
    }

    public void addVersion(ContentVersion version) {
        this.versions.add(version);
        this.currentVersion = version.getVersionNumber();
    }

    public ContentVersion getLatestVersion() {
        return versions.isEmpty() ? null : versions.get(versions.size() - 1);
    }
}
