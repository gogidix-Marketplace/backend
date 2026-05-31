package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.interfaces.rest.TaxCalculationController;
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
class TaxCalculationController_ReverseRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationController.ReverseRequestDto dto = new TaxCalculationController.ReverseRequestDto();
        dto.setReason("val-reason");
        assertEquals("val-reason", dto.getReason());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationController.ReverseRequestDto dto1 = new TaxCalculationController.ReverseRequestDto();
        TaxCalculationController.ReverseRequestDto dto2 = new TaxCalculationController.ReverseRequestDto();
        dto1.setReason("test");
        dto2.setReason("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setReason(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationController.ReverseRequestDto dto = new TaxCalculationController.ReverseRequestDto();
        dto.setReason("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationController.ReverseRequestDto dto = new TaxCalculationController.ReverseRequestDto();
        dto.setReason("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}