package com.gogidix.finance.forecasting.application.service;

import com.gogidix.finance.forecasting.application.service.ForecastQueryService;
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
class ForecastQueryService_ForecastSummaryTest {

        @Test
    void testBuilder() {
        ForecastQueryService.ForecastSummary dto = ForecastQueryService.ForecastSummary.builder()
                        .totalCount(42L)
            .totalForecastAmount(BigDecimal.TEN)
            .totalActualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .draftCount(42L)
            .pendingCount(42L)
            .approvedCount(42L)
            .countByType(Collections.emptyMap())
            .countByHorizon(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalCount());
        assertEquals(BigDecimal.TEN, dto.getTotalForecastAmount());
        assertEquals(BigDecimal.TEN, dto.getTotalActualAmount());
        assertEquals(BigDecimal.TEN, dto.getVarianceAmount());
        assertEquals(42L, dto.getDraftCount());
        assertEquals(42L, dto.getPendingCount());
        assertEquals(42L, dto.getApprovedCount());
    }

    @Test
    void testBuilderWithValues() {
        ForecastQueryService.ForecastSummary dto = ForecastQueryService.ForecastSummary.builder()
            .totalForecastAmount(BigDecimal.ONE)
            .totalActualAmount(BigDecimal.ONE)
            .varianceAmount(BigDecimal.ONE)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQueryService.ForecastSummary dto1 = ForecastQueryService.ForecastSummary.builder()
                        .totalCount(42L)
            .totalForecastAmount(BigDecimal.TEN)
            .totalActualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .draftCount(42L)
            .pendingCount(42L)
            .approvedCount(42L)
            .countByType(Collections.emptyMap())
            .countByHorizon(Collections.emptyMap())
            .build();
        ForecastQueryService.ForecastSummary dto2 = ForecastQueryService.ForecastSummary.builder()
                        .totalCount(42L)
            .totalForecastAmount(BigDecimal.TEN)
            .totalActualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .draftCount(42L)
            .pendingCount(42L)
            .approvedCount(42L)
            .countByType(Collections.emptyMap())
            .countByHorizon(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ForecastQueryService.ForecastSummary dto = ForecastQueryService.ForecastSummary.builder()
                        .totalCount(42L)
            .totalForecastAmount(BigDecimal.TEN)
            .totalActualAmount(BigDecimal.TEN)
            .varianceAmount(BigDecimal.TEN)
            .draftCount(42L)
            .pendingCount(42L)
            .approvedCount(42L)
            .countByType(Collections.emptyMap())
            .countByHorizon(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}