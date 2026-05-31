package com.gogidix.sales.territory.application.service;

import com.gogidix.sales.territory.application.service.QuotaQueryService;
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
class QuotaQueryService_QuotaSummaryTest {

        @Test
    void testBuilder() {
        QuotaQueryService.QuotaSummary dto = QuotaQueryService.QuotaSummary.builder()
                        .totalQuotas(42L)
            .activeQuotas(42L)
            .completedQuotas(42L)
            .totalAmount(BigDecimal.TEN)
            .totalAchieved(BigDecimal.TEN)
            .countByType(Collections.emptyMap())
            .countByStatus(Collections.emptyMap())
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalQuotas());
        assertEquals(42L, dto.getActiveQuotas());
        assertEquals(42L, dto.getCompletedQuotas());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertEquals(BigDecimal.TEN, dto.getTotalAchieved());
    }

    @Test
    void testBuilderWithValues() {
        QuotaQueryService.QuotaSummary dto = QuotaQueryService.QuotaSummary.builder()
            .totalAmount(BigDecimal.ONE)
            .totalAchieved(BigDecimal.ONE)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        QuotaQueryService.QuotaSummary dto1 = QuotaQueryService.QuotaSummary.builder()
                        .totalQuotas(42L)
            .activeQuotas(42L)
            .completedQuotas(42L)
            .totalAmount(BigDecimal.TEN)
            .totalAchieved(BigDecimal.TEN)
            .countByType(Collections.emptyMap())
            .countByStatus(Collections.emptyMap())
            .build();
        QuotaQueryService.QuotaSummary dto2 = QuotaQueryService.QuotaSummary.builder()
                        .totalQuotas(42L)
            .activeQuotas(42L)
            .completedQuotas(42L)
            .totalAmount(BigDecimal.TEN)
            .totalAchieved(BigDecimal.TEN)
            .countByType(Collections.emptyMap())
            .countByStatus(Collections.emptyMap())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        QuotaQueryService.QuotaSummary dto = QuotaQueryService.QuotaSummary.builder()
                        .totalQuotas(42L)
            .activeQuotas(42L)
            .completedQuotas(42L)
            .totalAmount(BigDecimal.TEN)
            .totalAchieved(BigDecimal.TEN)
            .countByType(Collections.emptyMap())
            .countByStatus(Collections.emptyMap())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}