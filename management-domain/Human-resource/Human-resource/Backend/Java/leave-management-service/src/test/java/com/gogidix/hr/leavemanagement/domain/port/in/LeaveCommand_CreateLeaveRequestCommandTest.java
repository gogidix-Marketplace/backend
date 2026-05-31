package com.gogidix.hr.leavemanagement.domain.port.in;

import com.gogidix.hr.leavemanagement.domain.enums.LeaveType;
import com.gogidix.hr.leavemanagement.domain.enums.RequestType;
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
class LeaveCommand_CreateLeaveRequestCommandTest {

        @Test
    void testBuilder() {
        LeaveCommand.CreateLeaveRequestCommand dto = LeaveCommand.CreateLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .managerId("test-managerId")
            .leaveType(LeaveType.ANNUAL)
            .requestType(RequestType.FULL_DAY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .requestedDays(null)
            .hoursRequested(null)
            .reason("test-reason")
            .contactDuringLeave("test-contactDuringLeave")
            .emergencyContact("test-emergencyContact")
            .isHalfDay(true)
            .halfDayType("test-halfDayType")
            .attachments(Collections.emptyList())
            .year("test-year")
            .reliefStaffId("test-reliefStaffId")
            .reliefStaffName("test-reliefStaffName")
            .handoverNotes("test-handoverNotes")
            .createdBy("test-createdBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-employeeId", dto.getEmployeeId());
        assertEquals("test-employeeName", dto.getEmployeeName());
        assertEquals("test-employeeCode", dto.getEmployeeCode());
        assertEquals("test-department", dto.getDepartment());
        assertEquals("test-position", dto.getPosition());
        assertEquals("test-managerId", dto.getManagerId());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals("test-reason", dto.getReason());
        assertEquals("test-contactDuringLeave", dto.getContactDuringLeave());
        assertEquals("test-emergencyContact", dto.getEmergencyContact());
        assertTrue(dto.getIsHalfDay());
        assertEquals("test-halfDayType", dto.getHalfDayType());
        assertEquals("test-year", dto.getYear());
        assertEquals("test-reliefStaffId", dto.getReliefStaffId());
        assertEquals("test-reliefStaffName", dto.getReliefStaffName());
        assertEquals("test-handoverNotes", dto.getHandoverNotes());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.CreateLeaveRequestCommand dto = new LeaveCommand.CreateLeaveRequestCommand();
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setEmployeeId("val-employeeId");
        dto.setEmployeeName("val-employeeName");
        dto.setEmployeeCode("val-employeeCode");
        dto.setDepartment("val-department");
        dto.setPosition("val-position");
        dto.setManagerId("val-managerId");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setReason("val-reason");
        dto.setContactDuringLeave("val-contactDuringLeave");
        dto.setEmergencyContact("val-emergencyContact");
        dto.setIsHalfDay(true);
        dto.setHalfDayType("val-halfDayType");
        dto.setYear("val-year");
        dto.setReliefStaffId("val-reliefStaffId");
        dto.setReliefStaffName("val-reliefStaffName");
        dto.setHandoverNotes("val-handoverNotes");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-employeeId", dto.getEmployeeId());
        assertEquals("val-employeeName", dto.getEmployeeName());
        assertEquals("val-employeeCode", dto.getEmployeeCode());
        assertEquals("val-department", dto.getDepartment());
        assertEquals("val-position", dto.getPosition());
        assertEquals("val-managerId", dto.getManagerId());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals("val-reason", dto.getReason());
        assertEquals("val-contactDuringLeave", dto.getContactDuringLeave());
        assertEquals("val-emergencyContact", dto.getEmergencyContact());
        assertTrue(dto.getIsHalfDay());
        assertEquals("val-halfDayType", dto.getHalfDayType());
        assertEquals("val-year", dto.getYear());
        assertEquals("val-reliefStaffId", dto.getReliefStaffId());
        assertEquals("val-reliefStaffName", dto.getReliefStaffName());
        assertEquals("val-handoverNotes", dto.getHandoverNotes());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.CreateLeaveRequestCommand dto1 = LeaveCommand.CreateLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .managerId("test-managerId")
            .leaveType(LeaveType.ANNUAL)
            .requestType(RequestType.FULL_DAY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .requestedDays(null)
            .hoursRequested(null)
            .reason("test-reason")
            .contactDuringLeave("test-contactDuringLeave")
            .emergencyContact("test-emergencyContact")
            .isHalfDay(true)
            .halfDayType("test-halfDayType")
            .attachments(Collections.emptyList())
            .year("test-year")
            .reliefStaffId("test-reliefStaffId")
            .reliefStaffName("test-reliefStaffName")
            .handoverNotes("test-handoverNotes")
            .createdBy("test-createdBy")
            .build();
        LeaveCommand.CreateLeaveRequestCommand dto2 = LeaveCommand.CreateLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .managerId("test-managerId")
            .leaveType(LeaveType.ANNUAL)
            .requestType(RequestType.FULL_DAY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .requestedDays(null)
            .hoursRequested(null)
            .reason("test-reason")
            .contactDuringLeave("test-contactDuringLeave")
            .emergencyContact("test-emergencyContact")
            .isHalfDay(true)
            .halfDayType("test-halfDayType")
            .attachments(Collections.emptyList())
            .year("test-year")
            .reliefStaffId("test-reliefStaffId")
            .reliefStaffName("test-reliefStaffName")
            .handoverNotes("test-handoverNotes")
            .createdBy("test-createdBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.CreateLeaveRequestCommand dto = LeaveCommand.CreateLeaveRequestCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .employeeId("test-employeeId")
            .employeeName("test-employeeName")
            .employeeCode("test-employeeCode")
            .department("test-department")
            .position("test-position")
            .managerId("test-managerId")
            .leaveType(LeaveType.ANNUAL)
            .requestType(RequestType.FULL_DAY)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .requestedDays(null)
            .hoursRequested(null)
            .reason("test-reason")
            .contactDuringLeave("test-contactDuringLeave")
            .emergencyContact("test-emergencyContact")
            .isHalfDay(true)
            .halfDayType("test-halfDayType")
            .attachments(Collections.emptyList())
            .year("test-year")
            .reliefStaffId("test-reliefStaffId")
            .reliefStaffName("test-reliefStaffName")
            .handoverNotes("test-handoverNotes")
            .createdBy("test-createdBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}