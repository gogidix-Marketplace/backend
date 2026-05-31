package com.gogidix.corporatecms.domain.model;

import com.gogidix.corporatecms.domain.enums.MediaType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Domain model representing media files.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "media")
public class Media {

    @Id
    private String id;

    @Indexed
    private String fileName;

    @Indexed
    private String originalFileName;

    @Indexed
    private MediaType mediaType;

    private String mimeType;

    private String extension;

    private Long fileSize;

    private String filePath;

    private String cdnUrl;

    private String storageProvider;

    @Indexed
    private String folder;

    private String altText;

    private String caption;

    private String description;

    @Indexed
    private String uploadedBy;

    private String uploadedByName;

    @Indexed
    private String tenantId;

    private Integer width;

    private Integer height;

    private Integer duration;

    private Map<String, Object> metadata;

    private String thumbnailPath;

    @Indexed
    @Builder.Default
    private Boolean optimized = false;

    @Builder.Default
    private Map<String, String> transformations = Map.of();

    private String etag;

    @Indexed
    @Builder.Default
    private Boolean deleted = false;

    private LocalDateTime deletedAt;

    @Indexed
    private Integer downloadCount;

    private Integer usageCount;

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

    public boolean isImage() {
        return mediaType == MediaType.IMAGE;
    }

    public boolean isVideo() {
        return mediaType == MediaType.VIDEO;
    }

    public boolean isDocument() {
        return mediaType == MediaType.DOCUMENT;
    }

    public void incrementDownloadCount() {
        this.downloadCount = (this.downloadCount == null) ? 1 : this.downloadCount + 1;
    }

    public void incrementUsageCount() {
        this.usageCount = (this.usageCount == null) ? 1 : this.usageCount + 1;
    }
}
