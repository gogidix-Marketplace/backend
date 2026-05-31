package com.gogidix.sales.dealmanagement.application.service;

import com.gogidix.sales.dealmanagement.application.service.DealQueryService;
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
class DealQueryService_DealSummaryTest {

        @Test
    void testBuilder() {
        DealQueryService.DealSummary dto = DealQueryService.DealSummary.builder()
                        .totalDeals(42)
            .openDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getTotalDeals());
        assertEquals(42, dto.getOpenDeals());
        assertEquals(42, dto.getWonDeals());
        assertEquals(42, dto.getLostDeals());
    }

    @Test
    void testBuilderWithValues() {
        DealQueryService.DealSummary dto = DealQueryService.DealSummary.builder()
            .totalDeals(99)
            .openDeals(99)
            .wonDeals(99)
            .lostDeals(99)
            .build();
        assertNotNull(dto);
    }

    @Test
    void testEqualsAndHashCode() {
        DealQueryService.DealSummary dto1 = DealQueryService.DealSummary.builder()
                        .totalDeals(42)
            .openDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .build();
        DealQueryService.DealSummary dto2 = DealQueryService.DealSummary.builder()
                        .totalDeals(42)
            .openDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DealQueryService.DealSummary dto = DealQueryService.DealSummary.builder()
                        .totalDeals(42)
            .openDeals(42)
            .wonDeals(42)
            .lostDeals(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}