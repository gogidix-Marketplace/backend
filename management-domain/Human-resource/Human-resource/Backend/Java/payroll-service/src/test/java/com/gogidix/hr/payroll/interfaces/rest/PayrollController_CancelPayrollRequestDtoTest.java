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
class PayrollController_CancelPayrollRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        PayrollController.CancelPayrollRequestDto dto = new PayrollController.CancelPayrollRequestDto();
        dto.setReason("val-reason");
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        PayrollController.CancelPayrollRequestDto dto1 = new PayrollController.CancelPayrollRequestDto();
        PayrollController.CancelPayrollRequestDto dto2 = new PayrollController.CancelPayrollRequestDto();
        dto1.setReason("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PayrollController.CancelPayrollRequestDto dto = new PayrollController.CancelPayrollRequestDto();
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PayrollController.CancelPayrollRequestDto dto = new PayrollController.CancelPayrollRequestDto();
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}