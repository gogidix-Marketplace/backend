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
@Document(collection = "case_studies")
public class CaseStudy {
    @Id
    private String id;

    private String slug;

    @Builder.Default
    private List<LocalizedContent> localizedContent = new ArrayList<>();

    private String clientName;
    private String clientLogo;
    private String industry;
    private String projectDuration;

    @Builder.Default
    private Set<Region> availableRegions = Set.of(Region.NG, Region.KE, Region.GH, Region.ZA, Region.US);

    private String challenge;
    private String solution;
    private String results;

    @Builder.Default
    private List<Metric> metrics = new ArrayList<>();

    @Builder.Default
    private List<String> technologies = new ArrayList<>();

    @Builder.Default
    private List<String> services = new ArrayList<>();

    private String heroImage;
    private String heroImageAlt;
    @Builder.Default
    private List<String> gallery = new ArrayList<>();

    private String testimonial;
    private String testimonialAuthor;
    private String testimonialRole;
    private String testimonialImage;

    @Builder.Default
    private List<String> relatedCaseStudies = new ArrayList<>();

    @Builder.Default
    private List<String> relatedProducts = new ArrayList<>();

    private SeoMetadata seoMetadata;

    private ContentStatus status;
    private LocalDateTime publishDate;

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

    private Long viewCount;
}
