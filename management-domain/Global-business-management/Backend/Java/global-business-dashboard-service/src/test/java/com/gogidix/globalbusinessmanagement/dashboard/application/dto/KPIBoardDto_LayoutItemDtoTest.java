package com.gogidix.globalbusinessmanagement.dashboard.application.dto;

import com.gogidix.globalbusinessmanagement.dashboard.application.dto.KPIBoardDto;
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
class KPIBoardDto_LayoutItemDtoTest {

        @Test
    void testBuilder() {
        KPIBoardDto.LayoutItemDto dto = KPIBoardDto.LayoutItemDto.builder()
                        .kpiId("test-kpiId")
            .column(42)
            .row(42)
            .columnSpan(42)
            .rowSpan(42)
            .build();
        assertNotNull(dto);
        assertEquals("test-kpiId", dto.getKpiId());
        assertEquals(42, dto.getColumn());
        assertEquals(42, dto.getRow());
        assertEquals(42, dto.getColumnSpan());
        assertEquals(42, dto.getRowSpan());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoardDto.LayoutItemDto dto = new KPIBoardDto.LayoutItemDto();
        dto.setKpiId("val-kpiId");
        dto.setColumn(99);
        dto.setRow(99);
        dto.setColumnSpan(99);
        dto.setRowSpan(99);
        assertEquals("val-kpiId", dto.getKpiId());
        assertEquals(99, dto.getColumn());
        assertEquals(99, dto.getRow());
        assertEquals(99, dto.getColumnSpan());
        assertEquals(99, dto.getRowSpan());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoardDto.LayoutItemDto dto1 = KPIBoardDto.LayoutItemDto.builder()
                        .kpiId("test-kpiId")
            .column(42)
            .row(42)
            .columnSpan(42)
            .rowSpan(42)
            .build();
        KPIBoardDto.LayoutItemDto dto2 = KPIBoardDto.LayoutItemDto.builder()
                        .kpiId("test-kpiId")
            .column(42)
            .row(42)
            .columnSpan(42)
            .rowSpan(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoardDto.LayoutItemDto dto = KPIBoardDto.LayoutItemDto.builder()
                        .kpiId("test-kpiId")
            .column(42)
            .row(42)
            .columnSpan(42)
            .rowSpan(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}