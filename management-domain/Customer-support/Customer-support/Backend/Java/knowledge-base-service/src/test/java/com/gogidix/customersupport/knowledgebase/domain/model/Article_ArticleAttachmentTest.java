package com.gogidix.customersupport.knowledgebase.domain.model;

import com.gogidix.customersupport.knowledgebase.domain.model.Article;
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
class Article_ArticleAttachmentTest {

        @Test
    void testBuilder() {
        Article.ArticleAttachment dto = Article.ArticleAttachment.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .mimeType("test-mimeType")
            .build();
        assertNotNull(dto);
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileUrl", dto.getFileUrl());
        assertEquals("test-fileSize", dto.getFileSize());
        assertEquals("test-mimeType", dto.getMimeType());
    }

    @Test
    void testSettersAndGetters() {
        Article.ArticleAttachment dto = new Article.ArticleAttachment();
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        dto.setFileSize("val-fileSize");
        dto.setMimeType("val-mimeType");
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
        assertEquals("val-fileSize", dto.getFileSize());
        assertEquals("val-mimeType", dto.getMimeType());
    }

    @Test
    void testEqualsAndHashCode() {
        Article.ArticleAttachment dto1 = Article.ArticleAttachment.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .mimeType("test-mimeType")
            .build();
        Article.ArticleAttachment dto2 = Article.ArticleAttachment.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .mimeType("test-mimeType")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Article.ArticleAttachment dto = Article.ArticleAttachment.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .mimeType("test-mimeType")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}