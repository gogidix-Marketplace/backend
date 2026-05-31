package com.gogidix.corporatecms.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.corporatecms.domain.enums.ContentStatus;
import com.gogidix.corporatecms.domain.enums.ContentType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * DTO for Content entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Content DTO for managing CMS content")
public class ContentDTO {

    @Schema(description = "Content ID")
    private String id;

    @Schema(description = "URL-friendly slug")
    @NotBlank(message = "Slug is required")
    @Size(min = 3, max = 200, message = "Slug must be between 3 and 200 characters")
    private String slug;

    @Schema(description = "Content type")
    private ContentType type;

    @Schema(description = "Content status")
    private ContentStatus status;

    @Schema(description = "Content title")
    @NotBlank(message = "Title is required")
    @Size(max = 500, message = "Title must not exceed 500 characters")
    private String title;

    @Schema(description = "Content summary")
    @Size(max = 1000, message = "Summary must not exceed 1000 characters")
    private String summary;

    @Schema(description = "Content body")
    private String body;

    @Schema(description = "Featured image ID")
    private String featuredImageId;

    @Schema(description = "Media IDs")
    private List<String> mediaIds;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "SEO data")
    private Map<String, String> seoData;

    @Schema(description = "Author ID")
    private String authorId;

    @Schema(description = "Author name")
    private String authorName;

    @Schema(description = "Category ID")
    private String categoryId;

    @Schema(description = "Category name")
    private String categoryName;

    @Schema(description = "Content tags")
    private List<String> tags;

    @Schema(description = "Related content IDs")
    private List<String> relatedContentIds;

    @Schema(description = "Template name")
    private String template;

    @Schema(description = "Template data")
    private Map<String, Object> templateData;

    @Schema(description = "Allow comments")
    private Boolean allowComments;

    @Schema(description = "View count")
    private Integer viewCount;

    @Schema(description = "Read time in minutes")
    private Integer readTimeMinutes;

    @Schema(description = "Current version number")
    private Integer currentVersion;

    @Schema(description = "Is published")
    private Boolean published;

    @Schema(description = "Published at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publishedAt;

    @Schema(description = "Scheduled publish at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime scheduledPublishAt;

    @Schema(description = "Unpublished at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime unpublishedAt;

    @Schema(description = "Tenant ID")
    private String tenantId;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;

    @Schema(description = "Created by")
    private String createdBy;

    @Schema(description = "Updated by")
    private String updatedBy;
}
