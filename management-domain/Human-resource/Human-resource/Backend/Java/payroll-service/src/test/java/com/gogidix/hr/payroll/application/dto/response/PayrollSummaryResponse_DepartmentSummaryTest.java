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
class PayrollSummaryResponse_DepartmentSummaryTest {

        @Test
    void testBuilder() {
        PayrollSummaryResponse.DepartmentSummary dto = PayrollSummaryResponse.DepartmentSummary.builder()
                        .department("test-department")
            .employeeCount(42)
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .averageGrossPay(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-department", dto.getDepartment());
        assertEquals(42, dto.getEmployeeCount());
        assertEquals(BigDecimal.TEN, dto.getTotalGrossPay());
        assertEquals(BigDecimal.TEN, dto.getTotalNetPay());
        assertEquals(BigDecimal.TEN, dto.getAverageGrossPay());
    }

    @Test
    void testSettersAndGetters() {
        PayrollSummaryResponse.DepartmentSummary dto = new PayrollSummaryResponse.DepartmentSummary();
        dto.setDepartment("val-department");
        dto.setEmployeeCount(99);
        dto.setTotalGrossPay(BigDecimal.ONE);
        dto.setTotalNetPay(BigDecimal.ONE);
        dto.setAverageGrossPay(BigDecimal.ONE);
        assertEquals("val-department", dto.getDepartment());
        assertEquals(99, dto.getEmployeeCount());
        assertEquals(BigDecimal.ONE, dto.getTotalGrossPay());
        assertEquals(BigDecimal.ONE, dto.getTotalNetPay());
        assertEquals(BigDecimal.ONE, dto.getAverageGrossPay());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollSummaryResponse.DepartmentSummary dto1 = PayrollSummaryResponse.DepartmentSummary.builder()
                        .department("test-department")
            .employeeCount(42)
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .averageGrossPay(BigDecimal.TEN)
            .build();
        PayrollSummaryResponse.DepartmentSummary dto2 = PayrollSummaryResponse.DepartmentSummary.builder()
                        .department("test-department")
            .employeeCount(42)
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .averageGrossPay(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        PayrollSummaryResponse.DepartmentSummary dto = PayrollSummaryResponse.DepartmentSummary.builder()
                        .department("test-department")
            .employeeCount(42)
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .averageGrossPay(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}