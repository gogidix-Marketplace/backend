package com.gogidix.hr.leavemanagement.domain.port.in;

import com.gogidix.hr.leavemanagement.domain.port.in.LeaveCommand;
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
class LeaveCommand_AttachmentRequestTest {

        @Test
    void testBuilder() {
        LeaveCommand.AttachmentRequest dto = LeaveCommand.AttachmentRequest.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileType("test-fileType")
            .fileSize(42L)
            .build();
        assertNotNull(dto);
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileUrl", dto.getFileUrl());
        assertEquals("test-fileType", dto.getFileType());
        assertEquals(42L, dto.getFileSize());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.AttachmentRequest dto = new LeaveCommand.AttachmentRequest();
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        dto.setFileType("val-fileType");
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
        assertEquals("val-fileType", dto.getFileType());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.AttachmentRequest dto1 = LeaveCommand.AttachmentRequest.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileType("test-fileType")
            .fileSize(42L)
            .build();
        LeaveCommand.AttachmentRequest dto2 = LeaveCommand.AttachmentRequest.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileType("test-fileType")
            .fileSize(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.AttachmentRequest dto = LeaveCommand.AttachmentRequest.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileType("test-fileType")
            .fileSize(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}