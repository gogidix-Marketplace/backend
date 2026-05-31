package com.gogidix.sales.forecast.application.dto.response;

import com.gogidix.sales.forecast.application.dto.response.ForecastSummaryDto;
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
class ForecastSummaryDtoTest {

        @Test
    void testBuilder() {
        ForecastSummaryDto dto = ForecastSummaryDto.builder()
                        .totalCount(42L)
            .totalBestCase(BigDecimal.TEN)
            .totalLikely(BigDecimal.TEN)
            .totalWorstCase(BigDecimal.TEN)
            .draftCount(42L)
            .submittedCount(42L)
            .approvedCount(42L)
            .publishedCount(42L)
            .build();
        assertNotNull(dto);
        assertEquals(42L, dto.getTotalCount());
        assertEquals(BigDecimal.TEN, dto.getTotalBestCase());
        assertEquals(BigDecimal.TEN, dto.getTotalLikely());
        assertEquals(BigDecimal.TEN, dto.getTotalWorstCase());
        assertEquals(42L, dto.getDraftCount());
        assertEquals(42L, dto.getSubmittedCount());
        assertEquals(42L, dto.getApprovedCount());
        assertEquals(42L, dto.getPublishedCount());
    }

    @Test
    void testSettersAndGetters() {
        ForecastSummaryDto dto = new ForecastSummaryDto();
        dto.setTotalBestCase(BigDecimal.ONE);
        dto.setTotalLikely(BigDecimal.ONE);
        dto.setTotalWorstCase(BigDecimal.ONE);
        assertEquals(BigDecimal.ONE, dto.getTotalBestCase());
        assertEquals(BigDecimal.ONE, dto.getTotalLikely());
        assertEquals(BigDecimal.ONE, dto.getTotalWorstCase());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastSummaryDto dto1 = ForecastSummaryDto.builder()
                        .totalCount(42L)
            .totalBestCase(BigDecimal.TEN)
            .totalLikely(BigDecimal.TEN)
            .totalWorstCase(BigDecimal.TEN)
            .draftCount(42L)
            .submittedCount(42L)
            .approvedCount(42L)
            .publishedCount(42L)
            .build();
        ForecastSummaryDto dto2 = ForecastSummaryDto.builder()
                        .totalCount(42L)
            .totalBestCase(BigDecimal.TEN)
            .totalLikely(BigDecimal.TEN)
            .totalWorstCase(BigDecimal.TEN)
            .draftCount(42L)
            .submittedCount(42L)
            .approvedCount(42L)
            .publishedCount(42L)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        ForecastSummaryDto dto = ForecastSummaryDto.builder()
                        .totalCount(42L)
            .totalBestCase(BigDecimal.TEN)
            .totalLikely(BigDecimal.TEN)
            .totalWorstCase(BigDecimal.TEN)
            .draftCount(42L)
            .submittedCount(42L)
            .approvedCount(42L)
            .publishedCount(42L)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}