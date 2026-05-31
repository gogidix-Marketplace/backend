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
class ChatMessageResponseDto_MessageMetadataDtoTest {

        @Test
    void testBuilder() {
        ChatMessageResponseDto.MessageMetadataDto dto = ChatMessageResponseDto.MessageMetadataDto.builder()
                        .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .browser("test-browser")
            .os("test-os")
            .location("test-location")
            .build();
        assertNotNull(dto);
        assertEquals("test-ipAddress", dto.getIpAddress());
        assertEquals("test-userAgent", dto.getUserAgent());
        assertEquals("test-browser", dto.getBrowser());
        assertEquals("test-os", dto.getOs());
        assertEquals("test-location", dto.getLocation());
    }

    @Test
    void testSettersAndGetters() {
        ChatMessageResponseDto.MessageMetadataDto dto = new ChatMessageResponseDto.MessageMetadataDto();
        dto.setIpAddress("val-ipAddress");
        dto.setUserAgent("val-userAgent");
        dto.setBrowser("val-browser");
        dto.setOs("val-os");
        dto.setLocation("val-location");
        assertEquals("val-ipAddress", dto.getIpAddress());
        assertEquals("val-userAgent", dto.getUserAgent());
        assertEquals("val-browser", dto.getBrowser());
        assertEquals("val-os", dto.getOs());
        assertEquals("val-location", dto.getLocation());
    }

    @Test
    void testEqualsAndHashCode() {
        ChatMessageResponseDto.MessageMetadataDto dto1 = ChatMessageResponseDto.MessageMetadataDto.builder()
                        .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .browser("test-browser")
            .os("test-os")
            .location("test-location")
            .build();
        ChatMessageResponseDto.MessageMetadataDto dto2 = ChatMessageResponseDto.MessageMetadataDto.builder()
                        .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .browser("test-browser")
            .os("test-os")
            .location("test-location")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ChatMessageResponseDto.MessageMetadataDto dto = ChatMessageResponseDto.MessageMetadataDto.builder()
                        .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .browser("test-browser")
            .os("test-os")
            .location("test-location")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}