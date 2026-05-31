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
class CommunicationChannelResponseDto_RetryPolicyDtoTest {

        @Test
    void testBuilder() {
        CommunicationChannelResponseDto.RetryPolicyDto dto = CommunicationChannelResponseDto.RetryPolicyDto.builder()
                        .isEnabled(true)
            .maxRetries(42)
            .retryIntervalSeconds(42)
            .build();
        assertNotNull(dto);
        assertTrue(dto.getIsEnabled());
        assertEquals(42, dto.getMaxRetries());
        assertEquals(42, dto.getRetryIntervalSeconds());
    }

    @Test
    void testSettersAndGetters() {
        CommunicationChannelResponseDto.RetryPolicyDto dto = new CommunicationChannelResponseDto.RetryPolicyDto();
        dto.setIsEnabled(true);
        dto.setMaxRetries(99);
        dto.setRetryIntervalSeconds(99);
        assertTrue(dto.getIsEnabled());
        assertEquals(99, dto.getMaxRetries());
        assertEquals(99, dto.getRetryIntervalSeconds());
    }

    @Test
    void testEqualsAndHashCode() {
        CommunicationChannelResponseDto.RetryPolicyDto dto1 = CommunicationChannelResponseDto.RetryPolicyDto.builder()
                        .isEnabled(true)
            .maxRetries(42)
            .retryIntervalSeconds(42)
            .build();
        CommunicationChannelResponseDto.RetryPolicyDto dto2 = CommunicationChannelResponseDto.RetryPolicyDto.builder()
                        .isEnabled(true)
            .maxRetries(42)
            .retryIntervalSeconds(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CommunicationChannelResponseDto.RetryPolicyDto dto = CommunicationChannelResponseDto.RetryPolicyDto.builder()
                        .isEnabled(true)
            .maxRetries(42)
            .retryIntervalSeconds(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}