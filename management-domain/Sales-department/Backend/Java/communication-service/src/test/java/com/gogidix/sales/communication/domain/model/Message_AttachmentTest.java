package com.gogidix.sales.communication.domain.model;

import com.gogidix.sales.communication.domain.model.Message;
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
class Message_AttachmentTest {

        @Test
    void testBuilder() {
        Message.Attachment dto = Message.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .storageProvider("test-storageProvider")
            .contentType("test-contentType")
            .build();
        assertNotNull(dto);
        assertEquals("test-attachmentId", dto.getAttachmentId());
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileType", dto.getFileType());
        assertEquals(42L, dto.getFileSize());
        assertEquals("test-fileUrl", dto.getFileUrl());
        assertEquals("test-storageProvider", dto.getStorageProvider());
        assertEquals("test-contentType", dto.getContentType());
    }

    @Test
    void testSettersAndGetters() {
        Message.Attachment dto = new Message.Attachment();
        dto.setAttachmentId("val-attachmentId");
        dto.setFileName("val-fileName");
        dto.setFileType("val-fileType");
        dto.setFileUrl("val-fileUrl");
        dto.setStorageProvider("val-storageProvider");
        dto.setContentType("val-contentType");
        assertEquals("val-attachmentId", dto.getAttachmentId());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileType", dto.getFileType());
        assertEquals("val-fileUrl", dto.getFileUrl());
        assertEquals("val-storageProvider", dto.getStorageProvider());
        assertEquals("val-contentType", dto.getContentType());
    }

    @Test
    void testEqualsAndHashCode() {
        Message.Attachment dto1 = Message.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .storageProvider("test-storageProvider")
            .contentType("test-contentType")
            .build();
        Message.Attachment dto2 = Message.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .storageProvider("test-storageProvider")
            .contentType("test-contentType")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Message.Attachment dto = Message.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .storageProvider("test-storageProvider")
            .contentType("test-contentType")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}