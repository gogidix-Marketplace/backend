package com.gogidix.corporatecms.domain.model;

import com.gogidix.corporatecms.domain.model.Content;
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
class Content_ContentVersionTest {

        @Test
    void testBuilder() {
        Content.ContentVersion dto = Content.ContentVersion.builder()
                        .versionNumber(42)
            .title("test-title")
            .body("test-body")
            .summary("test-summary")
            .comment("test-comment")
            .authorId("test-authorId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getVersionNumber());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-body", dto.getBody());
        assertEquals("test-summary", dto.getSummary());
        assertEquals("test-comment", dto.getComment());
        assertEquals("test-authorId", dto.getAuthorId());
    }

    @Test
    void testSettersAndGetters() {
        Content.ContentVersion dto = new Content.ContentVersion();
        dto.setVersionNumber(99);
        dto.setTitle("val-title");
        dto.setBody("val-body");
        dto.setSummary("val-summary");
        dto.setComment("val-comment");
        dto.setAuthorId("val-authorId");
        assertEquals(99, dto.getVersionNumber());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-body", dto.getBody());
        assertEquals("val-summary", dto.getSummary());
        assertEquals("val-comment", dto.getComment());
        assertEquals("val-authorId", dto.getAuthorId());
    }

    @Test
    void testEqualsAndHashCode() {
        Content.ContentVersion dto1 = Content.ContentVersion.builder()
                        .versionNumber(42)
            .title("test-title")
            .body("test-body")
            .summary("test-summary")
            .comment("test-comment")
            .authorId("test-authorId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        Content.ContentVersion dto2 = Content.ContentVersion.builder()
                        .versionNumber(42)
            .title("test-title")
            .body("test-body")
            .summary("test-summary")
            .comment("test-comment")
            .authorId("test-authorId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Content.ContentVersion dto = Content.ContentVersion.builder()
                        .versionNumber(42)
            .title("test-title")
            .body("test-body")
            .summary("test-summary")
            .comment("test-comment")
            .authorId("test-authorId")
            .createdAt(LocalDateTime.of(2025,1,15,10,0))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}