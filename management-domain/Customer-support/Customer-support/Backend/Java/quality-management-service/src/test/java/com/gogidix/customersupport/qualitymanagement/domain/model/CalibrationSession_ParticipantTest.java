package com.gogidix.customersupport.qualitymanagement.domain.model;

import com.gogidix.customersupport.qualitymanagement.domain.model.CalibrationSession;
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
class CalibrationSession_ParticipantTest {

        @Test
    void testBuilder() {
        CalibrationSession.Participant dto = CalibrationSession.Participant.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantEmail("test-participantEmail")
            .role(CalibrationSession.Participant.ParticipantRole.FACILITATOR)
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
        assertEquals(CalibrationSession.Participant.ParticipantRole.FACILITATOR, dto.getRole());
        assertEquals("test-teamId", dto.getTeamId());
        assertEquals("test-teamName", dto.getTeamName());
        assertTrue(dto.getHasAttended());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSession.Participant dto = new CalibrationSession.Participant();
        dto.setParticipantId("val-participantId");
        dto.setParticipantName("val-participantName");
        dto.setParticipantEmail("val-participantEmail");
        dto.setRole(CalibrationSession.Participant.ParticipantRole.FACILITATOR);
        dto.setTeamId("val-teamId");
        dto.setTeamName("val-teamName");
        dto.setHasAttended(true);
        assertEquals("val-participantId", dto.getParticipantId());
        assertEquals("val-participantName", dto.getParticipantName());
        assertEquals("val-participantEmail", dto.getParticipantEmail());
        assertEquals(CalibrationSession.Participant.ParticipantRole.FACILITATOR, dto.getRole());
        assertEquals("val-teamId", dto.getTeamId());
        assertEquals("val-teamName", dto.getTeamName());
        assertTrue(dto.getHasAttended());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSession.Participant dto1 = CalibrationSession.Participant.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantEmail("test-participantEmail")
            .role(CalibrationSession.Participant.ParticipantRole.FACILITATOR)
            .teamId("test-teamId")
            .teamName("test-teamName")
            .hasAttended(true)
            .joinedAt(Instant.parse("2025-01-15T10:00:00Z"))
            .leftAt(Instant.parse("2025-01-15T10:00:00Z"))
            .complianceScore(null)
            .build();
        CalibrationSession.Participant dto2 = CalibrationSession.Participant.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantEmail("test-participantEmail")
            .role(CalibrationSession.Participant.ParticipantRole.FACILITATOR)
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
        CalibrationSession.Participant dto = CalibrationSession.Participant.builder()
                        .participantId("test-participantId")
            .participantName("test-participantName")
            .participantEmail("test-participantEmail")
            .role(CalibrationSession.Participant.ParticipantRole.FACILITATOR)
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