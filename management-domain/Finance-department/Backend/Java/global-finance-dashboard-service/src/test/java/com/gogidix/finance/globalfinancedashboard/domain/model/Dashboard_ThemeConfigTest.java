package com.gogidix.finance.globalfinancedashboard.domain.model;

import com.gogidix.finance.globalfinancedashboard.domain.model.Dashboard;
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
class Dashboard_ThemeConfigTest {

        @Test
    void testBuilder() {
        Dashboard.ThemeConfig dto = Dashboard.ThemeConfig.builder()
                        .themeName("test-themeName")
            .primaryColor("test-primaryColor")
            .secondaryColor("test-secondaryColor")
            .backgroundColor("test-backgroundColor")
            .textColor("test-textColor")
            .darkMode(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-themeName", dto.getThemeName());
        assertEquals("test-primaryColor", dto.getPrimaryColor());
        assertEquals("test-secondaryColor", dto.getSecondaryColor());
        assertEquals("test-backgroundColor", dto.getBackgroundColor());
        assertEquals("test-textColor", dto.getTextColor());
        assertTrue(dto.getDarkMode());
    }

    @Test
    void testSettersAndGetters() {
        Dashboard.ThemeConfig dto = new Dashboard.ThemeConfig();
        dto.setThemeName("val-themeName");
        dto.setPrimaryColor("val-primaryColor");
        dto.setSecondaryColor("val-secondaryColor");
        dto.setBackgroundColor("val-backgroundColor");
        dto.setTextColor("val-textColor");
        dto.setDarkMode(true);
        assertEquals("val-themeName", dto.getThemeName());
        assertEquals("val-primaryColor", dto.getPrimaryColor());
        assertEquals("val-secondaryColor", dto.getSecondaryColor());
        assertEquals("val-backgroundColor", dto.getBackgroundColor());
        assertEquals("val-textColor", dto.getTextColor());
        assertTrue(dto.getDarkMode());
    }

    @Test
    void testEqualsAndHashCode() {
        Dashboard.ThemeConfig dto1 = Dashboard.ThemeConfig.builder()
                        .themeName("test-themeName")
            .primaryColor("test-primaryColor")
            .secondaryColor("test-secondaryColor")
            .backgroundColor("test-backgroundColor")
            .textColor("test-textColor")
            .darkMode(true)
            .build();
        Dashboard.ThemeConfig dto2 = Dashboard.ThemeConfig.builder()
                        .themeName("test-themeName")
            .primaryColor("test-primaryColor")
            .secondaryColor("test-secondaryColor")
            .backgroundColor("test-backgroundColor")
            .textColor("test-textColor")
            .darkMode(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        Dashboard.ThemeConfig dto = Dashboard.ThemeConfig.builder()
                        .themeName("test-themeName")
            .primaryColor("test-primaryColor")
            .secondaryColor("test-secondaryColor")
            .backgroundColor("test-backgroundColor")
            .textColor("test-textColor")
            .darkMode(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}