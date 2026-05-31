package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.GlobalSalesDashboard;
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
class GlobalSalesDashboard_DataFreshnessTest {

        @Test
    void testBuilder() {
        GlobalSalesDashboard.DataFreshness dto = GlobalSalesDashboard.DataFreshness.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .missingDataRegions(Collections.emptyList())
            .lagMinutes(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-dataQuality", dto.getDataQuality());
        assertEquals(42, dto.getCompletenessPercentage());
        assertEquals(42, dto.getLagMinutes());
    }

    @Test
    void testSettersAndGetters() {
        GlobalSalesDashboard.DataFreshness dto = new GlobalSalesDashboard.DataFreshness();
        dto.setDataQuality("val-dataQuality");
        dto.setCompletenessPercentage(99);
        dto.setLagMinutes(99);
        assertEquals("val-dataQuality", dto.getDataQuality());
        assertEquals(99, dto.getCompletenessPercentage());
        assertEquals(99, dto.getLagMinutes());
    }

    @Test
    void testEqualsAndHashCode() {
        GlobalSalesDashboard.DataFreshness dto1 = GlobalSalesDashboard.DataFreshness.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .missingDataRegions(Collections.emptyList())
            .lagMinutes(42)
            .build();
        GlobalSalesDashboard.DataFreshness dto2 = GlobalSalesDashboard.DataFreshness.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .missingDataRegions(Collections.emptyList())
            .lagMinutes(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        GlobalSalesDashboard.DataFreshness dto = GlobalSalesDashboard.DataFreshness.builder()
                        .lastDataUpdate(Instant.parse("2025-01-15T10:00:00Z"))
            .dataQuality("test-dataQuality")
            .completenessPercentage(42)
            .missingDataRegions(Collections.emptyList())
            .lagMinutes(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}