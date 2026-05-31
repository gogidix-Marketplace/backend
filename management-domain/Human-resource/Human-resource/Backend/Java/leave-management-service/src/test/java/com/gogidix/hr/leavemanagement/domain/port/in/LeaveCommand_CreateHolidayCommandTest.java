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
class LeaveCommand_CreateHolidayCommandTest {

        @Test
    void testBuilder() {
        LeaveCommand.CreateHolidayCommand dto = LeaveCommand.CreateHolidayCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .stateCode("test-stateCode")
            .holidayName("test-holidayName")
            .holidayDate(LocalDate.of(2025,1,15))
            .holidayType("test-holidayType")
            .isRecurring(true)
            .isPaid(true)
            .description("test-description")
            .createdBy("test-createdBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-stateCode", dto.getStateCode());
        assertEquals("test-holidayName", dto.getHolidayName());
        assertEquals(LocalDate.of(2025,1,15), dto.getHolidayDate());
        assertEquals("test-holidayType", dto.getHolidayType());
        assertTrue(dto.getIsRecurring());
        assertTrue(dto.getIsPaid());
        assertEquals("test-description", dto.getDescription());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        LeaveCommand.CreateHolidayCommand dto = new LeaveCommand.CreateHolidayCommand();
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setStateCode("val-stateCode");
        dto.setHolidayName("val-holidayName");
        dto.setHolidayDate(LocalDate.of(2025,6,1));
        dto.setHolidayType("val-holidayType");
        dto.setIsRecurring(true);
        dto.setIsPaid(true);
        dto.setDescription("val-description");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-stateCode", dto.getStateCode());
        assertEquals("val-holidayName", dto.getHolidayName());
        assertEquals(LocalDate.of(2025,6,1), dto.getHolidayDate());
        assertEquals("val-holidayType", dto.getHolidayType());
        assertTrue(dto.getIsRecurring());
        assertTrue(dto.getIsPaid());
        assertEquals("val-description", dto.getDescription());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        LeaveCommand.CreateHolidayCommand dto1 = LeaveCommand.CreateHolidayCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .stateCode("test-stateCode")
            .holidayName("test-holidayName")
            .holidayDate(LocalDate.of(2025,1,15))
            .holidayType("test-holidayType")
            .isRecurring(true)
            .isPaid(true)
            .description("test-description")
            .createdBy("test-createdBy")
            .build();
        LeaveCommand.CreateHolidayCommand dto2 = LeaveCommand.CreateHolidayCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .stateCode("test-stateCode")
            .holidayName("test-holidayName")
            .holidayDate(LocalDate.of(2025,1,15))
            .holidayType("test-holidayType")
            .isRecurring(true)
            .isPaid(true)
            .description("test-description")
            .createdBy("test-createdBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        LeaveCommand.CreateHolidayCommand dto = LeaveCommand.CreateHolidayCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .stateCode("test-stateCode")
            .holidayName("test-holidayName")
            .holidayDate(LocalDate.of(2025,1,15))
            .holidayType("test-holidayType")
            .isRecurring(true)
            .isPaid(true)
            .description("test-description")
            .createdBy("test-createdBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}