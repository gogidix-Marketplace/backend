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
class PayrollController_UpdatePayrollRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        PayrollController.UpdatePayrollRequestDto dto = new PayrollController.UpdatePayrollRequestDto();
        dto.setPayrollName("val-payrollName");
        dto.setPaymentDate(LocalDate.of(2025,6,1));
        dto.setNotes("val-notes");
        assertEquals("val-payrollName", dto.getPayrollName());
        assertEquals(LocalDate.of(2025,6,1), dto.getPaymentDate());
        assertEquals("val-notes", dto.getNotes());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollController.UpdatePayrollRequestDto dto1 = new PayrollController.UpdatePayrollRequestDto();
        PayrollController.UpdatePayrollRequestDto dto2 = new PayrollController.UpdatePayrollRequestDto();
        dto1.setPayrollName("test");
        dto1.setPaymentDate(LocalDate.of(2025,1,1));
        dto1.setNotes("test");
        dto2.setPayrollName("test");
        dto2.setPaymentDate(LocalDate.of(2025,1,1));
        dto2.setNotes("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setPayrollName(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PayrollController.UpdatePayrollRequestDto dto = new PayrollController.UpdatePayrollRequestDto();
        dto.setPayrollName("test");
        dto.setPaymentDate(LocalDate.of(2025,1,1));
        dto.setNotes("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PayrollController.UpdatePayrollRequestDto dto = new PayrollController.UpdatePayrollRequestDto();
        dto.setPayrollName("test");
        dto.setPaymentDate(LocalDate.of(2025,1,1));
        dto.setNotes("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}