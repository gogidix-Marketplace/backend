package com.gogidix.sales.dealmanagement.application.dto.response;

import com.gogidix.sales.dealmanagement.application.dto.response.DealResponseDto;
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
class DealResponseDto_DealSummaryDtoTest {

        @Test
    void testBuilder() {
        DealResponseDto.DealSummaryDto dto = DealResponseDto.DealSummaryDto.builder()
                        .dealId("test-dealId")
            .dealName("test-dealName")
            .dealCode("test-dealCode")
            .amount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .currency("test-currency")
            .stage(null)
            .probability(42)
            .priority(null)
            .ownerName("test-ownerName")
            .accountName("test-accountName")
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .daysInStage(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-dealId", dto.getDealId());
        assertEquals("test-dealName", dto.getDealName());
        assertEquals("test-dealCode", dto.getDealCode());
        assertEquals(BigDecimal.TEN, dto.getAmount());
        assertEquals(BigDecimal.TEN, dto.getWeightedAmount());
        assertEquals("test-currency", dto.getCurrency());
        assertEquals(42, dto.getProbability());
        assertEquals("test-ownerName", dto.getOwnerName());
        assertEquals("test-accountName", dto.getAccountName());
        assertEquals(LocalDate.of(2025,1,15), dto.getExpectedCloseDate());
        assertEquals(42, dto.getDaysInStage());
    }

    @Test
    void testSettersAndGetters() {
        DealResponseDto.DealSummaryDto dto = new DealResponseDto.DealSummaryDto();
        dto.setDealId("val-dealId");
        dto.setDealName("val-dealName");
        dto.setDealCode("val-dealCode");
        dto.setAmount(BigDecimal.ONE);
        dto.setWeightedAmount(BigDecimal.ONE);
        dto.setCurrency("val-currency");
        dto.setProbability(99);
        dto.setOwnerName("val-ownerName");
        dto.setAccountName("val-accountName");
        dto.setExpectedCloseDate(LocalDate.of(2025,6,1));
        dto.setDaysInStage(99);
        assertEquals("val-dealId", dto.getDealId());
        assertEquals("val-dealName", dto.getDealName());
        assertEquals("val-dealCode", dto.getDealCode());
        assertEquals(BigDecimal.ONE, dto.getAmount());
        assertEquals(BigDecimal.ONE, dto.getWeightedAmount());
        assertEquals("val-currency", dto.getCurrency());
        assertEquals(99, dto.getProbability());
        assertEquals("val-ownerName", dto.getOwnerName());
        assertEquals("val-accountName", dto.getAccountName());
        assertEquals(LocalDate.of(2025,6,1), dto.getExpectedCloseDate());
        assertEquals(99, dto.getDaysInStage());
    }

    @Test
    void testEqualsAndHashCode() {
        DealResponseDto.DealSummaryDto dto1 = DealResponseDto.DealSummaryDto.builder()
                        .dealId("test-dealId")
            .dealName("test-dealName")
            .dealCode("test-dealCode")
            .amount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .currency("test-currency")
            .stage(null)
            .probability(42)
            .priority(null)
            .ownerName("test-ownerName")
            .accountName("test-accountName")
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .daysInStage(42)
            .build();
        DealResponseDto.DealSummaryDto dto2 = DealResponseDto.DealSummaryDto.builder()
                        .dealId("test-dealId")
            .dealName("test-dealName")
            .dealCode("test-dealCode")
            .amount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .currency("test-currency")
            .stage(null)
            .probability(42)
            .priority(null)
            .ownerName("test-ownerName")
            .accountName("test-accountName")
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .daysInStage(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DealResponseDto.DealSummaryDto dto = DealResponseDto.DealSummaryDto.builder()
                        .dealId("test-dealId")
            .dealName("test-dealName")
            .dealCode("test-dealCode")
            .amount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .currency("test-currency")
            .stage(null)
            .probability(42)
            .priority(null)
            .ownerName("test-ownerName")
            .accountName("test-accountName")
            .expectedCloseDate(LocalDate.of(2025,1,15))
            .daysInStage(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}