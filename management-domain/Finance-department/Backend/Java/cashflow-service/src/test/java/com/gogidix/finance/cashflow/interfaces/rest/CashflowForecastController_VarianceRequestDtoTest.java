package com.gogidix.finance.cashflow.interfaces.rest;

import com.gogidix.finance.cashflow.interfaces.rest.CashflowForecastController;
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
class CashflowForecastController_VarianceRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastController.VarianceRequestDto dto = new CashflowForecastController.VarianceRequestDto();
        dto.setCategory("val-category");
        dto.setForecastedAmount(BigDecimal.ONE);
        dto.setActualAmount(BigDecimal.ONE);
        assertEquals("val-category", dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getForecastedAmount());
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastController.VarianceRequestDto dto1 = new CashflowForecastController.VarianceRequestDto();
        CashflowForecastController.VarianceRequestDto dto2 = new CashflowForecastController.VarianceRequestDto();
        dto1.setCategory("test");
        dto1.setForecastedAmount(BigDecimal.TEN);
        dto1.setActualAmount(BigDecimal.TEN);
        dto2.setCategory("test");
        dto2.setForecastedAmount(BigDecimal.TEN);
        dto2.setActualAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setCategory(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastController.VarianceRequestDto dto = new CashflowForecastController.VarianceRequestDto();
        dto.setCategory("test");
        dto.setForecastedAmount(BigDecimal.TEN);
        dto.setActualAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastController.VarianceRequestDto dto = new CashflowForecastController.VarianceRequestDto();
        dto.setCategory("test");
        dto.setForecastedAmount(BigDecimal.TEN);
        dto.setActualAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}