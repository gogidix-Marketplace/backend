package com.gogidix.hr.payroll.interfaces.rest;

import com.gogidix.hr.payroll.interfaces.rest.PayrollEntryController;
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
class PayrollEntryController_BatchCreateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        PayrollEntryController.BatchCreateRequestDto dto = new PayrollEntryController.BatchCreateRequestDto();


    }

    @Test
    void testEqualsAndHashCode() {
        PayrollEntryController.BatchCreateRequestDto dto1 = new PayrollEntryController.BatchCreateRequestDto();
        PayrollEntryController.BatchCreateRequestDto dto2 = new PayrollEntryController.BatchCreateRequestDto();
        dto1.setEntries(Collections.emptyList());
        dto2.setEntries(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setEntries(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        PayrollEntryController.BatchCreateRequestDto dto = new PayrollEntryController.BatchCreateRequestDto();
        dto.setEntries(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        PayrollEntryController.BatchCreateRequestDto dto = new PayrollEntryController.BatchCreateRequestDto();
        dto.setEntries(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}