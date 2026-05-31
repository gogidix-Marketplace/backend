package com.gogidix.customersupport.livechat.application.dto;

import com.gogidix.customersupport.livechat.application.dto.ChatSessionRequestDto;
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
class ChatSessionRequestDtoTest {

        @Test
    void testBuilder() {
        ChatSessionRequestDto dto = ChatSessionRequestDto.builder()
                        .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .channel("test-channel")
            .tags(Collections.emptyList())
            .category("test-category")
            .relatedTicketId("test-relatedTicketId")
            .preChatSurveyData(null)
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .referrer("test-referrer")
            .location("test-location")
            .build();
        assertNotNull(dto);
        assertEquals("test-customerId", dto.getCustomerId());
        assertEquals("test-customerName", dto.getCustomerName());
        assertEquals("test-customerEmail", dto.getCustomerEmail());
        assertEquals("test-channel", dto.getChannel());
        assertEquals("test-category", dto.getCategory());
        assertEquals("test-relatedTicketId", dto.getRelatedTicketId());
        assertEquals("test-ipAddress", dto.getIpAddress());
        assertEquals("test-userAgent", dto.getUserAgent());
        assertEquals("test-referrer", dto.getReferrer());
        assertEquals("test-location", dto.getLocation());
    }

    @Test
    void testSettersAndGetters() {
        ChatSessionRequestDto dto = new ChatSessionRequestDto();
        dto.setCustomerId("val-customerId");
        dto.setCustomerName("val-customerName");
        dto.setCustomerEmail("val-customerEmail");
        dto.setChannel("val-channel");
        dto.setCategory("val-category");
        dto.setRelatedTicketId("val-relatedTicketId");
        dto.setIpAddress("val-ipAddress");
        dto.setUserAgent("val-userAgent");
        dto.setReferrer("val-referrer");
        dto.setLocation("val-location");
        assertEquals("val-customerId", dto.getCustomerId());
        assertEquals("val-customerName", dto.getCustomerName());
        assertEquals("val-customerEmail", dto.getCustomerEmail());
        assertEquals("val-channel", dto.getChannel());
        assertEquals("val-category", dto.getCategory());
        assertEquals("val-relatedTicketId", dto.getRelatedTicketId());
        assertEquals("val-ipAddress", dto.getIpAddress());
        assertEquals("val-userAgent", dto.getUserAgent());
        assertEquals("val-referrer", dto.getReferrer());
        assertEquals("val-location", dto.getLocation());
    }

    @Test
    void testEqualsAndHashCode() {
        ChatSessionRequestDto dto1 = ChatSessionRequestDto.builder()
                        .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .channel("test-channel")
            .tags(Collections.emptyList())
            .category("test-category")
            .relatedTicketId("test-relatedTicketId")
            .preChatSurveyData(null)
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .referrer("test-referrer")
            .location("test-location")
            .build();
        ChatSessionRequestDto dto2 = ChatSessionRequestDto.builder()
                        .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .channel("test-channel")
            .tags(Collections.emptyList())
            .category("test-category")
            .relatedTicketId("test-relatedTicketId")
            .preChatSurveyData(null)
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .referrer("test-referrer")
            .location("test-location")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ChatSessionRequestDto dto = ChatSessionRequestDto.builder()
                        .customerId("test-customerId")
            .customerName("test-customerName")
            .customerEmail("test-customerEmail")
            .channel("test-channel")
            .tags(Collections.emptyList())
            .category("test-category")
            .relatedTicketId("test-relatedTicketId")
            .preChatSurveyData(null)
            .ipAddress("test-ipAddress")
            .userAgent("test-userAgent")
            .referrer("test-referrer")
            .location("test-location")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}