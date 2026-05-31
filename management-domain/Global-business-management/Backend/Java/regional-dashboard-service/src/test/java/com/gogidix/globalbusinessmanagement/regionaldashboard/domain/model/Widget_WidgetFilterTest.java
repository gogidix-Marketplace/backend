package com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model;

import com.gogidix.globalbusinessmanagement.regionaldashboard.domain.model.Widget;
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
class Widget_WidgetFilterTest {

        @Test
    void testBuilder() {
        Widget.WidgetFilter dto = Widget.WidgetFilter.builder()
                        .name("test-name")
            .field("test-field")
            .operator("test-operator")
            .value("test-value")
            .type("test-type")
            .isDynamic(true)
            .dataSource("test-dataSource")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-field", dto.getField());
        assertEquals("test-operator", dto.getOperator());
        assertEquals("test-value", dto.getValue());
        assertEquals("test-type", dto.getType());
        assertTrue(dto.getIsDynamic());
        assertEquals("test-dataSource", dto.getDataSource());
    }

    @Test
    void testSettersAndGetters() {
        Widget.WidgetFilter dto = new Widget.WidgetFilter();
        dto.setName("val-name");
        dto.setField("val-field");
        dto.setOperator("val-operator");
        dto.setValue("val-value");
        dto.setType("val-type");
        dto.setIsDynamic(true);
        dto.setDataSource("val-dataSource");
        assertEquals("val-name", dto.getName());
        assertEquals("val-field", dto.getField());
        assertEquals("val-operator", dto.getOperator());
        assertEquals("val-value", dto.getValue());
        assertEquals("val-type", dto.getType());
        assertTrue(dto.getIsDynamic());
        assertEquals("val-dataSource", dto.getDataSource());
    }

    @Test
    void testEqualsAndHashCode() {
        Widget.WidgetFilter dto1 = Widget.WidgetFilter.builder()
                        .name("test-name")
            .field("test-field")
            .operator("test-operator")
            .value("test-value")
            .type("test-type")
            .isDynamic(true)
            .dataSource("test-dataSource")
            .build();
        Widget.WidgetFilter dto2 = Widget.WidgetFilter.builder()
                        .name("test-name")
            .field("test-field")
            .operator("test-operator")
            .value("test-value")
            .type("test-type")
            .isDynamic(true)
            .dataSource("test-dataSource")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Widget.WidgetFilter dto = Widget.WidgetFilter.builder()
                        .name("test-name")
            .field("test-field")
            .operator("test-operator")
            .value("test-value")
            .type("test-type")
            .isDynamic(true)
            .dataSource("test-dataSource")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}