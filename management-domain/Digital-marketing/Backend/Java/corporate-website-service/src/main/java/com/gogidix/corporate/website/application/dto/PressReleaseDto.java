package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.LocalizedContent;
import com.gogidix.corporate.website.domain.model.PressRelease;
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
public class PressReleaseDto {
    private String id;
    private String slug;
    private LocalizedContent localizedContent;
    private Language language;
    private String releaseDate;
    private String contactName;
    private String contactEmail;
    private String contactPhone;
    private List<String> mediaContacts;
    private Set<Region> availableRegions;
    private boolean embargoed;
    private boolean immediateRelease;
    private List<String> tags;
    private SeoMetadataDto seoMetadata;
    private ContentStatus status;
    private LocalDateTime publishDate;
    private LocalDateTime createdAt;
    private String organization;
    private String tickerSymbol;
}
