package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.model.DashboardLayout;
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
class DashboardLayout_BreakpointTest {

        @Test
    void testBuilder() {
        DashboardLayout.Breakpoint dto = DashboardLayout.Breakpoint.builder()
                        .minWidth(42)
            .columns(42)
            .build();
        assertNotNull(dto);
        assertEquals(42, dto.getMinWidth());
        assertEquals(42, dto.getColumns());
    }

    @Test
    void testSettersAndGetters() {
        DashboardLayout.Breakpoint dto = new DashboardLayout.Breakpoint();
        dto.setMinWidth(99);
        dto.setColumns(99);
        assertEquals(99, dto.getMinWidth());
        assertEquals(99, dto.getColumns());
    }

    @Test
    void testEqualsAndHashCode() {
        DashboardLayout.Breakpoint dto1 = DashboardLayout.Breakpoint.builder()
                        .minWidth(42)
            .columns(42)
            .build();
        DashboardLayout.Breakpoint dto2 = DashboardLayout.Breakpoint.builder()
                        .minWidth(42)
            .columns(42)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardLayout.Breakpoint dto = DashboardLayout.Breakpoint.builder()
                        .minWidth(42)
            .columns(42)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}