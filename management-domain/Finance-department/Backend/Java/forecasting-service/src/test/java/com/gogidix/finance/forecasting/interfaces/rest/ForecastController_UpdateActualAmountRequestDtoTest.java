package com.gogidix.finance.forecasting.interfaces.rest;

import com.gogidix.finance.forecasting.interfaces.rest.ForecastController;
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
class ForecastController_UpdateActualAmountRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        ForecastController.UpdateActualAmountRequestDto dto = new ForecastController.UpdateActualAmountRequestDto();
        dto.setActualAmount(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastController.UpdateActualAmountRequestDto dto1 = new ForecastController.UpdateActualAmountRequestDto();
        ForecastController.UpdateActualAmountRequestDto dto2 = new ForecastController.UpdateActualAmountRequestDto();
        dto1.setActualAmount(BigDecimal.TEN);
        dto2.setActualAmount(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setActualAmount(BigDecimal.ZERO);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastController.UpdateActualAmountRequestDto dto = new ForecastController.UpdateActualAmountRequestDto();
        dto.setActualAmount(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastController.UpdateActualAmountRequestDto dto = new ForecastController.UpdateActualAmountRequestDto();
        dto.setActualAmount(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}