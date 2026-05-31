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
class ForecastQuery_GetLineItemsQueryTest {

        @Test
    void testSettersAndGetters() {
        ForecastQuery.GetLineItemsQuery dto = new ForecastQuery.GetLineItemsQuery();
        dto.setForecastId("val-forecastId");
        assertEquals("val-forecastId", dto.getForecastId());
    }

    @Test
    void testEqualsAndHashCode() {
        ForecastQuery.GetLineItemsQuery dto1 = new ForecastQuery.GetLineItemsQuery();
        ForecastQuery.GetLineItemsQuery dto2 = new ForecastQuery.GetLineItemsQuery();
        dto1.setForecastId("test");
        dto2.setForecastId("test");
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
        dto2.setForecastId(null);
        assertNotEquals(dto1, dto2);
    }

    @Test
    void testToString() {
        ForecastQuery.GetLineItemsQuery dto = new ForecastQuery.GetLineItemsQuery();
        dto.setForecastId("test");
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

    @Test
    void testEqualsNullAndOtherType() {
        ForecastQuery.GetLineItemsQuery dto = new ForecastQuery.GetLineItemsQuery();
        dto.setForecastId("test");
        assertNotEquals(null, dto);
        assertNotEquals("string", dto);
        assertEquals(dto, dto);
    }

}