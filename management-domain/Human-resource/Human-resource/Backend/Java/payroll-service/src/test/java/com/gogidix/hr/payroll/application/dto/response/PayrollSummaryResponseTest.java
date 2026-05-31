package com.gogidix.hr.payroll.application.dto.response;

import com.gogidix.hr.payroll.application.dto.response.PayrollSummaryResponse;
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
class PayrollSummaryResponseTest {

        @Test
    void testBuilder() {
        PayrollSummaryResponse dto = PayrollSummaryResponse.builder()
                        .payrollId("test-payrollId")
            .payrollPeriod(null)
            .status("test-status")
            .employeeCount(42)
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .currency("test-currency")
            .paymentDate("test-paymentDate")
            .departmentSummaries(Collections.emptyList())
            .averageGrossPay(BigDecimal.TEN)
            .averageNetPay(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-payrollId", dto.getPayrollId());
        assertEquals("test-status", dto.getStatus());
        assertEquals(42, dto.getEmployeeCount());
        assertEquals(BigDecimal.TEN, dto.getTotalGrossPay());
        assertEquals(BigDecimal.TEN, dto.getTotalNetPay());
        assertEquals(BigDecimal.TEN, dto.getTotalTaxes());
        assertEquals(BigDecimal.TEN, dto.getTotalDeductions());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals("test-paymentDate", dto.getPaymentDate());
        assertEquals(BigDecimal.TEN, dto.getAverageGrossPay());
        assertEquals(BigDecimal.TEN, dto.getAverageNetPay());
    }

    @Test
    void testSettersAndGetters() {
        PayrollSummaryResponse dto = new PayrollSummaryResponse();
        dto.setPayrollId("val-payrollId");
        dto.setStatus("val-status");
        dto.setEmployeeCount(99);
        dto.setTotalGrossPay(BigDecimal.ONE);
        dto.setTotalNetPay(BigDecimal.ONE);
        dto.setTotalTaxes(BigDecimal.ONE);
        dto.setTotalDeductions(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setPaymentDate("val-paymentDate");
        dto.setAverageGrossPay(BigDecimal.ONE);
        dto.setAverageNetPay(BigDecimal.ONE);
        assertEquals("val-payrollId", dto.getPayrollId());
        assertEquals("val-status", dto.getStatus());
        assertEquals(99, dto.getEmployeeCount());
        assertEquals(BigDecimal.ONE, dto.getTotalGrossPay());
        assertEquals(BigDecimal.ONE, dto.getTotalNetPay());
        assertEquals(BigDecimal.ONE, dto.getTotalTaxes());
        assertEquals(BigDecimal.ONE, dto.getTotalDeductions());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals("val-paymentDate", dto.getPaymentDate());
        assertEquals(BigDecimal.ONE, dto.getAverageGrossPay());
        assertEquals(BigDecimal.ONE, dto.getAverageNetPay());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollSummaryResponse dto1 = PayrollSummaryResponse.builder()
                        .payrollId("test-payrollId")
            .payrollPeriod(null)
            .status("test-status")
            .employeeCount(42)
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .currency("test-currency")
            .paymentDate("test-paymentDate")
            .departmentSummaries(Collections.emptyList())
            .averageGrossPay(BigDecimal.TEN)
            .averageNetPay(BigDecimal.TEN)
            .build();
        PayrollSummaryResponse dto2 = PayrollSummaryResponse.builder()
                        .payrollId("test-payrollId")
            .payrollPeriod(null)
            .status("test-status")
            .employeeCount(42)
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .currency("test-currency")
            .paymentDate("test-paymentDate")
            .departmentSummaries(Collections.emptyList())
            .averageGrossPay(BigDecimal.TEN)
            .averageNetPay(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollSummaryResponse dto = PayrollSummaryResponse.builder()
                        .payrollId("test-payrollId")
            .payrollPeriod(null)
            .status("test-status")
            .employeeCount(42)
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .currency("test-currency")
            .paymentDate("test-paymentDate")
            .departmentSummaries(Collections.emptyList())
            .averageGrossPay(BigDecimal.TEN)
            .averageNetPay(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}