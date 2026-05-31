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
class KPIBoardDto_BoardLayoutDtoTest {

        @Test
    void testBuilder() {
        KPIBoardDto.BoardLayoutDto dto = KPIBoardDto.BoardLayoutDto.builder()
                        .layoutType("test-layoutType")
            .columns(42)
            .rows(42)
            .items(Collections.emptyList())
            .build();
        assertNotNull(dto);
        assertEquals("test-layoutType", dto.getLayoutType());
        assertEquals(42, dto.getColumns());
        assertEquals(42, dto.getRows());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoardDto.BoardLayoutDto dto = new KPIBoardDto.BoardLayoutDto();
        dto.setLayoutType("val-layoutType");
        dto.setColumns(99);
        dto.setRows(99);
        assertEquals("val-layoutType", dto.getLayoutType());
        assertEquals(99, dto.getColumns());
        assertEquals(99, dto.getRows());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoardDto.BoardLayoutDto dto1 = KPIBoardDto.BoardLayoutDto.builder()
                        .layoutType("test-layoutType")
            .columns(42)
            .rows(42)
            .items(Collections.emptyList())
            .build();
        KPIBoardDto.BoardLayoutDto dto2 = KPIBoardDto.BoardLayoutDto.builder()
                        .layoutType("test-layoutType")
            .columns(42)
            .rows(42)
            .items(Collections.emptyList())
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoardDto.BoardLayoutDto dto = KPIBoardDto.BoardLayoutDto.builder()
                        .layoutType("test-layoutType")
            .columns(42)
            .rows(42)
            .items(Collections.emptyList())
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}