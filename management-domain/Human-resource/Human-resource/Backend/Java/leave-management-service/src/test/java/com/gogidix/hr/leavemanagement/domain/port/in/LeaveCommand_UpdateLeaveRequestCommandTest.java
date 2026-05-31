package com.gogidix.hr.leavemanagement.domain.port.in;

import com.gogidix.hr.leavemanagement.domain.port.in.LeaveCommand;
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
class LeaveCommand_UpdateLeaveRequestCommandTest {

        @Test
    void testBuilder() {
        LeaveCommand.UpdateLeaveRequestCommand dto = LeaveCommand.UpdateLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .requestedDays(null)
            .reason("test-reason")
            .contactDuringLeave("test-contactDuringLeave")
            .emergencyContact("test-emergencyContact")
            .attachments(Collections.emptyList())
            .updatedBy("test-updatedBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-requestId", dto.getRequestId());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-contactDuringLeave", dto.getContactDuringLeave());
        assertEquals("test-emergencyContact", dto.getEmergencyContact());
        assertEquals("test-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.UpdateLeaveRequestCommand dto = new LeaveCommand.UpdateLeaveRequestCommand();
        dto.setTenantId("val-tenantId");
        dto.setRequestId("val-requestId");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setReason("val-reason");
        dto.setContactDuringLeave("val-contactDuringLeave");
        dto.setEmergencyContact("val-emergencyContact");
        dto.setUpdatedBy("val-updatedBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-requestId", dto.getRequestId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-contactDuringLeave", dto.getContactDuringLeave());
        assertEquals("val-emergencyContact", dto.getEmergencyContact());
        assertEquals("val-updatedBy", dto.getUpdatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.UpdateLeaveRequestCommand dto1 = LeaveCommand.UpdateLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .requestedDays(null)
            .reason("test-reason")
            .contactDuringLeave("test-contactDuringLeave")
            .emergencyContact("test-emergencyContact")
            .attachments(Collections.emptyList())
            .updatedBy("test-updatedBy")
            .build();
        LeaveCommand.UpdateLeaveRequestCommand dto2 = LeaveCommand.UpdateLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .requestedDays(null)
            .reason("test-reason")
            .contactDuringLeave("test-contactDuringLeave")
            .emergencyContact("test-emergencyContact")
            .attachments(Collections.emptyList())
            .updatedBy("test-updatedBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.UpdateLeaveRequestCommand dto = LeaveCommand.UpdateLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .requestId("test-requestId")
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .requestedDays(null)
            .reason("test-reason")
            .contactDuringLeave("test-contactDuringLeave")
            .emergencyContact("test-emergencyContact")
            .attachments(Collections.emptyList())
            .updatedBy("test-updatedBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}