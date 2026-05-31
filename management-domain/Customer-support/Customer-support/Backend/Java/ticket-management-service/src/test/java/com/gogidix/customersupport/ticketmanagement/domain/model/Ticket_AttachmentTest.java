package com.gogidix.customersupport.ticketmanagement.domain.model;

import com.gogidix.customersupport.ticketmanagement.domain.model.Ticket;
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
class AttachmentTest {

        @Test
    void testBuilder() {
        Ticket.Attachment dto = Ticket.Attachment.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .contentType("test-contentType")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .uploadedBy("test-uploadedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileUrl", dto.getFileUrl());
        assertEquals("test-fileSize", dto.getFileSize());
        assertEquals("test-contentType", dto.getContentType());
        assertEquals("test-uploadedBy", dto.getUploadedBy());
    }

    @Test
    void testSettersAndGetters() {
        Ticket.Attachment dto = new Ticket.Attachment();
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        dto.setFileSize("val-fileSize");
        dto.setContentType("val-contentType");
        dto.setUploadedBy("val-uploadedBy");
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
        assertEquals("val-fileSize", dto.getFileSize());
        assertEquals("val-contentType", dto.getContentType());
        assertEquals("val-uploadedBy", dto.getUploadedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        Ticket.Attachment dto1 = Ticket.Attachment.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .contentType("test-contentType")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .uploadedBy("test-uploadedBy")
            .build();
        Ticket.Attachment dto2 = Ticket.Attachment.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .contentType("test-contentType")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .uploadedBy("test-uploadedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Ticket.Attachment dto = Ticket.Attachment.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .contentType("test-contentType")
            .uploadedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .uploadedBy("test-uploadedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}