package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.PageDto;
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
class PageDtoTest {

        @Test
    void testBuilder() {
        PageDto dto = PageDto.builder()
                        .id("test-id")
            .pageKey("test-pageKey")
            .path("test-path")
            .localizedContent(null)
            .language(Language.EN)
            .layout("test-layout")
            .template("test-template")
            .components(Collections.emptyList())
            .availableRegions(null)
            .sortOrder(42)
            .showInNavigation(true)
            .parentPageId("test-parentPageId")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-pageKey", dto.getPageKey());
        assertEquals("test-path", dto.getPath());
        assertEquals("test-layout", dto.getLayout());
        assertEquals("test-template", dto.getTemplate());
        assertEquals(42, dto.getSortOrder());
        assertTrue(dto.isShowInNavigation());
        assertEquals("test-parentPageId", dto.getParentPageId());
    }

    @Test
    void testSettersAndGetters() {
        PageDto dto = new PageDto();
        dto.setId("val-id");
        dto.setPageKey("val-pageKey");
        dto.setPath("val-path");
        dto.setLayout("val-layout");
        dto.setTemplate("val-template");
        dto.setSortOrder(99);
        dto.setShowInNavigation(true);
        dto.setParentPageId("val-parentPageId");
        assertEquals("val-id", dto.getId());
        assertEquals("val-pageKey", dto.getPageKey());
        assertEquals("val-path", dto.getPath());
        assertEquals("val-layout", dto.getLayout());
        assertEquals("val-template", dto.getTemplate());
        assertEquals(99, dto.getSortOrder());
        assertTrue(dto.isShowInNavigation());
        assertEquals("val-parentPageId", dto.getParentPageId());
    }

    @Test
    void testEqualsAndHashCode() {
        PageDto dto1 = PageDto.builder()
                        .id("test-id")
            .pageKey("test-pageKey")
            .path("test-path")
            .localizedContent(null)
            .language(Language.EN)
            .layout("test-layout")
            .template("test-template")
            .components(Collections.emptyList())
            .availableRegions(null)
            .sortOrder(42)
            .showInNavigation(true)
            .parentPageId("test-parentPageId")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .build();
        PageDto dto2 = PageDto.builder()
                        .id("test-id")
            .pageKey("test-pageKey")
            .path("test-path")
            .localizedContent(null)
            .language(Language.EN)
            .layout("test-layout")
            .template("test-template")
            .components(Collections.emptyList())
            .availableRegions(null)
            .sortOrder(42)
            .showInNavigation(true)
            .parentPageId("test-parentPageId")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PageDto dto = PageDto.builder()
                        .id("test-id")
            .pageKey("test-pageKey")
            .path("test-path")
            .localizedContent(null)
            .language(Language.EN)
            .layout("test-layout")
            .template("test-template")
            .components(Collections.emptyList())
            .availableRegions(null)
            .sortOrder(42)
            .showInNavigation(true)
            .parentPageId("test-parentPageId")
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .updatedAt(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}