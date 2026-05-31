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
class ForecastQuery_GetForecastVersionsQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetForecastVersionsQuery dto = new ForecastQuery.GetForecastVersionsQuery();
        dto.setParentForecastId("val-parentForecastId");
        assertEquals("val-parentForecastId", dto.getParentForecastId());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetForecastVersionsQuery dto1 = new ForecastQuery.GetForecastVersionsQuery();
        ForecastQuery.GetForecastVersionsQuery dto2 = new ForecastQuery.GetForecastVersionsQuery();
        dto1.setParentForecastId("test");
        dto2.setParentForecastId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setParentForecastId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetForecastVersionsQuery dto = new ForecastQuery.GetForecastVersionsQuery();
        dto.setParentForecastId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetForecastVersionsQuery dto = new ForecastQuery.GetForecastVersionsQuery();
        dto.setParentForecastId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}