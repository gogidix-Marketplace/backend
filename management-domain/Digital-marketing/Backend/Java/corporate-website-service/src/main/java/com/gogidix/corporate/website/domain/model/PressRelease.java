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
@Document(collection = "press_releases")
public class PressRelease {
    @Id
    private String id;

    private String slug;

    @Builder.Default
    private List<LocalizedContent> localizedContent = new ArrayList<>();

    private String releaseDate;

    private String contactName;
    private String contactEmail;
    private String contactPhone;

    @Builder.Default
    private List<String> mediaContacts = new ArrayList<>();

    @Builder.Default
    private List<MediaAsset> mediaAssets = new ArrayList<>();

    @Builder.Default
    private Set<Region> availableRegions = Set.of(Region.NG, Region.KE, Region.GH, Region.ZA, Region.US);

    private boolean embargoed;
    private LocalDateTime embargoUntil;

    private boolean immediateRelease;

    @Builder.Default
    private List<String> tags = new ArrayList<>();

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
    private List<String> attachments = new ArrayList<>();

    private String organization;
    private String tickerSymbol;
}
