package com.gogidix.corporate.website.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "jobs")
public class Job {
    @Id
    private String id;

    private String jobKey;
    private String slug;

    @Builder.Default
    private List<LocalizedContent> localizedContent = new ArrayList<>();

    private String department;
    private String employmentType;
    private String experienceLevel;
    private String location;
    private boolean remote;
    @Builder.Default
    private Set<Region> availableRegions = Set.of(Region.NG, Region.KE, Region.GH, Region.ZA, Region.US);

    private String description;
    @Builder.Default
    private List<String> responsibilities = new ArrayList<>();

    @Builder.Default
    private List<String> requirements = new ArrayList<>();

    @Builder.Default
    private List<String> benefits = new ArrayList<>();

    private String salaryMin;
    private String salaryMax;
    private String salaryCurrency;

    private String applicationUrl;
    private String applicationEmail;

    private LocalDateTime deadline;

    private SeoMetadata seoMetadata;

    private ContentStatus status;
    private LocalDateTime publishDate;
    private LocalDateTime closeDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    private boolean featured;
    private Integer sortOrder;

    private Long applicationCount;
}
