package com.gogidix.corporatecms.domain.model;

import com.gogidix.corporatecms.domain.enums.JobStatus;
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
 * Domain model representing job postings.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class Job {

    @Id
    private String id;

    @Indexed(unique = true)
    private String slug;

    @Indexed
    private JobStatus status;

    private String title;

    private String summary;

    private String description;

    private List<String> responsibilities;

    private List<String> requirements;

    private List<String> benefits;

    private List<String> skills;

    @Indexed
    private String departmentId;

    private String departmentName;

    private String location;

    private String locationType;

    @Indexed
    private String employmentType;

    private String experienceLevel;

    private String salaryMin;

    private String salaryMax;

    private String salaryCurrency;

    private String salaryDisplay;

    private String featuredImageId;

    private List<String> galleryImageIds;

    private String externalApplyUrl;

    @Indexed
    private String tenantId;

    private Integer sortOrder;

    @Indexed
    private Boolean featured;

    @Builder.Default
    private Boolean published = false;

    private LocalDateTime applicationDeadline;

    @Indexed
    private LocalDateTime publishedAt;

    private LocalDateTime closedAt;

    private Integer viewCount;

    private Integer applicationCount;

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

    private Map<String, Object> metadata;

    public void incrementViewCount() {
        this.viewCount = (this.viewCount == null) ? 1 : this.viewCount + 1;
    }

    public void incrementApplicationCount() {
        this.applicationCount = (this.applicationCount == null) ? 1 : this.applicationCount + 1;
    }

    public boolean isOpen() {
        return status == JobStatus.OPEN && (applicationDeadline == null || applicationDeadline.isAfter(LocalDateTime.now()));
    }
}
