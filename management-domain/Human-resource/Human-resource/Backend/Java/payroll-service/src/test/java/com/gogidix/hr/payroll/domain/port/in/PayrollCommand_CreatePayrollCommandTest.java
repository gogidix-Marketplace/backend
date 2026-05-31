package com.gogidix.hr.payroll.domain.port.in;

import com.gogidix.hr.payroll.domain.port.in.PayrollCommand;
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
class PayrollCommand_CreatePayrollCommandTest {

        @Test
    void testBuilder() {
        PayrollCommand.CreatePayrollCommand dto = PayrollCommand.CreatePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .payrollName("test-payrollName")
            .payrollPeriod(null)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .paymentDate(LocalDate.of(2025,1,15))
            .frequency("test-frequency")
            .currency("test-currency")
            .runType("test-runType")
            .employeeIds(Collections.emptyList())
            .notes("test-notes")
            .createdBy("test-createdBy")
            .build();
        assertNotNull(dto);
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-payrollName", dto.getPayrollName());
        assertEquals(LocalDate.of(2025,1,15), dto.getStartDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getEndDate());
        assertEquals(LocalDate.of(2025,1,15), dto.getPaymentDate());
        assertEquals("test-frequency", dto.getFrequency());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-runType", dto.getRunType());
        assertEquals("test-notes", dto.getNotes());
        assertEquals("test-createdBy", dto.getCreatedBy());
    }

    @Test
    void testSettersAndGetters() {
        PayrollCommand.CreatePayrollCommand dto = new PayrollCommand.CreatePayrollCommand();
        dto.setTenantId("val-tenantId");
        dto.setCountryCode("val-countryCode");
        dto.setPayrollName("val-payrollName");
        dto.setStartDate(LocalDate.of(2025,6,1));
        dto.setEndDate(LocalDate.of(2025,6,1));
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setFrequency("val-frequency");
        dto.setCurrency("val-currency");
        dto.setRunType("val-runType");
        dto.setNotes("val-notes");
        dto.setCreatedBy("val-createdBy");
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-payrollName", dto.getPayrollName());
        assertEquals(LocalDate.of(2025,6,1), dto.getStartDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getEndDate());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertEquals("val-frequency", dto.getFrequency());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-runType", dto.getRunType());
        assertEquals("val-notes", dto.getNotes());
        assertEquals("val-createdBy", dto.getCreatedBy());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollCommand.CreatePayrollCommand dto1 = PayrollCommand.CreatePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .payrollName("test-payrollName")
            .payrollPeriod(null)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .paymentDate(LocalDate.of(2025,1,15))
            .frequency("test-frequency")
            .currency("test-currency")
            .runType("test-runType")
            .employeeIds(Collections.emptyList())
            .notes("test-notes")
            .createdBy("test-createdBy")
            .build();
        PayrollCommand.CreatePayrollCommand dto2 = PayrollCommand.CreatePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .payrollName("test-payrollName")
            .payrollPeriod(null)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .paymentDate(LocalDate.of(2025,1,15))
            .frequency("test-frequency")
            .currency("test-currency")
            .runType("test-runType")
            .employeeIds(Collections.emptyList())
            .notes("test-notes")
            .createdBy("test-createdBy")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollCommand.CreatePayrollCommand dto = PayrollCommand.CreatePayrollCommand.builder()
                        .tenantId("test-tenantId")
            .countryCode("test-countryCode")
            .payrollName("test-payrollName")
            .payrollPeriod(null)
            .startDate(LocalDate.of(2025,1,15))
            .endDate(LocalDate.of(2025,1,15))
            .paymentDate(LocalDate.of(2025,1,15))
            .frequency("test-frequency")
            .currency("test-currency")
            .runType("test-runType")
            .employeeIds(Collections.emptyList())
            .notes("test-notes")
            .createdBy("test-createdBy")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}