package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.LocalizedContent;
import com.gogidix.corporate.website.domain.model.Region;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {
    private String id;
    private String jobKey;
    private String slug;
    private LocalizedContent localizedContent;
    private Language language;
    private String department;
    private String employmentType;
    private String experienceLevel;
    private String location;
    private boolean remote;
    private Set<Region> availableRegions;
    private List<String> responsibilities;
    private List<String> requirements;
    private List<String> benefits;
    private String salaryMin;
    private String salaryMax;
    private String salaryCurrency;
    private String applicationUrl;
    private String applicationEmail;
    private LocalDateTime deadline;
    private SeoMetadataDto seoMetadata;
    private ContentStatus status;
    private LocalDateTime publishDate;
    private LocalDateTime closeDate;
    private LocalDateTime createdAt;
    private List<String> tags;
    private boolean featured;
    private Integer sortOrder;
}
