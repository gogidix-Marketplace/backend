package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.PressReleaseDto;
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
class PressReleaseDtoTest {

        @Test
    void testBuilder() {
        PressReleaseDto dto = PressReleaseDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .releaseDate("test-releaseDate")
            .contactName("test-contactName")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .mediaContacts(Collections.emptyList())
            .availableRegions(null)
            .embargoed(true)
            .immediateRelease(true)
            .tags(Collections.emptyList())
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .organization("test-organization")
            .tickerSymbol("test-tickerSymbol")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-slug", dto.getSlug());
        assertEquals("test-releaseDate", dto.getReleaseDate());
        assertEquals("test-contactName", dto.getContactName());
        assertEquals("test-contactEmail", dto.getContactEmail());
        assertEquals("test-contactPhone", dto.getContactPhone());
        assertTrue(dto.isEmbargoed());
        assertTrue(dto.isImmediateRelease());
        assertEquals("test-organization", dto.getOrganization());
        assertEquals("test-tickerSymbol", dto.getTickerSymbol());
    }

    @Test
    void testSettersAndGetters() {
        PressReleaseDto dto = new PressReleaseDto();
        dto.setId("val-id");
        dto.setSlug("val-slug");
        dto.setReleaseDate("val-releaseDate");
        dto.setContactName("val-contactName");
        dto.setContactEmail("val-contactEmail");
        dto.setContactPhone("val-contactPhone");
        dto.setEmbargoed(true);
        dto.setImmediateRelease(true);
        dto.setOrganization("val-organization");
        dto.setTickerSymbol("val-tickerSymbol");
        assertEquals("val-id", dto.getId());
        assertEquals("val-slug", dto.getSlug());
        assertEquals("val-releaseDate", dto.getReleaseDate());
        assertEquals("val-contactName", dto.getContactName());
        assertEquals("val-contactEmail", dto.getContactEmail());
        assertEquals("val-contactPhone", dto.getContactPhone());
        assertTrue(dto.isEmbargoed());
        assertTrue(dto.isImmediateRelease());
        assertEquals("val-organization", dto.getOrganization());
        assertEquals("val-tickerSymbol", dto.getTickerSymbol());
    }

    @Test
    void testEqualsAndHashCode() {
        PressReleaseDto dto1 = PressReleaseDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .releaseDate("test-releaseDate")
            .contactName("test-contactName")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .mediaContacts(Collections.emptyList())
            .availableRegions(null)
            .embargoed(true)
            .immediateRelease(true)
            .tags(Collections.emptyList())
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .organization("test-organization")
            .tickerSymbol("test-tickerSymbol")
            .build();
        PressReleaseDto dto2 = PressReleaseDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .releaseDate("test-releaseDate")
            .contactName("test-contactName")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .mediaContacts(Collections.emptyList())
            .availableRegions(null)
            .embargoed(true)
            .immediateRelease(true)
            .tags(Collections.emptyList())
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .organization("test-organization")
            .tickerSymbol("test-tickerSymbol")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PressReleaseDto dto = PressReleaseDto.builder()
                        .id("test-id")
            .slug("test-slug")
            .localizedContent(null)
            .language(Language.EN)
            .releaseDate("test-releaseDate")
            .contactName("test-contactName")
            .contactEmail("test-contactEmail")
            .contactPhone("test-contactPhone")
            .mediaContacts(Collections.emptyList())
            .availableRegions(null)
            .embargoed(true)
            .immediateRelease(true)
            .tags(Collections.emptyList())
            .seoMetadata(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .organization("test-organization")
            .tickerSymbol("test-tickerSymbol")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}