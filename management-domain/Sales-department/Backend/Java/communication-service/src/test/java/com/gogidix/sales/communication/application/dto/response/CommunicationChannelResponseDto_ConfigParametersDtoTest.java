package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.application.dto.response.CommunicationChannelResponseDto;
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
class CommunicationChannelResponseDto_ConfigParametersDtoTest {

        @Test
    void testBuilder() {
        CommunicationChannelResponseDto.ConfigParametersDto dto = CommunicationChannelResponseDto.ConfigParametersDto.builder()
                        .smtpHost("test-smtpHost")
            .smtpPort(42)
            .fromEmail("test-fromEmail")
            .fromName("test-fromName")
            .replyToEmail("test-replyToEmail")
            .provider("test-provider")
            .senderId("test-senderId")
            .businessAccountId("test-businessAccountId")
            .phoneNumberId("test-phoneNumberId")
            .build();
        assertNotNull(dto);
        assertEquals("test-smtpHost", dto.getSmtpHost());
        assertEquals(42, dto.getSmtpPort());
        assertEquals("test-fromEmail", dto.getFromEmail());
        assertEquals("test-fromName", dto.getFromName());
        assertEquals("test-replyToEmail", dto.getReplyToEmail());
        assertEquals("test-provider", dto.getProvider());
        assertEquals("test-senderId", dto.getSenderId());
        assertEquals("test-businessAccountId", dto.getBusinessAccountId());
        assertEquals("test-phoneNumberId", dto.getPhoneNumberId());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannelResponseDto.ConfigParametersDto dto = new CommunicationChannelResponseDto.ConfigParametersDto();
        dto.setSmtpHost("val-smtpHost");
        dto.setSmtpPort(99);
        dto.setFromEmail("val-fromEmail");
        dto.setFromName("val-fromName");
        dto.setReplyToEmail("val-replyToEmail");
        dto.setProvider("val-provider");
        dto.setSenderId("val-senderId");
        dto.setBusinessAccountId("val-businessAccountId");
        dto.setPhoneNumberId("val-phoneNumberId");
        assertEquals("val-smtpHost", dto.getSmtpHost());
        assertEquals(99, dto.getSmtpPort());
        assertEquals("val-fromEmail", dto.getFromEmail());
        assertEquals("val-fromName", dto.getFromName());
        assertEquals("val-replyToEmail", dto.getReplyToEmail());
        assertEquals("val-provider", dto.getProvider());
        assertEquals("val-senderId", dto.getSenderId());
        assertEquals("val-businessAccountId", dto.getBusinessAccountId());
        assertEquals("val-phoneNumberId", dto.getPhoneNumberId());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannelResponseDto.ConfigParametersDto dto1 = CommunicationChannelResponseDto.ConfigParametersDto.builder()
                        .smtpHost("test-smtpHost")
            .smtpPort(42)
            .fromEmail("test-fromEmail")
            .fromName("test-fromName")
            .replyToEmail("test-replyToEmail")
            .provider("test-provider")
            .senderId("test-senderId")
            .businessAccountId("test-businessAccountId")
            .phoneNumberId("test-phoneNumberId")
            .build();
        CommunicationChannelResponseDto.ConfigParametersDto dto2 = CommunicationChannelResponseDto.ConfigParametersDto.builder()
                        .smtpHost("test-smtpHost")
            .smtpPort(42)
            .fromEmail("test-fromEmail")
            .fromName("test-fromName")
            .replyToEmail("test-replyToEmail")
            .provider("test-provider")
            .senderId("test-senderId")
            .businessAccountId("test-businessAccountId")
            .phoneNumberId("test-phoneNumberId")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannelResponseDto.ConfigParametersDto dto = CommunicationChannelResponseDto.ConfigParametersDto.builder()
                        .smtpHost("test-smtpHost")
            .smtpPort(42)
            .fromEmail("test-fromEmail")
            .fromName("test-fromName")
            .replyToEmail("test-replyToEmail")
            .provider("test-provider")
            .senderId("test-senderId")
            .businessAccountId("test-businessAccountId")
            .phoneNumberId("test-phoneNumberId")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}