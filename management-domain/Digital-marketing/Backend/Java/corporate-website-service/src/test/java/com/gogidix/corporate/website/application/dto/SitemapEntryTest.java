package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.SitemapEntry;
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
class SitemapEntryTest {

        @Test
    void testBuilder() {
        SitemapEntry dto = SitemapEntry.builder()
                        .url("test-url")
            .lastModified(LocalDateTime.of(2025,1,15,10,0))
            .changeFrequency("test-changeFrequency")
            .priority(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-url", dto.getUrl());
        assertEquals("test-changeFrequency", dto.getChangeFrequency());
    }

    @Test
    void testSettersAndGetters() {
        SitemapEntry dto = new SitemapEntry();
        dto.setUrl("val-url");
        dto.setChangeFrequency("val-changeFrequency");
        assertEquals("val-url", dto.getUrl());
        assertEquals("val-changeFrequency", dto.getChangeFrequency());
    }

    @Test
    void testEqualsAndHashCode() {
        SitemapEntry dto1 = SitemapEntry.builder()
                        .url("test-url")
            .lastModified(LocalDateTime.of(2025,1,15,10,0))
            .changeFrequency("test-changeFrequency")
            .priority(null)
            .build();
        SitemapEntry dto2 = SitemapEntry.builder()
                        .url("test-url")
            .lastModified(LocalDateTime.of(2025,1,15,10,0))
            .changeFrequency("test-changeFrequency")
            .priority(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SitemapEntry dto = SitemapEntry.builder()
                        .url("test-url")
            .lastModified(LocalDateTime.of(2025,1,15,10,0))
            .changeFrequency("test-changeFrequency")
            .priority(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}