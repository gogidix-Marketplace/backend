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
class CashflowForecastResponseDto_ForecastPeriodDataDtoTest {

        @Test
    void testBuilder() {
        CashflowForecastResponseDto.ForecastPeriodDataDto dto = CashflowForecastResponseDto.ForecastPeriodDataDto.builder()
                        .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .inflow(BigDecimal.TEN)
            .outflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .transactionCount(42)
            .build();
        assertNotNull(dto);
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,1,15), dto.getPeriodEnd());
        assertEquals(BigDecimal.TEN, dto.getOpeningBalance());
        assertEquals(BigDecimal.TEN, dto.getInflow());
        assertEquals(BigDecimal.TEN, dto.getOutflow());
        assertEquals(BigDecimal.TEN, dto.getNetCashflow());
        assertEquals(BigDecimal.TEN, dto.getClosingBalance());
        assertEquals(42, dto.getTransactionCount());
    }

    @Test
    void testSettersAndGetters() {
        CashflowForecastResponseDto.ForecastPeriodDataDto dto = new CashflowForecastResponseDto.ForecastPeriodDataDto();
        dto.setPeriodStart(LocalDate.of(2025,6,1));
        dto.setPeriodEnd(LocalDate.of(2025,6,1));
        dto.setOpeningBalance(BigDecimal.ONE);
        dto.setInflow(BigDecimal.ONE);
        dto.setOutflow(BigDecimal.ONE);
        dto.setNetCashflow(BigDecimal.ONE);
        dto.setClosingBalance(BigDecimal.ONE);
        dto.setTransactionCount(99);
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodStart());
        assertEquals(LocalDate.of(2025,6,1), dto.getPeriodEnd());
        assertEquals(BigDecimal.ONE, dto.getOpeningBalance());
        assertEquals(BigDecimal.ONE, dto.getInflow());
        assertEquals(BigDecimal.ONE, dto.getOutflow());
        assertEquals(BigDecimal.ONE, dto.getNetCashflow());
        assertEquals(BigDecimal.ONE, dto.getClosingBalance());
        assertEquals(99, dto.getTransactionCount());
    }

    @Test
    void testEqualsAndHashCode() {
        CashflowForecastResponseDto.ForecastPeriodDataDto dto1 = CashflowForecastResponseDto.ForecastPeriodDataDto.builder()
                        .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .inflow(BigDecimal.TEN)
            .outflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .transactionCount(42)
            .build();
        CashflowForecastResponseDto.ForecastPeriodDataDto dto2 = CashflowForecastResponseDto.ForecastPeriodDataDto.builder()
                        .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .inflow(BigDecimal.TEN)
            .outflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .transactionCount(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CashflowForecastResponseDto.ForecastPeriodDataDto dto = CashflowForecastResponseDto.ForecastPeriodDataDto.builder()
                        .periodStart(LocalDate.of(2025,1,15))
            .periodEnd(LocalDate.of(2025,1,15))
            .openingBalance(BigDecimal.TEN)
            .inflow(BigDecimal.TEN)
            .outflow(BigDecimal.TEN)
            .netCashflow(BigDecimal.TEN)
            .closingBalance(BigDecimal.TEN)
            .transactionCount(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}