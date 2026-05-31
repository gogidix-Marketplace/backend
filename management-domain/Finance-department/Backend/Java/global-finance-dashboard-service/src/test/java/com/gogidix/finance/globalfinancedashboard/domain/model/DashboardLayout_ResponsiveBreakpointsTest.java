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
class DashboardLayout_ResponsiveBreakpointsTest {

        @Test
    void testBuilder() {
        DashboardLayout.ResponsiveBreakpoints dto = DashboardLayout.ResponsiveBreakpoints.builder()
                        .xs(null)
            .sm(null)
            .md(null)
            .lg(null)
            .xl(null)
            .xxl(null)
            .build();
        assertNotNull(dto);

    }

    @Test
    void testEqualsAndHashCode() {
        DashboardLayout.ResponsiveBreakpoints dto1 = DashboardLayout.ResponsiveBreakpoints.builder()
                        .xs(null)
            .sm(null)
            .md(null)
            .lg(null)
            .xl(null)
            .xxl(null)
            .build();
        DashboardLayout.ResponsiveBreakpoints dto2 = DashboardLayout.ResponsiveBreakpoints.builder()
                        .xs(null)
            .sm(null)
            .md(null)
            .lg(null)
            .xl(null)
            .xxl(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        DashboardLayout.ResponsiveBreakpoints dto = DashboardLayout.ResponsiveBreakpoints.builder()
                        .xs(null)
            .sm(null)
            .md(null)
            .lg(null)
            .xl(null)
            .xxl(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}