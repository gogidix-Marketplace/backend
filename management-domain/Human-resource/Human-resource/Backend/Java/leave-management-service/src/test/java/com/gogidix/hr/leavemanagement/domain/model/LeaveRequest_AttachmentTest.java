package com.gogidix.hr.leavemanagement.domain.model;

import com.gogidix.hr.leavemanagement.domain.model.LeaveRequest;
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
class LeaveRequest_AttachmentTest {

        @Test
    void testBuilder() {
        LeaveRequest.Attachment dto = LeaveRequest.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileType("test-fileType")
            .fileSize(42L)
            .uploadedAt(LocalDateTime.of(2025,1,15,10,0))
            .uploadedBy("test-uploadedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-attachmentId", dto.getAttachmentId());
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileUrl", dto.getFileUrl());
        assertEquals("test-fileType", dto.getFileType());
        assertEquals(42L, dto.getFileSize());
        assertEquals("test-uploadedBy", dto.getUploadedBy());
    }

    @Test
    void testSettersAndGetters() {
        LeaveRequest.Attachment dto = new LeaveRequest.Attachment();
        dto.setAttachmentId("val-attachmentId");
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        dto.setFileType("val-fileType");
        dto.setUploadedBy("val-uploadedBy");
        assertEquals("val-attachmentId", dto.getAttachmentId());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
        assertEquals("val-fileType", dto.getFileType());
        assertEquals("val-uploadedBy", dto.getUploadedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveRequest.Attachment dto1 = LeaveRequest.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileType("test-fileType")
            .fileSize(42L)
            .uploadedAt(LocalDateTime.of(2025,1,15,10,0))
            .uploadedBy("test-uploadedBy")
            .build();
        LeaveRequest.Attachment dto2 = LeaveRequest.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileType("test-fileType")
            .fileSize(42L)
            .uploadedAt(LocalDateTime.of(2025,1,15,10,0))
            .uploadedBy("test-uploadedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveRequest.Attachment dto = LeaveRequest.Attachment.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileType("test-fileType")
            .fileSize(42L)
            .uploadedAt(LocalDateTime.of(2025,1,15,10,0))
            .uploadedBy("test-uploadedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}