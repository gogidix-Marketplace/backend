package com.gogidix.corporatecms.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.corporatecms.domain.enums.JobStatus;
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
 * DTO for Job entity.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Job DTO for managing career opportunities")
public class JobDTO {

    @Schema(description = "Job ID")
    private String id;

    @Schema(description = "URL-friendly slug")
    @NotBlank(message = "Slug is required")
    private String slug;

    @Schema(description = "Job status")
    private JobStatus status;

    @Schema(description = "Job title")
    @NotBlank(message = "Job title is required")
    @Size(max = 500, message = "Job title must not exceed 500 characters")
    private String title;

    @Schema(description = "Job summary")
    @Size(max = 1000, message = "Summary must not exceed 1000 characters")
    private String summary;

    @Schema(description = "Job description")
    private String description;

    @Schema(description = "Job responsibilities")
    private List<String> responsibilities;

    @Schema(description = "Job requirements")
    private List<String> requirements;

    @Schema(description = "Job benefits")
    private List<String> benefits;

    @Schema(description = "Required skills")
    private List<String> skills;

    @Schema(description = "Department ID")
    private String departmentId;

    @Schema(description = "Department name")
    private String departmentName;

    @Schema(description = "Location")
    private String location;

    @Schema(description = "Location type")
    private String locationType;

    @Schema(description = "Employment type")
    private String employmentType;

    @Schema(description = "Experience level")
    private String experienceLevel;

    @Schema(description = "Minimum salary")
    private String salaryMin;

    @Schema(description = "Maximum salary")
    private String salaryMax;

    @Schema(description = "Salary currency")
    private String salaryCurrency;

    @Schema(description = "Salary display string")
    private String salaryDisplay;

    @Schema(description = "Featured image ID")
    private String featuredImageId;

    @Schema(description = "Gallery image IDs")
    private List<String> galleryImageIds;

    @Schema(description = "External application URL")
    private String externalApplyUrl;

    @Schema(description = "Sort order")
    private Integer sortOrder;

    @Schema(description = "Is featured")
    private Boolean featured;

    @Schema(description = "Application deadline")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime applicationDeadline;

    @Schema(description = "Published at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime publishedAt;

    @Schema(description = "View count")
    private Integer viewCount;

    @Schema(description = "Application count")
    private Integer applicationCount;

    @Schema(description = "Additional metadata")
    private Map<String, Object> metadata;

    @Schema(description = "Created at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @Schema(description = "Updated at")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;
}
