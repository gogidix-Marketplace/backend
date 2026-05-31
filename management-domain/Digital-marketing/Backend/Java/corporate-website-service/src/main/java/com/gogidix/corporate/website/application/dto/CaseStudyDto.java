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
public class CaseStudyDto {
    private String id;
    private String slug;
    private LocalizedContent localizedContent;
    private Language language;
    private String clientName;
    private String clientLogo;
    private String industry;
    private String projectDuration;
    private Set<Region> availableRegions;
    private String challenge;
    private String solution;
    private String results;
    private List<MetricDto> metrics;
    private List<String> technologies;
    private List<String> services;
    private String heroImage;
    private String heroImageAlt;
    private List<String> gallery;
    private String testimonial;
    private String testimonialAuthor;
    private String testimonialRole;
    private String testimonialImage;
    private SeoMetadataDto seoMetadata;
    private ContentStatus status;
    private LocalDateTime publishDate;
    private LocalDateTime createdAt;
    private List<String> tags;
    private boolean featured;
    private Integer sortOrder;
    private Long viewCount;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MetricDto {
        private String label;
        private String value;
        private String unit;
        private String description;
    }
}
