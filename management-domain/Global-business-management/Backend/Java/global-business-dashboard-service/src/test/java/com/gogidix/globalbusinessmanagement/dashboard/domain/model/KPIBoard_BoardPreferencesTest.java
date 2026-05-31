package com.gogidix.globalbusinessmanagement.dashboard.domain.model;

import com.gogidix.globalbusinessmanagement.dashboard.domain.model.KPIBoard;
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
class KPIBoard_BoardPreferencesTest {

        @Test
    void testBuilder() {
        KPIBoard.BoardPreferences dto = KPIBoard.BoardPreferences.builder()
                        .theme("test-theme")
            .autoRefresh(true)
            .refreshInterval(42)
            .timeZone("test-timeZone")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .showAnnotations(true)
            .enableDrillDown(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-theme", dto.getTheme());
        assertTrue(dto.getAutoRefresh());
        assertEquals(42, dto.getRefreshInterval());
        assertEquals("test-timeZone", dto.getTimeZone());
        assertEquals("test-dateFormat", dto.getDateFormat());
        assertEquals("test-numberFormat", dto.getNumberFormat());
        assertTrue(dto.getShowAnnotations());
        assertTrue(dto.getEnableDrillDown());
    }

    @Test
    void testSettersAndGetters() {
        KPIBoard.BoardPreferences dto = new KPIBoard.BoardPreferences();
        dto.setTheme("val-theme");
        dto.setAutoRefresh(true);
        dto.setRefreshInterval(99);
        dto.setTimeZone("val-timeZone");
        dto.setDateFormat("val-dateFormat");
        dto.setNumberFormat("val-numberFormat");
        dto.setShowAnnotations(true);
        dto.setEnableDrillDown(true);
        assertEquals("val-theme", dto.getTheme());
        assertTrue(dto.getAutoRefresh());
        assertEquals(99, dto.getRefreshInterval());
        assertEquals("val-timeZone", dto.getTimeZone());
        assertEquals("val-dateFormat", dto.getDateFormat());
        assertEquals("val-numberFormat", dto.getNumberFormat());
        assertTrue(dto.getShowAnnotations());
        assertTrue(dto.getEnableDrillDown());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIBoard.BoardPreferences dto1 = KPIBoard.BoardPreferences.builder()
                        .theme("test-theme")
            .autoRefresh(true)
            .refreshInterval(42)
            .timeZone("test-timeZone")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .showAnnotations(true)
            .enableDrillDown(true)
            .build();
        KPIBoard.BoardPreferences dto2 = KPIBoard.BoardPreferences.builder()
                        .theme("test-theme")
            .autoRefresh(true)
            .refreshInterval(42)
            .timeZone("test-timeZone")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .showAnnotations(true)
            .enableDrillDown(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIBoard.BoardPreferences dto = KPIBoard.BoardPreferences.builder()
                        .theme("test-theme")
            .autoRefresh(true)
            .refreshInterval(42)
            .timeZone("test-timeZone")
            .dateFormat("test-dateFormat")
            .numberFormat("test-numberFormat")
            .showAnnotations(true)
            .enableDrillDown(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}