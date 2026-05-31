package com.gogidix.corporatecms.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.corporatecms.domain.enums.MediaType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * DTO for Media entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Media DTO for managing media files")
public class MediaDTO {

    @Schema(description = "Media ID")
    private String id;

    @Schema(description = "File name")
    private String fileName;

    @Schema(description = "Original file name")
    private String originalFileName;

    @Schema(description = "Media type")
    private MediaType mediaType;

    @Schema(description = "MIME type")
    private String mimeType;

    @Schema(description = "File extension")
    private String extension;

    @Schema(description = "File size in bytes")
    private Long fileSize;

    @Schema(description = "File path")
    private String filePath;

    @Schema(description = "CDN URL")
    private String cdnUrl;

    @Schema(description = "Storage provider")
    private String storageProvider;

    @Schema(description = "Folder path")
    private String folder;

    @Schema(description = "Alt text for accessibility")
    private String altText;

    @Schema(description = "Image caption")
    private String caption;

    @Schema(description = "Media description")
    private String description;

    @Schema(description = "Uploaded by user ID")
    private String uploadedBy;

    @Schema(description = "Uploaded by user name")
    private String uploadedByName;

    @Schema(description = "Image width")
    private Integer width;

    @Schema(description = "Image height")
    private Integer height;

    @Schema(description = "Video duration in seconds")
    private Integer duration;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Thumbnail path")
    private String thumbnailPath;

    @Schema(description = "Is optimized")
    private Boolean optimized;

    @Schema(description = "Transformations applied")
    private Map<String, String> transformations;

    @Schema(description = "Download count")
    private Integer downloadCount;

    @Schema(description = "Usage count in content")
    private Integer usageCount;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
