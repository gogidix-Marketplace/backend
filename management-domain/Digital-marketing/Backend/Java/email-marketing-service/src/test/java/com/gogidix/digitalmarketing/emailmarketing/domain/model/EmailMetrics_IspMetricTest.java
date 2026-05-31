package com.gogidix.digitalmarketing.emailmarketing.domain.model;

import com.gogidix.digitalmarketing.emailmarketing.domain.model.EmailMetrics;
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
class EmailMetrics_IspMetricTest {

        @Test
    void testBuilder() {
        EmailMetrics.IspMetric dto = EmailMetrics.IspMetric.builder()
                        .isp("test-isp")
            .domain("test-domain")
            .delivered(42)
            .opened(42)
            .clicked(42)
            .openRate(null)
            .clickRate(null)
            .build();
        assertNotNull(dto);
        assertEquals("test-isp", dto.getIsp());
        assertEquals("test-domain", dto.getDomain());
        assertEquals(42, dto.getDelivered());
        assertEquals(42, dto.getOpened());
        assertEquals(42, dto.getClicked());
    }

    @Test
    void testSettersAndGetters() {
        EmailMetrics.IspMetric dto = new EmailMetrics.IspMetric();
        dto.setIsp("val-isp");
        dto.setDomain("val-domain");
        dto.setDelivered(99);
        dto.setOpened(99);
        dto.setClicked(99);
        assertEquals("val-isp", dto.getIsp());
        assertEquals("val-domain", dto.getDomain());
        assertEquals(99, dto.getDelivered());
        assertEquals(99, dto.getOpened());
        assertEquals(99, dto.getClicked());
    }

    @Test
    void testEqualsAndHashCode() {
        EmailMetrics.IspMetric dto1 = EmailMetrics.IspMetric.builder()
                        .isp("test-isp")
            .domain("test-domain")
            .delivered(42)
            .opened(42)
            .clicked(42)
            .openRate(null)
            .clickRate(null)
            .build();
        EmailMetrics.IspMetric dto2 = EmailMetrics.IspMetric.builder()
                        .isp("test-isp")
            .domain("test-domain")
            .delivered(42)
            .opened(42)
            .clicked(42)
            .openRate(null)
            .clickRate(null)
            .build();
        assertEquals(dto1, dto2);
        assertEquals(dto1.hashCode(), dto2.hashCode());
    }

    @Test
    void testToString() {
        EmailMetrics.IspMetric dto = EmailMetrics.IspMetric.builder()
                        .isp("test-isp")
            .domain("test-domain")
            .delivered(42)
            .opened(42)
            .clicked(42)
            .openRate(null)
            .clickRate(null)
            .build();
        String str = dto.toString();
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }

}