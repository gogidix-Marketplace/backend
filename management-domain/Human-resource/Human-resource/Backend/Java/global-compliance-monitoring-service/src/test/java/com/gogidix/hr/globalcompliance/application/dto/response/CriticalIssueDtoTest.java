package com.gogidix.hr.globalcompliance.application.dto.response;

import com.gogidix.hr.globalcompliance.application.dto.response.CriticalIssueDto;
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
class CriticalIssueDtoTest {

        @Test
    void testBuilder() {
        CriticalIssueDto dto = CriticalIssueDto.builder()
                        .issueId("test-issueId")
            .issueNumber("test-issueNumber")
            .title("test-title")
            .severity("test-severity")
            .status("test-status")
            .countryCode("test-countryCode")
            .identifiedDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .daysUntilDue(42L)
            .requiresImmediateAttention(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-issueId", dto.getIssueId());
        assertEquals("test-issueNumber", dto.getIssueNumber());
        assertEquals("test-title", dto.getTitle());
        assertEquals("test-severity", dto.getSeverity());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals(LocalDate.of(2025,1,15), dto.getIdentifiedDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getDueDate());
        assertEquals("test-assignedTo", dto.getAssignedTo());
        assertEquals("test-assignedToName", dto.getAssignedToName());
        assertEquals(42L, dto.getDaysUntilDue());
        assertTrue(dto.getRequiresImmediateAttention());
    }

    @Test
    void testSettersAndGetters() {
        CriticalIssueDto dto = new CriticalIssueDto();
        dto.setIssueId("val-issueId");
        dto.setIssueNumber("val-issueNumber");
        dto.setTitle("val-title");
        dto.setSeverity("val-severity");
        dto.setStatus("val-status");
        dto.setCountryCode("val-countryCode");
        dto.setIdentifiedDate(LocalDate.of(2025,6,1));
        dto.setDueDate(LocalDate.of(2025,6,1));
        dto.setAssignedTo("val-assignedTo");
        dto.setAssignedToName("val-assignedToName");
        dto.setRequiresImmediateAttention(true);
        assertEquals("val-issueId", dto.getIssueId());
        assertEquals("val-issueNumber", dto.getIssueNumber());
        assertEquals("val-title", dto.getTitle());
        assertEquals("val-severity", dto.getSeverity());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals(LocalDate.of(2025,6,1), dto.getIdentifiedDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getDueDate());
        assertEquals("val-assignedTo", dto.getAssignedTo());
        assertEquals("val-assignedToName", dto.getAssignedToName());
        assertTrue(dto.getRequiresImmediateAttention());
    }

    @Test
    void testEqualsAndHashCode() {
        CriticalIssueDto dto1 = CriticalIssueDto.builder()
                        .issueId("test-issueId")
            .issueNumber("test-issueNumber")
            .title("test-title")
            .severity("test-severity")
            .status("test-status")
            .countryCode("test-countryCode")
            .identifiedDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .daysUntilDue(42L)
            .requiresImmediateAttention(true)
            .build();
        CriticalIssueDto dto2 = CriticalIssueDto.builder()
                        .issueId("test-issueId")
            .issueNumber("test-issueNumber")
            .title("test-title")
            .severity("test-severity")
            .status("test-status")
            .countryCode("test-countryCode")
            .identifiedDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .daysUntilDue(42L)
            .requiresImmediateAttention(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CriticalIssueDto dto = CriticalIssueDto.builder()
                        .issueId("test-issueId")
            .issueNumber("test-issueNumber")
            .title("test-title")
            .severity("test-severity")
            .status("test-status")
            .countryCode("test-countryCode")
            .identifiedDate(LocalDate.of(2025,1,15))
            .dueDate(LocalDate.of(2025,1,15))
            .assignedTo("test-assignedTo")
            .assignedToName("test-assignedToName")
            .daysUntilDue(42L)
            .requiresImmediateAttention(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}