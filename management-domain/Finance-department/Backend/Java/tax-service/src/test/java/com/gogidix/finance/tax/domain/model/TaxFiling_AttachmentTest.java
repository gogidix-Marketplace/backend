package com.gogidix.finance.tax.domain.model;

import com.gogidix.finance.tax.domain.model.TaxFiling;
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
class TaxFiling_AttachmentTest {

        @Test
    void testBuilder() {
        TaxFiling.Attachment dto = TaxFiling.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .storageLocation("test-storageLocation")
            .url("test-url")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .uploadedBy("test-uploadedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-attachmentId", dto.getAttachmentId());
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileType", dto.getFileType());
        assertEquals(42L, dto.getFileSize());
        assertEquals("test-storageLocation", dto.getStorageLocation());
        assertEquals("test-url", dto.getUrl());
        assertEquals("test-uploadedBy", dto.getUploadedBy());
    }

    @Test
    void testSettersAndGetters() {
        TaxFiling.Attachment dto = new TaxFiling.Attachment();
        dto.setAttachmentId("val-attachmentId");
        dto.setFileName("val-fileName");
        dto.setFileType("val-fileType");
        dto.setStorageLocation("val-storageLocation");
        dto.setUrl("val-url");
        dto.setUploadedBy("val-uploadedBy");
        assertEquals("val-attachmentId", dto.getAttachmentId());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileType", dto.getFileType());
        assertEquals("val-storageLocation", dto.getStorageLocation());
        assertEquals("val-url", dto.getUrl());
        assertEquals("val-uploadedBy", dto.getUploadedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxFiling.Attachment dto1 = TaxFiling.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .storageLocation("test-storageLocation")
            .url("test-url")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .uploadedBy("test-uploadedBy")
            .build();
        TaxFiling.Attachment dto2 = TaxFiling.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .storageLocation("test-storageLocation")
            .url("test-url")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .uploadedBy("test-uploadedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        TaxFiling.Attachment dto = TaxFiling.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .storageLocation("test-storageLocation")
            .url("test-url")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .uploadedBy("test-uploadedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}