package com.gogidix.finance.tax.interfaces.rest;

import com.gogidix.finance.tax.interfaces.rest.TaxRateController;
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
class TaxRateController_NewVersionRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        TaxRateController.NewVersionRequestDto dto = new TaxRateController.NewVersionRequestDto();
        dto.setNewRate(BigDecimal.ONE);
        dto.setNewEffectiveDate(LocalDate.of(2025,6,1));
        assertEquals(BigDecimal.ONE, dto.getNewRate());
        assertEquals(LocalDate.of(2025,6,1), dto.getNewEffectiveDate());
    }

    @Test
    void testEqualsAndHashCode() {
        TaxRateController.NewVersionRequestDto dto1 = new TaxRateController.NewVersionRequestDto();
        TaxRateController.NewVersionRequestDto dto2 = new TaxRateController.NewVersionRequestDto();
        dto1.setNewRate(BigDecimal.TEN);
        dto1.setNewEffectiveDate(LocalDate.of(2025,1,1));
        dto2.setNewRate(BigDecimal.TEN);
        dto2.setNewEffectiveDate(LocalDate.of(2025,1,1));
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setNewRate(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        TaxRateController.NewVersionRequestDto dto = new TaxRateController.NewVersionRequestDto();
        dto.setNewRate(BigDecimal.TEN);
        dto.setNewEffectiveDate(LocalDate.of(2025,1,1));
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        TaxRateController.NewVersionRequestDto dto = new TaxRateController.NewVersionRequestDto();
        dto.setNewRate(BigDecimal.TEN);
        dto.setNewEffectiveDate(LocalDate.of(2025,1,1));
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}