package com.gogidix.hr.payroll.application.dto.response;

import com.gogidix.hr.payroll.application.dto.response.PayrollResponse;
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
class PayrollResponseTest {

        @Test
    void testBuilder() {
        PayrollResponse dto = PayrollResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .payrollCode("test-payrollCode")
            .payrollPeriod(null)
            .payDate(LocalDate.of(2025,1,15))
            .countryCode("test-countryCode")
            .currency("test-currency")
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .employeeCount(42)
            .status("test-status")
            .processedBy("test-processedBy")
            .processedDate(LocalDate.of(2025,1,15))
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-tenantId", dto.getTenantId());
        assertEquals("test-payrollCode", dto.getPayrollCode());
        assertEquals(LocalDate.of(2025,1,15), dto.getPayDate());
        assertEquals("test-countryCode", dto.getCountryCode());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(BigDecimal.TEN, dto.getTotalGrossPay());
        assertEquals(BigDecimal.TEN, dto.getTotalNetPay());
        assertEquals(BigDecimal.TEN, dto.getTotalDeductions());
        assertEquals(BigDecimal.TEN, dto.getTotalTaxes());
        assertEquals(42, dto.getEmployeeCount());
        assertEquals("test-status", dto.getStatus());
        assertEquals("test-processedBy", dto.getProcessedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getProcessedDate());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getApprovedDate());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        PayrollResponse dto = new PayrollResponse();
        dto.setId("val-id");
        dto.setTenantId("val-tenantId");
        dto.setPayrollCode("val-payrollCode");
        dto.setPayDate(LocalDate.of(2025,6,1));
        dto.setCountryCode("val-countryCode");
        dto.setCurrency("val-currency");
        dto.setTotalGrossPay(BigDecimal.ONE);
        dto.setTotalNetPay(BigDecimal.ONE);
        dto.setTotalDeductions(BigDecimal.ONE);
        dto.setTotalTaxes(BigDecimal.ONE);
        dto.setEmployeeCount(99);
        dto.setStatus("val-status");
        dto.setProcessedBy("val-processedBy");
        dto.setProcessedDate(LocalDate.of(2025,6,1));
        dto.setApprovedBy("val-approvedBy");
        dto.setApprovedDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        assertEquals("val-id", dto.getId());
        assertEquals("val-tenantId", dto.getTenantId());
        assertEquals("val-payrollCode", dto.getPayrollCode());
        assertEquals(LocalDate.of(2025,6,1), dto.getPayDate());
        assertEquals("val-countryCode", dto.getCountryCode());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(BigDecimal.ONE, dto.getTotalGrossPay());
        assertEquals(BigDecimal.ONE, dto.getTotalNetPay());
        assertEquals(BigDecimal.ONE, dto.getTotalDeductions());
        assertEquals(BigDecimal.ONE, dto.getTotalTaxes());
        assertEquals(99, dto.getEmployeeCount());
        assertEquals("val-status", dto.getStatus());
        assertEquals("val-processedBy", dto.getProcessedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getProcessedDate());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getApprovedDate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollResponse dto1 = PayrollResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .payrollCode("test-payrollCode")
            .payrollPeriod(null)
            .payDate(LocalDate.of(2025,1,15))
            .countryCode("test-countryCode")
            .currency("test-currency")
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .employeeCount(42)
            .status("test-status")
            .processedBy("test-processedBy")
            .processedDate(LocalDate.of(2025,1,15))
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        PayrollResponse dto2 = PayrollResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .payrollCode("test-payrollCode")
            .payrollPeriod(null)
            .payDate(LocalDate.of(2025,1,15))
            .countryCode("test-countryCode")
            .currency("test-currency")
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .employeeCount(42)
            .status("test-status")
            .processedBy("test-processedBy")
            .processedDate(LocalDate.of(2025,1,15))
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollResponse dto = PayrollResponse.builder()
                        .id("test-id")
            .tenantId("test-tenantId")
            .payrollCode("test-payrollCode")
            .payrollPeriod(null)
            .payDate(LocalDate.of(2025,1,15))
            .countryCode("test-countryCode")
            .currency("test-currency")
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .employeeCount(42)
            .status("test-status")
            .processedBy("test-processedBy")
            .processedDate(LocalDate.of(2025,1,15))
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}