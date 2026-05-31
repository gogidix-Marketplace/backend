package com.gogidix.hr.payroll.interfaces.rest;

import com.gogidix.hr.payroll.interfaces.rest.PayrollController;
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
class PayrollController_CreatePayrollRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        PayrollController.CreatePayrollRequestDto dto = new PayrollController.CreatePayrollRequestDto();
        dto.setCountryCode("val-countryCode");
        dto.setPayrollName("val-payrollName");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setFrequency("val-frequency");
        dto.setCurrency("val-currency");
        dto.setRunType("val-runType");
        dto.setNotes("val-notes");
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-payrollName", dto.getPayrollName());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertEquals("val-frequency", dto.getFrequency());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-runType", dto.getRunType());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollController.CreatePayrollRequestDto dto1 = new PayrollController.CreatePayrollRequestDto();
        PayrollController.CreatePayrollRequestDto dto2 = new PayrollController.CreatePayrollRequestDto();
        dto1.setCountryCode("test");
        dto1.setPayrollName("test");
        dto1.setPayrollPeriod(null);
        dto1.setStartDate(LocalDate.of(2025,1,1));
        dto1.setEndDate(LocalDate.of(2025,1,1));
        dto1.setPaymentDate(LocalDate.of(2025,1,1));
        dto1.setFrequency("test");
        dto1.setCurrency("test");
        dto1.setRunType("test");
        dto1.setEmployeeIds(Collections.emptyList());
        dto1.setNotes("test");
        dto2.setCountryCode("test");
        dto2.setPayrollName("test");
        dto2.setPayrollPeriod(null);
        dto2.setStartDate(LocalDate.of(2025,1,1));
        dto2.setEndDate(LocalDate.of(2025,1,1));
        dto2.setPaymentDate(LocalDate.of(2025,1,1));
        dto2.setFrequency("test");
        dto2.setCurrency("test");
        dto2.setRunType("test");
        dto2.setEmployeeIds(Collections.emptyList());
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCountryCode(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PayrollController.CreatePayrollRequestDto dto = new PayrollController.CreatePayrollRequestDto();
        dto.setCountryCode("test");
        dto.setPayrollName("test");
        dto.setPayrollPeriod(null);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPaymentDate(LocalDate.of(2025,1,1));
        dto.setFrequency("test");
        dto.setCurrency("test");
        dto.setRunType("test");
        dto.setEmployeeIds(Collections.emptyList());
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PayrollController.CreatePayrollRequestDto dto = new PayrollController.CreatePayrollRequestDto();
        dto.setCountryCode("test");
        dto.setPayrollName("test");
        dto.setPayrollPeriod(null);
        dto.setStartDate(LocalDate.of(2025,1,1));
        dto.setEndDate(LocalDate.of(2025,1,1));
        dto.setPaymentDate(LocalDate.of(2025,1,1));
        dto.setFrequency("test");
        dto.setCurrency("test");
        dto.setRunType("test");
        dto.setEmployeeIds(Collections.emptyList());
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}