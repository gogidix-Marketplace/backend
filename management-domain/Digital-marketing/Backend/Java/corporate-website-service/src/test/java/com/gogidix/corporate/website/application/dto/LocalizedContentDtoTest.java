package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.LocalizedContentDto;
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
class LocalizedContentDtoTest {

        @Test
    void testBuilder() {
        LocalizedContentDto dto = LocalizedContentDto.builder()
                        .language(Language.EN)
            .title("test-title")
            .content("test-content")
            .excerpt("test-excerpt")
            .slug("test-slug")
            .build();
        assertNotNull(dto);
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-content", dto.getContent());
        assertEquals("test-excerpt", dto.getExcerpt());
        assertEquals("test-slug", dto.getSlug());
    }

    @Test
    void testSettersAndGetters() {
        LocalizedContentDto dto = new LocalizedContentDto();
        dto.setTitle("val-title");
        dto.setContent("val-content");
        dto.setExcerpt("val-excerpt");
        dto.setSlug("val-slug");
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-content", dto.getContent());
        assertEquals("val-excerpt", dto.getExcerpt());
        assertEquals("val-slug", dto.getSlug());
    }

    @Test
    void testEqualsAndHashCode() {
        LocalizedContentDto dto1 = LocalizedContentDto.builder()
                        .language(Language.EN)
            .title("test-title")
            .content("test-content")
            .excerpt("test-excerpt")
            .slug("test-slug")
            .build();
        LocalizedContentDto dto2 = LocalizedContentDto.builder()
                        .language(Language.EN)
            .title("test-title")
            .content("test-content")
            .excerpt("test-excerpt")
            .slug("test-slug")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LocalizedContentDto dto = LocalizedContentDto.builder()
                        .language(Language.EN)
            .title("test-title")
            .content("test-content")
            .excerpt("test-excerpt")
            .slug("test-slug")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}