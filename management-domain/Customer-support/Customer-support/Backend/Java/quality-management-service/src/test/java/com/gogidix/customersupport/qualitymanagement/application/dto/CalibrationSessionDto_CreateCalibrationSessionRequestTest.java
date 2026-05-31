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
class CalibrationSessionDto_CreateCalibrationSessionRequestTest {

        @Test
    void testBuilder() {
        CalibrationSessionDto.CreateCalibrationSessionRequest dto = CalibrationSessionDto.CreateCalibrationSessionRequest.builder()
                        .tenantId("test-tenantId")
            .sessionName("test-sessionName")
            .sessionCode("test-sessionCode")
            .description("test-description")
            .sessionType("test-sessionType")
            .facilitatorId("test-facilitatorId")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participants(Collections.emptyList())
            .minParticipants(42)
            .maxParticipants(42)
            .scorecardTemplateId("test-scorecardTemplateId")
            .targetInteractionsCount(42)
            .location("test-location")
            .isVirtual(true)
            .meetingLink("test-meetingLink")
            .tags(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-sessionName", dto.getSessionName());
        assertEquals("test-sessionCode", dto.getSessionCode());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-sessionType", dto.getSessionType());
        assertEquals("test-facilitatorId", dto.getFacilitatorId());
        assertEquals("test-facilitatorName", dto.getFacilitatorName());
        assertEquals(42, dto.getMinParticipants());
        assertEquals(42, dto.getMaxParticipants());
        assertEquals("test-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals(42, dto.getTargetInteractionsCount());
        assertEquals("test-location", dto.getLocation());
        assertTrue(dto.getIsVirtual());
        assertEquals("test-meetingLink", dto.getMeetingLink());
    }

    @Test
    void testSettersAndGetters() {
        CalibrationSessionDto.CreateCalibrationSessionRequest dto = new CalibrationSessionDto.CreateCalibrationSessionRequest();
        dto.setTenantId("val-tenantId");
        dto.setSessionName("val-sessionName");
        dto.setSessionCode("val-sessionCode");
        dto.setDescription("val-description");
        dto.setSessionType("val-sessionType");
        dto.setFacilitatorId("val-facilitatorId");
        dto.setFacilitatorName("val-facilitatorName");
        dto.setMinParticipants(99);
        dto.setMaxParticipants(99);
        dto.setScorecardTemplateId("val-scorecardTemplateId");
        dto.setTargetInteractionsCount(99);
        dto.setLocation("val-location");
        dto.setIsVirtual(true);
        dto.setMeetingLink("val-meetingLink");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-sessionName", dto.getSessionName());
        assertEquals("val-sessionCode", dto.getSessionCode());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-sessionType", dto.getSessionType());
        assertEquals("val-facilitatorId", dto.getFacilitatorId());
        assertEquals("val-facilitatorName", dto.getFacilitatorName());
        assertEquals(99, dto.getMinParticipants());
        assertEquals(99, dto.getMaxParticipants());
        assertEquals("val-scorecardTemplateId", dto.getScorecardTemplateId());
        assertEquals(99, dto.getTargetInteractionsCount());
        assertEquals("val-location", dto.getLocation());
        assertTrue(dto.getIsVirtual());
        assertEquals("val-meetingLink", dto.getMeetingLink());
    }

    @Test
    void testEqualsAndHashCode() {
        CalibrationSessionDto.CreateCalibrationSessionRequest dto1 = CalibrationSessionDto.CreateCalibrationSessionRequest.builder()
                        .tenantId("test-tenantId")
            .sessionName("test-sessionName")
            .sessionCode("test-sessionCode")
            .description("test-description")
            .sessionType("test-sessionType")
            .facilitatorId("test-facilitatorId")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participants(Collections.emptyList())
            .minParticipants(42)
            .maxParticipants(42)
            .scorecardTemplateId("test-scorecardTemplateId")
            .targetInteractionsCount(42)
            .location("test-location")
            .isVirtual(true)
            .meetingLink("test-meetingLink")
            .tags(Collections.emptyList())
            .build();
        CalibrationSessionDto.CreateCalibrationSessionRequest dto2 = CalibrationSessionDto.CreateCalibrationSessionRequest.builder()
                        .tenantId("test-tenantId")
            .sessionName("test-sessionName")
            .sessionCode("test-sessionCode")
            .description("test-description")
            .sessionType("test-sessionType")
            .facilitatorId("test-facilitatorId")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participants(Collections.emptyList())
            .minParticipants(42)
            .maxParticipants(42)
            .scorecardTemplateId("test-scorecardTemplateId")
            .targetInteractionsCount(42)
            .location("test-location")
            .isVirtual(true)
            .meetingLink("test-meetingLink")
            .tags(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CalibrationSessionDto.CreateCalibrationSessionRequest dto = CalibrationSessionDto.CreateCalibrationSessionRequest.builder()
                        .tenantId("test-tenantId")
            .sessionName("test-sessionName")
            .sessionCode("test-sessionCode")
            .description("test-description")
            .sessionType("test-sessionType")
            .facilitatorId("test-facilitatorId")
            .facilitatorName("test-facilitatorName")
            .scheduledDate(Instant.parse("2025-01-15T10:00:00Z"))
            .scheduledEndDate(Instant.parse("2025-01-15T10:00:00Z"))
            .participants(Collections.emptyList())
            .minParticipants(42)
            .maxParticipants(42)
            .scorecardTemplateId("test-scorecardTemplateId")
            .targetInteractionsCount(42)
            .location("test-location")
            .isVirtual(true)
            .meetingLink("test-meetingLink")
            .tags(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}