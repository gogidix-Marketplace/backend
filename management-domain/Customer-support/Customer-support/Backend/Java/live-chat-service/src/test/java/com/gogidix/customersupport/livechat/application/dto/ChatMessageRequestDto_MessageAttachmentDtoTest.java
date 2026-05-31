package com.gogidix.customersupport.livechat.application.dto;

import com.gogidix.customersupport.livechat.application.dto.ChatMessageRequestDto;
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
class ChatMessageRequestDto_MessageAttachmentDtoTest {

        @Test
    void testBuilder() {
        ChatMessageRequestDto.MessageAttachmentDto dto = ChatMessageRequestDto.MessageAttachmentDto.builder()
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
        ChatMessageRequestDto.MessageAttachmentDto dto = new ChatMessageRequestDto.MessageAttachmentDto();
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
        ChatMessageRequestDto.MessageAttachmentDto dto1 = ChatMessageRequestDto.MessageAttachmentDto.builder()
                        .fileName("test-fileName")
            .fileUrl("test-fileUrl")
            .fileSize("test-fileSize")
            .contentType("test-contentType")
            .build();
        ChatMessageRequestDto.MessageAttachmentDto dto2 = ChatMessageRequestDto.MessageAttachmentDto.builder()
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
        ChatMessageRequestDto.MessageAttachmentDto dto = ChatMessageRequestDto.MessageAttachmentDto.builder()
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