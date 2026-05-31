package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.application.dto.response.MessageResponseDto;
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
class MessageResponseDto_AttachmentDtoTest {

        @Test
    void testBuilder() {
        MessageResponseDto.AttachmentDto dto = MessageResponseDto.AttachmentDto.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .build();
        assertNotNull(dto);
        assertEquals("test-attachmentId", dto.getAttachmentId());
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileType", dto.getFileType());
        assertEquals(42L, dto.getFileSize());
        assertEquals("test-fileUrl", dto.getFileUrl());
    }

    @Test
    void testSettersAndGetters() {
        MessageResponseDto.AttachmentDto dto = new MessageResponseDto.AttachmentDto();
        dto.setAttachmentId("val-attachmentId");
        dto.setFileName("val-fileName");
        dto.setFileType("val-fileType");
        dto.setFileUrl("val-fileUrl");
        assertEquals("val-attachmentId", dto.getAttachmentId());
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileType", dto.getFileType());
        assertEquals("val-fileUrl", dto.getFileUrl());
    }

    @Test
    void testEqualsAndHashCode() {
        MessageResponseDto.AttachmentDto dto1 = MessageResponseDto.AttachmentDto.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .build();
        MessageResponseDto.AttachmentDto dto2 = MessageResponseDto.AttachmentDto.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        MessageResponseDto.AttachmentDto dto = MessageResponseDto.AttachmentDto.builder()
                        .attachmentId("test-attachmentId")
            .fileName("test-fileName")
            .fileType("test-fileType")
            .fileSize(42L)
            .fileUrl("test-fileUrl")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}