package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.domain.model.BlogPost;
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
public class BlogPostDto {
    private String id;
    private String slug;
    private LocalizedContent localizedContent;
    private Language language;
    private String featuredImage;
    private String featuredImageAlt;
    private String gallery;
    private List<String> tags;
    private List<String> categories;
    private String authorId;
    private String authorName;
    private String authorAvatar;
    private Set<Region> availableRegions;
    private boolean featured;
    private Integer featuredOrder;
    private boolean allowComments;
    private Integer readTimeMinutes;
    private SeoMetadataDto seoMetadata;
    private ContentStatus status;
    private LocalDateTime publishDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long viewCount;
    private Long likeCount;

    public static BlogPostDto fromEntity(BlogPost blogPost, Language language) {
        LocalizedContent content = blogPost.getLocalizedContent().stream()
                .filter(lc -> lc.getLanguage() == language)
                .findFirst()
                .orElse(blogPost.getLocalizedContent().isEmpty() ? null : blogPost.getLocalizedContent().get(0));

        return BlogPostDto.builder()
                .id(blogPost.getId())
                .slug(blogPost.getSlug())
                .localizedContent(content)
                .language(language)
                .featuredImage(blogPost.getFeaturedImage())
                .featuredImageAlt(blogPost.getFeaturedImageAlt())
                .gallery(blogPost.getGallery())
                .tags(blogPost.getTags())
                .categories(blogPost.getCategories())
                .authorId(blogPost.getAuthorId())
                .authorName(blogPost.getAuthorName())
                .authorAvatar(blogPost.getAuthorAvatar())
                .availableRegions(blogPost.getAvailableRegions())
                .featured(blogPost.isFeatured())
                .featuredOrder(blogPost.getFeaturedOrder())
                .allowComments(blogPost.isAllowComments())
                .readTimeMinutes(blogPost.getReadTimeMinutes())
                .seoMetadata(mapSeoMetadata(blogPost.getSeoMetadata()))
                .status(blogPost.getStatus())
                .publishDate(blogPost.getPublishDate())
                .createdAt(blogPost.getCreatedAt())
                .updatedAt(blogPost.getUpdatedAt())
                .viewCount(blogPost.getViewCount())
                .likeCount(blogPost.getLikeCount())
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
                .canonicalUrl(seo.getCanonicalUrl())
                .noIndex(seo.isNoIndex())
                .noFollow(seo.isNoFollow())
                .alternateLanguages(seo.getAlternateLanguages())
                .build();
    }
}
