package com.gogidix.sales.communication.application.dto.response;

import com.gogidix.sales.communication.application.dto.response.ConversationResponseDto;
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
class ConversationResponseDto_ParticipantDetailsDtoTest {

        @Test
    void testBuilder() {
        ConversationResponseDto.ParticipantDetailsDto dto = ConversationResponseDto.ParticipantDetailsDto.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantType("test-participantType")
            .role(null)
            .joinedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isActive(true)
            .lastReadAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertNotNull(dto);
        assertEquals("test-participantId", dto.getParticipantId());
        assertEquals("test-participantName", dto.getParticipantName());
        assertEquals("test-participantType", dto.getParticipantType());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testSettersAndGetters() {
        ConversationResponseDto.ParticipantDetailsDto dto = new ConversationResponseDto.ParticipantDetailsDto();
        dto.setParticipantId("val-participantId");
        dto.setParticipantName("val-participantName");
        dto.setParticipantType("val-participantType");
        dto.setIsActive(true);
        assertEquals("val-participantId", dto.getParticipantId());
        assertEquals("val-participantName", dto.getParticipantName());
        assertEquals("val-participantType", dto.getParticipantType());
        assertTrue(dto.getIsActive());
    }

    @Test
    void testEqualsAndHashCode() {
        ConversationResponseDto.ParticipantDetailsDto dto1 = ConversationResponseDto.ParticipantDetailsDto.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantType("test-participantType")
            .role(null)
            .joinedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isActive(true)
            .lastReadAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        ConversationResponseDto.ParticipantDetailsDto dto2 = ConversationResponseDto.ParticipantDetailsDto.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantType("test-participantType")
            .role(null)
            .joinedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isActive(true)
            .lastReadAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ConversationResponseDto.ParticipantDetailsDto dto = ConversationResponseDto.ParticipantDetailsDto.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantType("test-participantType")
            .role(null)
            .joinedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .isActive(true)
            .lastReadAt(Instant.parse("2025-01-15T10:00:00Z"))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}