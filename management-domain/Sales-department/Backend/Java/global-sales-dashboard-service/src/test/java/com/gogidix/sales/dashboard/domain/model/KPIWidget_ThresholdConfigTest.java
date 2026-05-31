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
class KPIWidget_ThresholdConfigTest {

        @Test
    void testBuilder() {
        KPIWidget.ThresholdConfig dto = KPIWidget.ThresholdConfig.builder()
                        .type(null)
            .thresholds(Collections.emptyList())
            .alertConfig("test-alertConfig")
            .enableAlerts(true)
            .build();
        assertNotNull(dto);
        assertEquals("test-alertConfig", dto.getAlertConfig());
        assertTrue(dto.getEnableAlerts());
    }

    @Test
    void testSettersAndGetters() {
        KPIWidget.ThresholdConfig dto = new KPIWidget.ThresholdConfig();
        dto.setAlertConfig("val-alertConfig");
        dto.setEnableAlerts(true);
        assertEquals("val-alertConfig", dto.getAlertConfig());
        assertTrue(dto.getEnableAlerts());
    }

    @Test
    void testEqualsAndHashCode() {
        KPIWidget.ThresholdConfig dto1 = KPIWidget.ThresholdConfig.builder()
                        .type(null)
            .thresholds(Collections.emptyList())
            .alertConfig("test-alertConfig")
            .enableAlerts(true)
            .build();
        KPIWidget.ThresholdConfig dto2 = KPIWidget.ThresholdConfig.builder()
                        .type(null)
            .thresholds(Collections.emptyList())
            .alertConfig("test-alertConfig")
            .enableAlerts(true)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        KPIWidget.ThresholdConfig dto = KPIWidget.ThresholdConfig.builder()
                        .type(null)
            .thresholds(Collections.emptyList())
            .alertConfig("test-alertConfig")
            .enableAlerts(true)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}