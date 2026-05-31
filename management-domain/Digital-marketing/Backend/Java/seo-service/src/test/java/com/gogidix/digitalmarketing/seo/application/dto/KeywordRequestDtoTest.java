package com.gogidix.digitalmarketing.seo.application.dto;

import com.gogidix.digitalmarketing.seo.application.dto.KeywordRequestDto;
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
class KeywordRequestDtoTest {

        @Test
    void testBuilder() {
        KeywordRequestDto dto = KeywordRequestDto.builder()
                        .tenantId("test-tenantId")
            .keyword("test-keyword")
            .domain("test-domain")
            .volume("test-volume")
            .difficulty("test-difficulty")
            .ranking("test-ranking")
            .previousRanking("test-previousRanking")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-keyword", dto.getKeyword());
        assertEquals("test-domain", dto.getDomain());
        assertEquals("test-volume", dto.getVolume());
        assertEquals("test-difficulty", dto.getDifficulty());
        assertEquals("test-ranking", dto.getRanking());
        assertEquals("test-previousRanking", dto.getPreviousRanking());
    }

    @Test
    void testSettersAndGetters() {
        KeywordRequestDto dto = new KeywordRequestDto();
        dto.setTenantId("val-tenantId");
        dto.setKeyword("val-keyword");
        dto.setDomain("val-domain");
        dto.setVolume("val-volume");
        dto.setDifficulty("val-difficulty");
        dto.setRanking("val-ranking");
        dto.setPreviousRanking("val-previousRanking");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-keyword", dto.getKeyword());
        assertEquals("val-domain", dto.getDomain());
        assertEquals("val-volume", dto.getVolume());
        assertEquals("val-difficulty", dto.getDifficulty());
        assertEquals("val-ranking", dto.getRanking());
        assertEquals("val-previousRanking", dto.getPreviousRanking());
    }

    @Test
    void testEqualsAndHashCode() {
        KeywordRequestDto dto1 = KeywordRequestDto.builder()
                        .tenantId("test-tenantId")
            .keyword("test-keyword")
            .domain("test-domain")
            .volume("test-volume")
            .difficulty("test-difficulty")
            .ranking("test-ranking")
            .previousRanking("test-previousRanking")
            .build();
        KeywordRequestDto dto2 = KeywordRequestDto.builder()
                        .tenantId("test-tenantId")
            .keyword("test-keyword")
            .domain("test-domain")
            .volume("test-volume")
            .difficulty("test-difficulty")
            .ranking("test-ranking")
            .previousRanking("test-previousRanking")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KeywordRequestDto dto = KeywordRequestDto.builder()
                        .tenantId("test-tenantId")
            .keyword("test-keyword")
            .domain("test-domain")
            .volume("test-volume")
            .difficulty("test-difficulty")
            .ranking("test-ranking")
            .previousRanking("test-previousRanking")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}