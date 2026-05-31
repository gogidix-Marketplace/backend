package com.gogidix.finance.cashflow.application.dto.response;

import com.gogidix.finance.cashflow.application.dto.response.CashflowForecastResponseDto;
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
class CashflowForecastResponseDto_ForecastVarianceDtoTest {

        @Test
    void testBuilder() {
        CashflowForecastResponseDto.ForecastVarianceDto dto = CashflowForecastResponseDto.ForecastVarianceDto.builder()
                        .category("test-category")
            .forecastedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .periodDate(LocalDate.of(2025,1,15))
            .build();
        assertNotNull(dto);
        assertEquals("test-category", dto.getCategory());
        assertEquals(BigDecimal.TEN, dto.getForecastedAmount());
        assertEquals(BigDecimal.TEN, dto.getActualAmount());
        assertEquals(BigDecimal.TEN, dto.getVariance());
        assertEquals(BigDecimal.TEN, dto.getVariancePercentage());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodDate());
    }

    @Test
    void testSettersAndGetters() {
        CashflowForecastResponseDto.ForecastVarianceDto dto = new CashflowForecastResponseDto.ForecastVarianceDto();
        dto.setCategory("val-category");
        dto.setForecastedAmount(BigDecimal.ONE);
        dto.setActualAmount(BigDecimal.ONE);
        dto.setVariance(BigDecimal.ONE);
        dto.setVariancePercentage(BigDecimal.ONE);
        dto.setPeriodDate(LocalDate.of(2025,6,1));
        assertEquals("val-category", dto.getCategory());
        assertEquals(BigDecimal.ONE, dto.getForecastedAmount());
        assertEquals(BigDecimal.ONE, dto.getActualAmount());
        assertEquals(BigDecimal.ONE, dto.getVariance());
        assertEquals(BigDecimal.ONE, dto.getVariancePercentage());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodDate());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastResponseDto.ForecastVarianceDto dto1 = CashflowForecastResponseDto.ForecastVarianceDto.builder()
                        .category("test-category")
            .forecastedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .periodDate(LocalDate.of(2025,1,15))
            .build();
        CashflowForecastResponseDto.ForecastVarianceDto dto2 = CashflowForecastResponseDto.ForecastVarianceDto.builder()
                        .category("test-category")
            .forecastedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .periodDate(LocalDate.of(2025,1,15))
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CashflowForecastResponseDto.ForecastVarianceDto dto = CashflowForecastResponseDto.ForecastVarianceDto.builder()
                        .category("test-category")
            .forecastedAmount(BigDecimal.TEN)
            .actualAmount(BigDecimal.TEN)
            .variance(BigDecimal.TEN)
            .variancePercentage(BigDecimal.TEN)
            .periodDate(LocalDate.of(2025,1,15))
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}