package com.gogidix.customersupport.livechat.application.dto;

import com.gogidix.customersupport.livechat.application.dto.ChatMessageResponseDto;
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
class ChatMessageResponseDto_MessageAttachmentDtoTest {

        @Test
    void testBuilder() {
        ChatMessageResponseDto.MessageAttachmentDto dto = ChatMessageResponseDto.MessageAttachmentDto.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .contentType("test-contentType")
            .build();
        assertNotNull(dto);
        assertEquals("test-fileName", dto.getFileName());
        assertEquals("test-fileUrl", dto.getFileUrl());
        assertEquals("test-fileSize", dto.getFileSize());
        assertEquals("test-contentType", dto.getContentType());
    }

    @Test
    void testSettersAndGetters() {
        ChatMessageResponseDto.MessageAttachmentDto dto = new ChatMessageResponseDto.MessageAttachmentDto();
        dto.setFileName("val-fileName");
        dto.setFileUrl("val-fileUrl");
        dto.setFileSize("val-fileSize");
        dto.setContentType("val-contentType");
        assertEquals("val-fileName", dto.getFileName());
        assertEquals("val-fileUrl", dto.getFileUrl());
        assertEquals("val-fileSize", dto.getFileSize());
        assertEquals("val-contentType", dto.getContentType());
    }

    @Test
    void testEqualsAndHashCode() {
        ChatMessageResponseDto.MessageAttachmentDto dto1 = ChatMessageResponseDto.MessageAttachmentDto.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .contentType("test-contentType")
            .build();
        ChatMessageResponseDto.MessageAttachmentDto dto2 = ChatMessageResponseDto.MessageAttachmentDto.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .contentType("test-contentType")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ChatMessageResponseDto.MessageAttachmentDto dto = ChatMessageResponseDto.MessageAttachmentDto.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .contentType("test-contentType")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}