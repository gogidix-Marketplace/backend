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
class ForecastQuery_GetForecastsByRegionQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetForecastsByRegionQuery dto = new ForecastQuery.GetForecastsByRegionQuery();
        dto.setRegion("val-region");
        assertEquals("val-region", dto.getRegion());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetForecastsByRegionQuery dto1 = new ForecastQuery.GetForecastsByRegionQuery();
        ForecastQuery.GetForecastsByRegionQuery dto2 = new ForecastQuery.GetForecastsByRegionQuery();
        dto1.setRegion("test");
        dto2.setRegion("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setRegion(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetForecastsByRegionQuery dto = new ForecastQuery.GetForecastsByRegionQuery();
        dto.setRegion("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetForecastsByRegionQuery dto = new ForecastQuery.GetForecastsByRegionQuery();
        dto.setRegion("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}