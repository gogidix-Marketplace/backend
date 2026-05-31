package com.gogidix.sales.dashboard.domain.model;

import com.gogidix.sales.dashboard.domain.model.KPIWidget;
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
class KPIWidget_DataSourceTest {

        @Test
    void testBuilder() {
        KPIWidget.DataSource dto = KPIWidget.DataSource.builder()
                        .sourceId("test-sourceId")
            .sourceType("test-sourceType")
            .sourceKey("test-sourceKey")
            .field("test-field")
            .filters(Collections.emptyMap())
            .aggregation("test-aggregation")
            .weight(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-sourceId", dto.getSourceId());
        assertEquals("test-sourceType", dto.getSourceType());
        assertEquals("test-sourceKey", dto.getSourceKey());
        assertEquals("test-field", dto.getField());
        assertEquals("test-aggregation", dto.getAggregation());
        assertEquals(42, dto.getWeight());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.DataSource dto = new KPIWidget.DataSource();
        dto.setSourceId("val-sourceId");
        dto.setSourceType("val-sourceType");
        dto.setSourceKey("val-sourceKey");
        dto.setField("val-field");
        dto.setAggregation("val-aggregation");
        dto.setWeight(99);
        assertEquals("val-sourceId", dto.getSourceId());
        assertEquals("val-sourceType", dto.getSourceType());
        assertEquals("val-sourceKey", dto.getSourceKey());
        assertEquals("val-field", dto.getField());
        assertEquals("val-aggregation", dto.getAggregation());
        assertEquals(99, dto.getWeight());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.DataSource dto1 = KPIWidget.DataSource.builder()
                        .sourceId("test-sourceId")
            .sourceType("test-sourceType")
            .sourceKey("test-sourceKey")
            .field("test-field")
            .filters(Collections.emptyMap())
            .aggregation("test-aggregation")
            .weight(42)
            .build();
        KPIWidget.DataSource dto2 = KPIWidget.DataSource.builder()
                        .sourceId("test-sourceId")
            .sourceType("test-sourceType")
            .sourceKey("test-sourceKey")
            .field("test-field")
            .filters(Collections.emptyMap())
            .aggregation("test-aggregation")
            .weight(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.DataSource dto = KPIWidget.DataSource.builder()
                        .sourceId("test-sourceId")
            .sourceType("test-sourceType")
            .sourceKey("test-sourceKey")
            .field("test-field")
            .filters(Collections.emptyMap())
            .aggregation("test-aggregation")
            .weight(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}