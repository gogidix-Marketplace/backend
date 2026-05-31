package com.gogidix.customersupport.qualitymanagement.application.dto;

import com.gogidix.customersupport.qualitymanagement.application.dto.CalibrationSessionDto;
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
class CalibrationSessionDto_ParticipantDtoTest {

        @Test
    void testBuilder() {
        CalibrationSessionDto.ParticipantDto dto = CalibrationSessionDto.ParticipantDto.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantEmail("test-participantEmail")
            .role("test-role")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .hasAttended(true)
            .joinedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .leftAt(Instant.parse("2025-01-15T10:00:00Z"))
            .complianceScore(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-participantId", dto.getParticipantId());
        assertEquals("test-participantName", dto.getParticipantName());
        assertEquals("test-participantEmail", dto.getParticipantEmail());
        assertEquals("test-role", dto.getRole());
        assertEquals("test-teamId", dto.getTeamId());
        assertEquals("test-teamName", dto.getTeamName());
        assertTrue(dto.getHasAttended());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSessionDto.ParticipantDto dto = new CalibrationSessionDto.ParticipantDto();
        dto.setParticipantId("val-participantId");
        dto.setParticipantName("val-participantName");
        dto.setParticipantEmail("val-participantEmail");
        dto.setRole("val-role");
        dto.setTeamId("val-teamId");
        dto.setTeamName("val-teamName");
        dto.setHasAttended(true);
        assertEquals("val-participantId", dto.getParticipantId());
        assertEquals("val-participantName", dto.getParticipantName());
        assertEquals("val-participantEmail", dto.getParticipantEmail());
        assertEquals("val-role", dto.getRole());
        assertEquals("val-teamId", dto.getTeamId());
        assertEquals("val-teamName", dto.getTeamName());
        assertTrue(dto.getHasAttended());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSessionDto.ParticipantDto dto1 = CalibrationSessionDto.ParticipantDto.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantEmail("test-participantEmail")
            .role("test-role")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .hasAttended(true)
            .joinedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .leftAt(Instant.parse("2025-01-15T10:00:00Z"))
            .complianceScore(null)
            .build();
        CalibrationSessionDto.ParticipantDto dto2 = CalibrationSessionDto.ParticipantDto.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantEmail("test-participantEmail")
            .role("test-role")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .hasAttended(true)
            .joinedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .leftAt(Instant.parse("2025-01-15T10:00:00Z"))
            .complianceScore(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalibrationSessionDto.ParticipantDto dto = CalibrationSessionDto.ParticipantDto.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantEmail("test-participantEmail")
            .role("test-role")
            .teamId("test-teamId")
            .teamName("test-teamName")
            .hasAttended(true)
            .joinedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .leftAt(Instant.parse("2025-01-15T10:00:00Z"))
            .complianceScore(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}