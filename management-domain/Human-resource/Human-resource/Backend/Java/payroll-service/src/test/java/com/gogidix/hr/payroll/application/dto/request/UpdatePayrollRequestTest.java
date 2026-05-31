package com.gogidix.hr.payroll.application.dto.request;

import com.gogidix.hr.payroll.application.dto.request.UpdatePayrollRequest;
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
class UpdatePayrollRequestTest {

        @Test
    void testBuilder() {
        UpdatePayrollRequest dto = UpdatePayrollRequest.builder()
                        .payDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertNotNull(dto);
        assertEquals(LocalDate.of(2025,1,15), dto.getPayDate());
        assertEquals("test-status", dto.getStatus());
        assertEquals(BigDecimal.TEN, dto.getTotalGrossPay());
        assertEquals(BigDecimal.TEN, dto.getTotalNetPay());
        assertEquals(BigDecimal.TEN, dto.getTotalDeductions());
        assertEquals(BigDecimal.TEN, dto.getTotalTaxes());
        assertEquals("test-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,1,15), dto.getApprovedDate());
        assertEquals("test-notes", dto.getNotes());
    }

    @Test
    void testSettersAndGetters() {
        UpdatePayrollRequest dto = new UpdatePayrollRequest();
        dto.setPayDate(LocalDate.of(2025,6,1));
        dto.setStatus("val-status");
        dto.setTotalGrossPay(BigDecimal.ONE);
        dto.setTotalNetPay(BigDecimal.ONE);
        dto.setTotalDeductions(BigDecimal.ONE);
        dto.setTotalTaxes(BigDecimal.ONE);
        dto.setApprovedBy("val-approvedBy");
        dto.setApprovedDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        assertEquals(LocalDate.of(2025,6,1), dto.getPayDate());
        assertEquals("val-status", dto.getStatus());
        assertEquals(BigDecimal.ONE, dto.getTotalGrossPay());
        assertEquals(BigDecimal.ONE, dto.getTotalNetPay());
        assertEquals(BigDecimal.ONE, dto.getTotalDeductions());
        assertEquals(BigDecimal.ONE, dto.getTotalTaxes());
        assertEquals("val-approvedBy", dto.getApprovedBy());
        assertEquals(LocalDate.of(2025,6,1), dto.getApprovedDate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        UpdatePayrollRequest dto1 = UpdatePayrollRequest.builder()
                        .payDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        UpdatePayrollRequest dto2 = UpdatePayrollRequest.builder()
                        .payDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        UpdatePayrollRequest dto = UpdatePayrollRequest.builder()
                        .payDate(LocalDate.of(2025,1,15))
            .status("test-status")
            .totalGrossPay(BigDecimal.TEN)
            .totalNetPay(BigDecimal.TEN)
            .totalDeductions(BigDecimal.TEN)
            .totalTaxes(BigDecimal.TEN)
            .approvedBy("test-approvedBy")
            .approvedDate(LocalDate.of(2025,1,15))
            .notes("test-notes")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}