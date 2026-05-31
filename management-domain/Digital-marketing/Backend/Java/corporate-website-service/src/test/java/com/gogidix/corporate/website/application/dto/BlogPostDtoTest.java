package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.BlogPostDto;
import com.gogidix.corporate.website.domain.model.ContentStatus;
import com.gogidix.corporate.website.domain.model.Language;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class BlogPostDtoTest {

        @Test
    void testBuilder() {
        BlogPostDto dto = BlogPostDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .featuredImage("test-featuredImage")
            .featuredImageAlt("test-featuredImageAlt")
            .gallery("test-gallery")
            .tags(Collections.emptyList())
            .categories(Collections.emptyList())
            .authorId("test-authorId")
            .authorName("test-authorName")
            .authorAvatar("test-authorAvatar")
            .availableRegions(null)
            .featured(true)
            .featuredOrder(42)
            .allowComments(true)
            .readTimeMinutes(42)
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .viewCount(42L)
            .likeCount(42L)
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-slug", dto.getSlug());
        assertEquals("test-featuredImage", dto.getFeaturedImage());
        assertEquals("test-featuredImageAlt", dto.getFeaturedImageAlt());
        assertEquals("test-gallery", dto.getGallery());
        assertEquals("test-authorId", dto.getAuthorId());
        assertEquals("test-authorName", dto.getAuthorName());
        assertEquals("test-authorAvatar", dto.getAuthorAvatar());
        assertTrue(dto.isFeatured());
        assertEquals(42, dto.getFeaturedOrder());
        assertTrue(dto.isAllowComments());
        assertEquals(42, dto.getReadTimeMinutes());
        assertEquals(42L, dto.getViewCount());
        assertEquals(42L, dto.getLikeCount());
    }

    @Test
    void testSettersAndGetters() {
        BlogPostDto dto = new BlogPostDto();
        dto.setId("val-id");
        dto.setSlug("val-slug");
        dto.setFeaturedImage("val-featuredImage");
        dto.setFeaturedImageAlt("val-featuredImageAlt");
        dto.setGallery("val-gallery");
        dto.setAuthorId("val-authorId");
        dto.setAuthorName("val-authorName");
        dto.setAuthorAvatar("val-authorAvatar");
        dto.setFeatured(true);
        dto.setFeaturedOrder(99);
        dto.setAllowComments(true);
        dto.setReadTimeMinutes(99);
        assertEquals("val-id", dto.getId());
        assertEquals("val-slug", dto.getSlug());
        assertEquals("val-featuredImage", dto.getFeaturedImage());
        assertEquals("val-featuredImageAlt", dto.getFeaturedImageAlt());
        assertEquals("val-gallery", dto.getGallery());
        assertEquals("val-authorId", dto.getAuthorId());
        assertEquals("val-authorName", dto.getAuthorName());
        assertEquals("val-authorAvatar", dto.getAuthorAvatar());
        assertTrue(dto.isFeatured());
        assertEquals(99, dto.getFeaturedOrder());
        assertTrue(dto.isAllowComments());
        assertEquals(99, dto.getReadTimeMinutes());
    }

    @Test
    void testEqualsAndHashCode() {
        BlogPostDto dto1 = BlogPostDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .featuredImage("test-featuredImage")
            .featuredImageAlt("test-featuredImageAlt")
            .gallery("test-gallery")
            .tags(Collections.emptyList())
            .categories(Collections.emptyList())
            .authorId("test-authorId")
            .authorName("test-authorName")
            .authorAvatar("test-authorAvatar")
            .availableRegions(null)
            .featured(true)
            .featuredOrder(42)
            .allowComments(true)
            .readTimeMinutes(42)
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .viewCount(42L)
            .likeCount(42L)
            .build();
        BlogPostDto dto2 = BlogPostDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .featuredImage("test-featuredImage")
            .featuredImageAlt("test-featuredImageAlt")
            .gallery("test-gallery")
            .tags(Collections.emptyList())
            .categories(Collections.emptyList())
            .authorId("test-authorId")
            .authorName("test-authorName")
            .authorAvatar("test-authorAvatar")
            .availableRegions(null)
            .featured(true)
            .featuredOrder(42)
            .allowComments(true)
            .readTimeMinutes(42)
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .viewCount(42L)
            .likeCount(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        BlogPostDto dto = BlogPostDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .featuredImage("test-featuredImage")
            .featuredImageAlt("test-featuredImageAlt")
            .gallery("test-gallery")
            .tags(Collections.emptyList())
            .categories(Collections.emptyList())
            .authorId("test-authorId")
            .authorName("test-authorName")
            .authorAvatar("test-authorAvatar")
            .availableRegions(null)
            .featured(true)
            .featuredOrder(42)
            .allowComments(true)
            .readTimeMinutes(42)
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .viewCount(42L)
            .likeCount(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}