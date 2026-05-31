package com.gogidix.corporatecms.application.dto;

import com.gogidix.corporatecms.application.dto.ContentDTO;
import com.gogidix.corporatecms.domain.enums.ContentStatus;
import com.gogidix.corporatecms.domain.enums.ContentType;
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
class ContentDTOTest {

        @Test
    void testBuilder() {
        ContentDTO dto = ContentDTO.builder()
                        .id("test-id")
            .slug("test-slug")
            .type(ContentType.PAGE)
            .status(ContentStatus.DRAFT)
            .title("test-title")
            .summary("test-summary")
            .body("test-body")
            .featuredImageId("test-featuredImageId")
            .mediaIds(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .seoData(Collections.emptyMap())
            .authorId("test-authorId")
            .authorName("test-authorName")
            .categoryId("test-categoryId")
            .categoryName("test-categoryName")
            .tags(Collections.emptyList())
            .relatedContentIds(Collections.emptyList())
            .template("test-template")
            .templateData(Collections.emptyMap())
            .allowComments(true)
            .viewCount(42)
            .readTimeMinutes(42)
            .currentVersion(42)
            .published(true)
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .scheduledPublishAt(LocalDateTime.of(2025,1,15,10,0))
            .unpublishedAt(LocalDateTime.of(2025,1,15,10,0))
            .tenantId("test-tenantId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdBy("test-createdBy")
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-slug", dto.getSlug());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-summary", dto.getSummary());
        assertEquals("test-body", dto.getBody());
        assertEquals("test-featuredImageId", dto.getFeaturedImageId());
        assertEquals("test-authorId", dto.getAuthorId());
        assertEquals("test-authorName", dto.getAuthorName());
        assertEquals("test-categoryId", dto.getCategoryId());
        assertEquals("test-categoryName", dto.getCategoryName());
        assertEquals("test-template", dto.getTemplate());
        assertTrue(dto.getAllowComments());
        assertEquals(42, dto.getViewCount());
        assertEquals(42, dto.getReadTimeMinutes());
        assertEquals(42, dto.getCurrentVersion());
        assertTrue(dto.getPublished());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-createdBy", dto.getCreatedBy());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        ContentDTO dto = new ContentDTO();
        dto.setId("val-id");
        dto.setSlug("val-slug");
        dto.setTitle("val-title");
        dto.setSummary("val-summary");
        dto.setBody("val-body");
        dto.setFeaturedImageId("val-featuredImageId");
        dto.setAuthorId("val-authorId");
        dto.setAuthorName("val-authorName");
        dto.setCategoryId("val-categoryId");
        dto.setCategoryName("val-categoryName");
        dto.setTemplate("val-template");
        dto.setAllowComments(true);
        dto.setViewCount(99);
        dto.setReadTimeMinutes(99);
        dto.setCurrentVersion(99);
        dto.setPublished(true);
        dto.setTenantId("val-tenantId");
        dto.setCreatedBy("val-createdBy");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-id", dto.getId());
        assertEquals("val-slug", dto.getSlug());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-summary", dto.getSummary());
        assertEquals("val-body", dto.getBody());
        assertEquals("val-featuredImageId", dto.getFeaturedImageId());
        assertEquals("val-authorId", dto.getAuthorId());
        assertEquals("val-authorName", dto.getAuthorName());
        assertEquals("val-categoryId", dto.getCategoryId());
        assertEquals("val-categoryName", dto.getCategoryName());
        assertEquals("val-template", dto.getTemplate());
        assertTrue(dto.getAllowComments());
        assertEquals(99, dto.getViewCount());
        assertEquals(99, dto.getReadTimeMinutes());
        assertEquals(99, dto.getCurrentVersion());
        assertTrue(dto.getPublished());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-createdBy", dto.getCreatedBy());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        ContentDTO dto1 = ContentDTO.builder()
                        .id("test-id")
            .slug("test-slug")
            .type(ContentType.PAGE)
            .status(ContentStatus.DRAFT)
            .title("test-title")
            .summary("test-summary")
            .body("test-body")
            .featuredImageId("test-featuredImageId")
            .mediaIds(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .seoData(Collections.emptyMap())
            .authorId("test-authorId")
            .authorName("test-authorName")
            .categoryId("test-categoryId")
            .categoryName("test-categoryName")
            .tags(Collections.emptyList())
            .relatedContentIds(Collections.emptyList())
            .template("test-template")
            .templateData(Collections.emptyMap())
            .allowComments(true)
            .viewCount(42)
            .readTimeMinutes(42)
            .currentVersion(42)
            .published(true)
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .scheduledPublishAt(LocalDateTime.of(2025,1,15,10,0))
            .unpublishedAt(LocalDateTime.of(2025,1,15,10,0))
            .tenantId("test-tenantId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdBy("test-createdBy")
            .updatedBy("test-updatedBy")
            .build();
        ContentDTO dto2 = ContentDTO.builder()
                        .id("test-id")
            .slug("test-slug")
            .type(ContentType.PAGE)
            .status(ContentStatus.DRAFT)
            .title("test-title")
            .summary("test-summary")
            .body("test-body")
            .featuredImageId("test-featuredImageId")
            .mediaIds(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .seoData(Collections.emptyMap())
            .authorId("test-authorId")
            .authorName("test-authorName")
            .categoryId("test-categoryId")
            .categoryName("test-categoryName")
            .tags(Collections.emptyList())
            .relatedContentIds(Collections.emptyList())
            .template("test-template")
            .templateData(Collections.emptyMap())
            .allowComments(true)
            .viewCount(42)
            .readTimeMinutes(42)
            .currentVersion(42)
            .published(true)
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .scheduledPublishAt(LocalDateTime.of(2025,1,15,10,0))
            .unpublishedAt(LocalDateTime.of(2025,1,15,10,0))
            .tenantId("test-tenantId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdBy("test-createdBy")
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ContentDTO dto = ContentDTO.builder()
                        .id("test-id")
            .slug("test-slug")
            .type(ContentType.PAGE)
            .status(ContentStatus.DRAFT)
            .title("test-title")
            .summary("test-summary")
            .body("test-body")
            .featuredImageId("test-featuredImageId")
            .mediaIds(Collections.emptyList())
            .metadata(Collections.emptyMap())
            .seoData(Collections.emptyMap())
            .authorId("test-authorId")
            .authorName("test-authorName")
            .categoryId("test-categoryId")
            .categoryName("test-categoryName")
            .tags(Collections.emptyList())
            .relatedContentIds(Collections.emptyList())
            .template("test-template")
            .templateData(Collections.emptyMap())
            .allowComments(true)
            .viewCount(42)
            .readTimeMinutes(42)
            .currentVersion(42)
            .published(true)
            .publishedAt(LocalDateTime.of(2025,1,15,10,0))
            .scheduledPublishAt(LocalDateTime.of(2025,1,15,10,0))
            .unpublishedAt(LocalDateTime.of(2025,1,15,10,0))
            .tenantId("test-tenantId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .createdBy("test-createdBy")
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}