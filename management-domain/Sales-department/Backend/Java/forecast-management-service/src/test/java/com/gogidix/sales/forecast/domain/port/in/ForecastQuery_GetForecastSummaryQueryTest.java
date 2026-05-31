package com.gogidix.sales.forecast.domain.port.in;

import com.gogidix.sales.forecast.domain.port.in.ForecastQuery;
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
class ForecastQuery_GetForecastSummaryQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetForecastSummaryQuery dto = new ForecastQuery.GetForecastSummaryQuery();
        dto.setRegion("val-region");
        dto.setTerritory("val-territory");
        assertEquals("val-region", dto.getRegion());
        assertEquals("val-territory", dto.getTerritory());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetForecastSummaryQuery dto1 = new ForecastQuery.GetForecastSummaryQuery();
        ForecastQuery.GetForecastSummaryQuery dto2 = new ForecastQuery.GetForecastSummaryQuery();
        dto1.setStartDate(null);
        dto1.setEndDate(null);
        dto1.setRegion("test");
        dto1.setTerritory("test");
        dto2.setStartDate(null);
        dto2.setEndDate(null);
        dto2.setRegion("test");
        dto2.setTerritory("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setRegion(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetForecastSummaryQuery dto = new ForecastQuery.GetForecastSummaryQuery();
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setRegion("test");
        dto.setTerritory("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetForecastSummaryQuery dto = new ForecastQuery.GetForecastSummaryQuery();
        dto.setStartDate(null);
        dto.setEndDate(null);
        dto.setRegion("test");
        dto.setTerritory("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}