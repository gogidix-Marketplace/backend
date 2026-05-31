package com.gogidix.sales.onboarding.domain.model;

import com.gogidix.sales.onboarding.domain.model.DocumentChecklist;
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
class DocumentChecklist_DocumentItemTest {

        @Test
    void testBuilder() {
        DocumentChecklist.DocumentItem dto = DocumentChecklist.DocumentItem.builder()
                        .itemId("test-itemId")
            .documentType("test-documentType")
            .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSizeBytes(42L)
            .required(true)
            .status(null)
            .uploadedBy("test-uploadedBy")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verificationNotes("test-verificationNotes")
            .addedBy("test-addedBy")
            .addedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-itemId", dto.getItemId());
        assertEquals("test-documentType", dto.getDocumentType());
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileUrl", dto.getFileUrl());
        assertEquals(42L, dto.getFileSizeBytes());
        assertTrue(dto.getRequired());
        assertEquals("test-uploadedBy", dto.getUploadedBy());
        assertEquals("test-verifiedBy", dto.getVerifiedBy());
        assertEquals("test-verificationNotes", dto.getVerificationNotes());
        assertEquals("test-addedBy", dto.getAddedBy());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        DocumentChecklist.DocumentItem dto = new DocumentChecklist.DocumentItem();
        dto.setItemId("val-itemId");
        dto.setDocumentType("val-documentType");
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        dto.setRequired(true);
        dto.setUploadedBy("val-uploadedBy");
        dto.setVerifiedBy("val-verifiedBy");
        dto.setVerificationNotes("val-verificationNotes");
        dto.setAddedBy("val-addedBy");
        dto.setNotes("val-notes");
        assertEquals("val-itemId", dto.getItemId());
        assertEquals("val-documentType", dto.getDocumentType());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
        assertTrue(dto.getRequired());
        assertEquals("val-uploadedBy", dto.getUploadedBy());
        assertEquals("val-verifiedBy", dto.getVerifiedBy());
        assertEquals("val-verificationNotes", dto.getVerificationNotes());
        assertEquals("val-addedBy", dto.getAddedBy());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        DocumentChecklist.DocumentItem dto1 = DocumentChecklist.DocumentItem.builder()
                        .itemId("test-itemId")
            .documentType("test-documentType")
            .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSizeBytes(42L)
            .required(true)
            .status(null)
            .uploadedBy("test-uploadedBy")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verificationNotes("test-verificationNotes")
            .addedBy("test-addedBy")
            .addedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        DocumentChecklist.DocumentItem dto2 = DocumentChecklist.DocumentItem.builder()
                        .itemId("test-itemId")
            .documentType("test-documentType")
            .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSizeBytes(42L)
            .required(true)
            .status(null)
            .uploadedBy("test-uploadedBy")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verificationNotes("test-verificationNotes")
            .addedBy("test-addedBy")
            .addedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DocumentChecklist.DocumentItem dto = DocumentChecklist.DocumentItem.builder()
                        .itemId("test-itemId")
            .documentType("test-documentType")
            .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSizeBytes(42L)
            .required(true)
            .status(null)
            .uploadedBy("test-uploadedBy")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verifiedBy("test-verifiedBy")
            .verifiedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .verificationNotes("test-verificationNotes")
            .addedBy("test-addedBy")
            .addedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}