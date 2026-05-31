package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.SeoMetadataDto;
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
class SeoMetadataDtoTest {

        @Test
    void testBuilder() {
        SeoMetadataDto dto = SeoMetadataDto.builder()
                        .metaTitle("test-metaTitle")
            .metaDescription("test-metaDescription")
            .metaKeywords("test-metaKeywords")
            .ogTitle("test-ogTitle")
            .ogDescription("test-ogDescription")
            .ogImage("test-ogImage")
            .ogType("test-ogType")
            .twitterCard("test-twitterCard")
            .twitterTitle("test-twitterTitle")
            .twitterDescription("test-twitterDescription")
            .twitterImage("test-twitterImage")
            .canonicalUrl("test-canonicalUrl")
            .noIndex(true)
            .noFollow(true)
            .alternateLanguages(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals("test-metaTitle", dto.getMetaTitle());
        assertEquals("test-metaDescription", dto.getMetaDescription());
        assertEquals("test-metaKeywords", dto.getMetaKeywords());
        assertEquals("test-ogTitle", dto.getOgTitle());
        assertEquals("test-ogDescription", dto.getOgDescription());
        assertEquals("test-ogImage", dto.getOgImage());
        assertEquals("test-ogType", dto.getOgType());
        assertEquals("test-twitterCard", dto.getTwitterCard());
        assertEquals("test-twitterTitle", dto.getTwitterTitle());
        assertEquals("test-twitterDescription", dto.getTwitterDescription());
        assertEquals("test-twitterImage", dto.getTwitterImage());
        assertEquals("test-canonicalUrl", dto.getCanonicalUrl());
        assertTrue(dto.isNoIndex());
        assertTrue(dto.isNoFollow());
    }

    @Test
    void testSettersAndGetters() {
        SeoMetadataDto dto = new SeoMetadataDto();
        dto.setMetaTitle("val-metaTitle");
        dto.setMetaDescription("val-metaDescription");
        dto.setMetaKeywords("val-metaKeywords");
        dto.setOgTitle("val-ogTitle");
        dto.setOgDescription("val-ogDescription");
        dto.setOgImage("val-ogImage");
        dto.setOgType("val-ogType");
        dto.setTwitterCard("val-twitterCard");
        dto.setTwitterTitle("val-twitterTitle");
        dto.setTwitterDescription("val-twitterDescription");
        dto.setTwitterImage("val-twitterImage");
        dto.setCanonicalUrl("val-canonicalUrl");
        dto.setNoIndex(true);
        dto.setNoFollow(true);
        assertEquals("val-metaTitle", dto.getMetaTitle());
        assertEquals("val-metaDescription", dto.getMetaDescription());
        assertEquals("val-metaKeywords", dto.getMetaKeywords());
        assertEquals("val-ogTitle", dto.getOgTitle());
        assertEquals("val-ogDescription", dto.getOgDescription());
        assertEquals("val-ogImage", dto.getOgImage());
        assertEquals("val-ogType", dto.getOgType());
        assertEquals("val-twitterCard", dto.getTwitterCard());
        assertEquals("val-twitterTitle", dto.getTwitterTitle());
        assertEquals("val-twitterDescription", dto.getTwitterDescription());
        assertEquals("val-twitterImage", dto.getTwitterImage());
        assertEquals("val-canonicalUrl", dto.getCanonicalUrl());
        assertTrue(dto.isNoIndex());
        assertTrue(dto.isNoFollow());
    }

    @Test
    void testEqualsAndHashCode() {
        SeoMetadataDto dto1 = SeoMetadataDto.builder()
                        .metaTitle("test-metaTitle")
            .metaDescription("test-metaDescription")
            .metaKeywords("test-metaKeywords")
            .ogTitle("test-ogTitle")
            .ogDescription("test-ogDescription")
            .ogImage("test-ogImage")
            .ogType("test-ogType")
            .twitterCard("test-twitterCard")
            .twitterTitle("test-twitterTitle")
            .twitterDescription("test-twitterDescription")
            .twitterImage("test-twitterImage")
            .canonicalUrl("test-canonicalUrl")
            .noIndex(true)
            .noFollow(true)
            .alternateLanguages(Collections.emptyMap())
            .build();
        SeoMetadataDto dto2 = SeoMetadataDto.builder()
                        .metaTitle("test-metaTitle")
            .metaDescription("test-metaDescription")
            .metaKeywords("test-metaKeywords")
            .ogTitle("test-ogTitle")
            .ogDescription("test-ogDescription")
            .ogImage("test-ogImage")
            .ogType("test-ogType")
            .twitterCard("test-twitterCard")
            .twitterTitle("test-twitterTitle")
            .twitterDescription("test-twitterDescription")
            .twitterImage("test-twitterImage")
            .canonicalUrl("test-canonicalUrl")
            .noIndex(true)
            .noFollow(true)
            .alternateLanguages(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        SeoMetadataDto dto = SeoMetadataDto.builder()
                        .metaTitle("test-metaTitle")
            .metaDescription("test-metaDescription")
            .metaKeywords("test-metaKeywords")
            .ogTitle("test-ogTitle")
            .ogDescription("test-ogDescription")
            .ogImage("test-ogImage")
            .ogType("test-ogType")
            .twitterCard("test-twitterCard")
            .twitterTitle("test-twitterTitle")
            .twitterDescription("test-twitterDescription")
            .twitterImage("test-twitterImage")
            .canonicalUrl("test-canonicalUrl")
            .noIndex(true)
            .noFollow(true)
            .alternateLanguages(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}