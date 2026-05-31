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
import java.util.Map;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "pages")
public class Page {
    @Id
    private String id;

    private String pageKey;
    private String path;

    @Builder.Default
    private List<LocalizedContent> localizedContent = new ArrayList<>();

    private String layout;
    private String template;

    @Builder.Default
    private List<String> components = new ArrayList<>();

    @Builder.Default
    private Set<Region> availableRegions = Set.of(Region.NG, Region.KE, Region.GH, Region.ZA, Region.US);

    private Integer sortOrder;
    private boolean showInNavigation;
    private String parentPageId;

    private SeoMetadata seoMetadata;

    private ContentStatus status;
    private LocalDateTime publishDate;
    private LocalDateTime unpublishDate;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    @Builder.Default
    private List<PageVersion> versions = new ArrayList<>();

    @Builder.Default
    private List<String> tags = new ArrayList<>();

    private Map<String, Object> metadata;
}
