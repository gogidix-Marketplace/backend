package com.gogidix.management.executive.operations.application.command;

import com.gogidix.management.executive.operations.application.command.CreateOperationsCommand;
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
class CreateOperationsCommand_WidgetConfigTest {

        @Test
    void testBuilder() {
        CreateOperationsCommand.WidgetConfig dto = CreateOperationsCommand.WidgetConfig.builder()
                        .name("test-name")
            .type("test-type")
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config("test-config")
            .dataSource("test-dataSource")
            .build();
        assertNotNull(dto);
        assertEquals("test-name", dto.getName());
        assertEquals("test-type", dto.getType());
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
        CreateOperationsCommand.WidgetConfig dto = new CreateOperationsCommand.WidgetConfig();
        dto.setName("val-name");
        dto.setType("val-type");
        dto.setPosition(99);
        dto.setRow(99);
        dto.setColumn(99);
        dto.setWidth(99);
        dto.setHeight(99);
        dto.setConfig("val-config");
        dto.setDataSource("val-dataSource");
        assertEquals("val-name", dto.getName());
        assertEquals("val-type", dto.getType());
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
        CreateOperationsCommand.WidgetConfig dto1 = CreateOperationsCommand.WidgetConfig.builder()
                        .name("test-name")
            .type("test-type")
            .position(42)
            .row(42)
            .column(42)
            .width(42)
            .height(42)
            .config("test-config")
            .dataSource("test-dataSource")
            .build();
        CreateOperationsCommand.WidgetConfig dto2 = CreateOperationsCommand.WidgetConfig.builder()
                        .name("test-name")
            .type("test-type")
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
        CreateOperationsCommand.WidgetConfig dto = CreateOperationsCommand.WidgetConfig.builder()
                        .name("test-name")
            .type("test-type")
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