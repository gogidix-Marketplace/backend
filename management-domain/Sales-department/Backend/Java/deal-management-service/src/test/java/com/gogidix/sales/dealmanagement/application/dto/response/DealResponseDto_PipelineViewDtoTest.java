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
class DealResponseDto_PipelineViewDtoTest {

        @Test
    void testBuilder() {
        DealResponseDto.PipelineViewDto dto = DealResponseDto.PipelineViewDto.builder()
                        .stage(null)
            .order(42)
            .probability(42)
            .totalAmount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .dealCount(42)
            .deals(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getOrder());
        assertEquals(42, dto.getProbability());
        assertEquals(BigDecimal.TEN, dto.getTotalAmount());
        assertEquals(BigDecimal.TEN, dto.getWeightedAmount());
        assertEquals(42, dto.getDealCount());
    }

    @Test
    void testSettersAndGetters() {
        DealResponseDto.PipelineViewDto dto = new DealResponseDto.PipelineViewDto();
        dto.setOrder(99);
        dto.setProbability(99);
        dto.setTotalAmount(BigDecimal.ONE);
        dto.setWeightedAmount(BigDecimal.ONE);
        dto.setDealCount(99);
        assertEquals(99, dto.getOrder());
        assertEquals(99, dto.getProbability());
        assertEquals(BigDecimal.ONE, dto.getTotalAmount());
        assertEquals(BigDecimal.ONE, dto.getWeightedAmount());
        assertEquals(99, dto.getDealCount());
    }

    @Test
    void testEqualsAndHashCode() {
        DealResponseDto.PipelineViewDto dto1 = DealResponseDto.PipelineViewDto.builder()
                        .stage(null)
            .order(42)
            .probability(42)
            .totalAmount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .dealCount(42)
            .deals(Collections.emptyList())
            .build();
        DealResponseDto.PipelineViewDto dto2 = DealResponseDto.PipelineViewDto.builder()
                        .stage(null)
            .order(42)
            .probability(42)
            .totalAmount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .dealCount(42)
            .deals(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DealResponseDto.PipelineViewDto dto = DealResponseDto.PipelineViewDto.builder()
                        .stage(null)
            .order(42)
            .probability(42)
            .totalAmount(BigDecimal.TEN)
            .weightedAmount(BigDecimal.TEN)
            .dealCount(42)
            .deals(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}