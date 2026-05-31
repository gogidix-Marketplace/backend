package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import com.gogidix.corporate.website.domain.model.LocalizedContent;
import com.gogidix.corporate.website.domain.model.Page;
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
public class PageDto {
    private String id;
    private String pageKey;
    private String path;
    private LocalizedContent localizedContent;
    private Language language;
    private String layout;
    private String template;
    private List<String> components;
    private Set<Region> availableRegions;
    private Integer sortOrder;
    private boolean showInNavigation;
    private String parentPageId;
    private SeoMetadataDto seoMetadata;
    private ContentStatus status;
    private LocalDateTime publishDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> tags;

    public static PageDto fromEntity(Page page, Language language) {
        LocalizedContent content = page.getLocalizedContent().stream()
                .filter(lc -> lc.getLanguage() == language)
                .findFirst()
                .orElse(page.getLocalizedContent().isEmpty() ? null : page.getLocalizedContent().get(0));

        return PageDto.builder()
                .id(page.getId())
                .pageKey(page.getPageKey())
                .path(page.getPath())
                .localizedContent(content)
                .language(language)
                .layout(page.getLayout())
                .template(page.getTemplate())
                .components(page.getComponents())
                .availableRegions(page.getAvailableRegions())
                .sortOrder(page.getSortOrder())
                .showInNavigation(page.isShowInNavigation())
                .parentPageId(page.getParentPageId())
                .seoMetadata(mapSeoMetadata(page.getSeoMetadata()))
                .status(page.getStatus())
                .publishDate(page.getPublishDate())
                .createdAt(page.getCreatedAt())
                .updatedAt(page.getUpdatedAt())
                .tags(page.getTags())
                .build();
    }

    private static SeoMetadataDto mapSeoMetadata(com.gogidix.corporate.website.domain.model.SeoMetadata seo) {
        if (seo == null) return null;
        return SeoMetadataDto.builder()
                .metaTitle(seo.getMetaTitle())
                .metaDescription(seo.getMetaDescription())
                .metaKeywords(seo.getMetaKeywords())
                .ogTitle(seo.getOgTitle())
                .ogDescription(seo.getOgDescription())
                .ogImage(seo.getOgImage())
                .ogType(seo.getOgType())
                .twitterCard(seo.getTwitterCard())
                .canonicalUrl(seo.getCanonicalUrl())
                .noIndex(seo.isNoIndex())
                .noFollow(seo.isNoFollow())
                .alternateLanguages(seo.getAlternateLanguages())
                .build();
    }
}
