package com.gogidix.management.executive.financial.application.dto;

import com.gogidix.management.executive.financial.application.dto.FinancialDataDto;
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
class FinancialDataDto_WidgetDtoTest {

        @Test
    void testBuilder() {
        FinancialDataDto.WidgetDto dto = FinancialDataDto.WidgetDto.builder()
                        .id("test-id")
            .name("test-name")
            .type(null)
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config("test-config")
            .dataSource("test-dataSource")
            .build();
        assertNotNull(dto);
        assertEquals("test-id", dto.getId());
        assertEquals("test-name", dto.getName());
        assertEquals(42, dto.getPosition());
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumn());
        assertEquals(42, dto.getWidth());
        assertEquals(42, dto.getHeight());
        assertEquals("test-config", dto.getConfig());
        assertEquals("test-dataSource", dto.getDataSource());
    }

    @Test
    void testSettersAndGetters() {
        FinancialDataDto.WidgetDto dto = new FinancialDataDto.WidgetDto();
        dto.setId("val-id");
        dto.setName("val-name");
        dto.setPosition(99);
        dto.setRow(99);
        dto.setColumn(99);
        dto.setWidth(99);
        dto.setHeight(99);
        dto.setConfig("val-config");
        dto.setDataSource("val-dataSource");
        assertEquals("val-id", dto.getId());
        assertEquals("val-name", dto.getName());
        assertEquals(99, dto.getPosition());
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumn());
        assertEquals(99, dto.getWidth());
        assertEquals(99, dto.getHeight());
        assertEquals("val-config", dto.getConfig());
        assertEquals("val-dataSource", dto.getDataSource());
    }

    @Test
    void testEqualsAndHashCode() {
        FinancialDataDto.WidgetDto dto1 = FinancialDataDto.WidgetDto.builder()
                        .id("test-id")
            .name("test-name")
            .type(null)
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config("test-config")
            .dataSource("test-dataSource")
            .build();
        FinancialDataDto.WidgetDto dto2 = FinancialDataDto.WidgetDto.builder()
                        .id("test-id")
            .name("test-name")
            .type(null)
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config("test-config")
            .dataSource("test-dataSource")
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        FinancialDataDto.WidgetDto dto = FinancialDataDto.WidgetDto.builder()
                        .id("test-id")
            .name("test-name")
            .type(null)
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config("test-config")
            .dataSource("test-dataSource")
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}