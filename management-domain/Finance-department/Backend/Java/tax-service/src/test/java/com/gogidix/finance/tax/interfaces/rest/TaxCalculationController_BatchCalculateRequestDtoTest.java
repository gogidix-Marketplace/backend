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
class TaxCalculationController_BatchCalculateRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxCalculationController.BatchCalculateRequestDto dto = new TaxCalculationController.BatchCalculateRequestDto();


    }

    @Test
    void testEqualsAndHashCode() {
        TaxCalculationController.BatchCalculateRequestDto dto1 = new TaxCalculationController.BatchCalculateRequestDto();
        TaxCalculationController.BatchCalculateRequestDto dto2 = new TaxCalculationController.BatchCalculateRequestDto();
        dto1.setTransactions(Collections.emptyList());
        dto2.setTransactions(Collections.emptyList());
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setTransactions(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxCalculationController.BatchCalculateRequestDto dto = new TaxCalculationController.BatchCalculateRequestDto();
        dto.setTransactions(Collections.emptyList());
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxCalculationController.BatchCalculateRequestDto dto = new TaxCalculationController.BatchCalculateRequestDto();
        dto.setTransactions(Collections.emptyList());
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}