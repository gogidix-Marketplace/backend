package com.gogidix.corporate.website.application.dto;

import com.gogidix.corporate.website.application.dto.ContentRequest;
import com.gogidix.corporate.website.domain.model.ContentStatus;
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
class ContentRequestTest {

        @Test
    void testBuilder() {
        ContentRequest dto = ContentRequest.builder()
                        .availableRegions(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .unpublishDate(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .build();
        assertNotNull(dto);

    }

    @Test
    void testSettersAndGetters() {
        ContentRequest dto = new ContentRequest();


    }

    @Test
    void testEqualsAndHashCode() {
        ContentRequest dto1 = ContentRequest.builder()
                        .availableRegions(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .unpublishDate(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .build();
        ContentRequest dto2 = ContentRequest.builder()
                        .availableRegions(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .unpublishDate(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ContentRequest dto = ContentRequest.builder()
                        .availableRegions(null)
            .status(ContentStatus.DRAFT)
            .publishDate(LocalDateTime.of(2025,1,15,10,0))
            .unpublishDate(LocalDateTime.of(2025,1,15,10,0))
            .tags(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}