package com.gogidix.finance.cashflow.interfaces.rest;

import com.gogidix.finance.cashflow.domain.model.CashflowForecast;
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
class CashflowForecastController_ConfidenceRequestDtoTest {

        @Test
    void testSettersAndGetters() {
        CashflowForecastController.ConfidenceRequestDto dto = new CashflowForecastController.ConfidenceRequestDto();
        dto.setVariancePercentage(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastController.ConfidenceRequestDto dto1 = new CashflowForecastController.ConfidenceRequestDto();
        CashflowForecastController.ConfidenceRequestDto dto2 = new CashflowForecastController.ConfidenceRequestDto();
        dto1.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto1.setVariancePercentage(BigDecimal.TEN);
        dto2.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto2.setVariancePercentage(BigDecimal.TEN);
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setConfidenceLevel(CashflowForecast.ConfidenceLevel.MEDIUM);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        CashflowForecastController.ConfidenceRequestDto dto = new CashflowForecastController.ConfidenceRequestDto();
        dto.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto.setVariancePercentage(BigDecimal.TEN);
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        CashflowForecastController.ConfidenceRequestDto dto = new CashflowForecastController.ConfidenceRequestDto();
        dto.setConfidenceLevel(CashflowForecast.ConfidenceLevel.LOW);
        dto.setVariancePercentage(BigDecimal.TEN);
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}