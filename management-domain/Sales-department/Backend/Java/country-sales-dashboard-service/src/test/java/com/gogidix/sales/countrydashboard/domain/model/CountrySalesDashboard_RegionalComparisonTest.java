package com.gogidix.sales.countrydashboard.domain.model;

import com.gogidix.sales.countrydashboard.domain.model.CountrySalesDashboard;
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
class CountrySalesDashboard_RegionalComparisonTest {

        @Test
    void testBuilder() {
        CountrySalesDashboard.RegionalComparison dto = CountrySalesDashboard.RegionalComparison.builder()
                        .region("test-region")
            .countryMetrics(Collections.emptyList())
            .regionalAverage(null)
            .rank("test-rank")
            .percentile(BigDecimal.TEN)
            .build();
        assertNotNull(dto);
        assertEquals("test-region", dto.getRegion());
        assertEquals("test-rank", dto.getRank());
        assertEquals(BigDecimal.TEN, dto.getPercentile());
    }

    @Test
    void testSettersAndGetters() {
        CountrySalesDashboard.RegionalComparison dto = new CountrySalesDashboard.RegionalComparison();
        dto.setRegion("val-region");
        dto.setRank("val-rank");
        dto.setPercentile(BigDecimal.ONE);
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-rank", dto.getRank());
        assertEquals(BigDecimal.ONE, dto.getPercentile());
    }

    @Test
    void testEqualsAndHashCode() {
        CountrySalesDashboard.RegionalComparison dto1 = CountrySalesDashboard.RegionalComparison.builder()
                        .region("test-region")
            .countryMetrics(Collections.emptyList())
            .regionalAverage(null)
            .rank("test-rank")
            .percentile(BigDecimal.TEN)
            .build();
        CountrySalesDashboard.RegionalComparison dto2 = CountrySalesDashboard.RegionalComparison.builder()
                        .region("test-region")
            .countryMetrics(Collections.emptyList())
            .regionalAverage(null)
            .rank("test-rank")
            .percentile(BigDecimal.TEN)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        CountrySalesDashboard.RegionalComparison dto = CountrySalesDashboard.RegionalComparison.builder()
                        .region("test-region")
            .countryMetrics(Collections.emptyList())
            .regionalAverage(null)
            .rank("test-rank")
            .percentile(BigDecimal.TEN)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}