package com.gogidix.analytics.metrics.application.service;

import com.gogidix.analytics.metrics.domain.model.MetricAggregation;
import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import com.gogidix.analytics.metrics.domain.port.in.GetMetricsQuery;
import com.gogidix.analytics.metrics.domain.repository.MetricAggregationRepository;
import com.gogidix.analytics.metrics.domain.repository.MetricAlertRepository;
import com.gogidix.analytics.metrics.domain.repository.MetricDataPointRepository;
import com.gogidix.shared.exceptions.NotFoundException;
import com.gogidix.shared.exceptions.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import com.gogidix.shared.security.context.RequestContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricsQueryServiceTest {

    @Mock
    private MetricDataPointRepository metricRepository;

    @Mock
    private MetricAggregationRepository aggregationRepository;

    @Mock
    private MetricAlertRepository alertRepository;

    @InjectMocks
    private MetricsQueryService service;

    private static final String TENANT_ID = "tenant-1";

    private GetMetricsQuery baseQuery;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    @BeforeEach
    void setUp() {
        startTime = LocalDateTime.now().minusHours(1);
        endTime = LocalDateTime.now();
        baseQuery = GetMetricsQuery.builder()
                .startTime(startTime)
                .endTime(endTime)
                .build();
    }

    private MetricDataPoint createDataPoint(String name, BigDecimal value) {
        return MetricDataPoint.builder()
                .id(1L)
                .metricName(name)
                .metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(value)
                .sourceService("svc")
                .tenantId(TENANT_ID)
                .timestamp(endTime)
                .build();
    }

    @Test
    void getMetrics_withMetricName_returnsFilteredResults() {
        baseQuery.setMetricName("cpu.usage");

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndMetricNameAndTimestampBetween(
                    eq(TENANT_ID), eq("cpu.usage"), eq(startTime), eq(endTime)))
                    .thenReturn(List.of(createDataPoint("cpu.usage", new BigDecimal("85"))));

            Page<MetricDataPoint> result = service.getMetrics(baseQuery, PageRequest.of(0, 10));

            assertEquals(1, result.getTotalElements());
            assertEquals("cpu.usage", result.getContent().get(0).getMetricName());
        }
    }

    @Test
    void getMetrics_withSourceService_returnsFilteredResults() {
        baseQuery.setSourceService("order-service");

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndSourceServiceAndTimestampBetween(
                    eq(TENANT_ID), eq("order-service"), eq(startTime), eq(endTime)))
                    .thenReturn(List.of(createDataPoint("cpu", BigDecimal.ONE)));

            Page<MetricDataPoint> result = service.getMetrics(baseQuery, PageRequest.of(0, 10));

            assertEquals(1, result.getTotalElements());
        }
    }

    @Test
    void getMetrics_withNoFilters_returnsAllForTenant() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndTimestampBetween(eq(TENANT_ID), eq(startTime), eq(endTime)))
                    .thenReturn(List.of(createDataPoint("cpu", BigDecimal.ONE)));

            Page<MetricDataPoint> result = service.getMetrics(baseQuery, PageRequest.of(0, 10));

            assertEquals(1, result.getTotalElements());
        }
    }

    @Test
    void getMetrics_withPagination_worksCorrectly() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndTimestampBetween(eq(TENANT_ID), eq(startTime), eq(endTime)))
                    .thenReturn(List.of(
                            createDataPoint("m1", BigDecimal.ONE),
                            createDataPoint("m2", BigDecimal.TEN),
                            createDataPoint("m3", BigDecimal.ZERO)));

            Page<MetricDataPoint> page = service.getMetrics(baseQuery, PageRequest.of(0, 2));

            assertEquals(3, page.getTotalElements());
            assertEquals(2, page.getContent().size());
        }
    }

    @Test
    void getMetrics_noTenantId_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class,
                    () -> service.getMetrics(baseQuery, PageRequest.of(0, 10)));
        }
    }

    @Test
    void getAggregatedMetrics_returnsResults() {
        MetricAggregation agg = MetricAggregation.builder()
                .metricName("cpu").aggregationType(MetricAggregation.AggregationType.AVG).build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(aggregationRepository.findAggregatesForTimeRange(eq(TENANT_ID), eq("cpu"),
                    eq(MetricAggregation.AggregationType.AVG), eq(startTime), eq(endTime)))
                    .thenReturn(List.of(agg));

            List<MetricAggregation> result = service.getAggregatedMetrics("cpu", "AVG", startTime, endTime);

            assertEquals(1, result.size());
        }
    }

    @Test
    void getAggregatedMetrics_nullType_defaultsToAvg() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(aggregationRepository.findAggregatesForTimeRange(eq(TENANT_ID), eq("cpu"),
                    eq(MetricAggregation.AggregationType.AVG), eq(startTime), eq(endTime)))
                    .thenReturn(Collections.emptyList());

            service.getAggregatedMetrics("cpu", null, startTime, endTime);

            verify(aggregationRepository).findAggregatesForTimeRange(TENANT_ID, "cpu",
                    MetricAggregation.AggregationType.AVG, startTime, endTime);
        }
    }

    @Test
    void getAggregatedMetrics_noTenantId_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class,
                    () -> service.getAggregatedMetrics("cpu", "AVG", startTime, endTime));
        }
    }

    @Test
    void getCurrentMetricValue_returnsLatest() {
        MetricDataPoint dp = createDataPoint("cpu", new BigDecimal("95"));
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findFirstByTenantIdAndMetricNameOrderByTimestampDesc(TENANT_ID, "cpu"))
                    .thenReturn(Optional.of(dp));

            MetricDataPoint result = service.getCurrentMetricValue("cpu");
            assertEquals(dp, result);
        }
    }

    @Test
    void getCurrentMetricValue_notFound_throwsNotFoundException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findFirstByTenantIdAndMetricNameOrderByTimestampDesc(TENANT_ID, "missing"))
                    .thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.getCurrentMetricValue("missing"));
        }
    }

    @Test
    void getCurrentMetricValue_noTenantId_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getCurrentMetricValue("cpu"));
        }
    }

    @Test
    void getMetricStatistics_returnsCorrectStats() {
        List<MetricDataPoint> points = List.of(
                createDataPoint("cpu", new BigDecimal("10")),
                createDataPoint("cpu", new BigDecimal("20")),
                createDataPoint("cpu", new BigDecimal("30")));

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndMetricNameAndTimestampBetween(eq(TENANT_ID), eq("cpu"),
                    eq(startTime), eq(endTime))).thenReturn(points);

            var stats = service.getMetricStatistics("cpu", startTime, endTime);

            assertEquals(3L, stats.count());
            assertEquals(new BigDecimal("20.000000"), stats.average());
            assertEquals(new BigDecimal("10"), stats.minimum());
            assertEquals(new BigDecimal("30"), stats.maximum());
            assertEquals(new BigDecimal("60"), stats.sum());
        }
    }

    @Test
    void getMetricStatistics_emptyMetrics_returnsNullStats() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndMetricNameAndTimestampBetween(eq(TENANT_ID), eq("cpu"),
                    eq(startTime), eq(endTime))).thenReturn(Collections.emptyList());

            var stats = service.getMetricStatistics("cpu", startTime, endTime);

            assertEquals(0L, stats.count());
            assertNull(stats.average());
            assertNull(stats.minimum());
            assertNull(stats.maximum());
            assertNull(stats.sum());
        }
    }

    @Test
    void getMetricStatistics_noTenantId_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class,
                    () -> service.getMetricStatistics("cpu", startTime, endTime));
        }
    }

    @Test
    void getAlert_returnsAlert() {
        MetricAlert alert = MetricAlert.builder()
                .id("a1").alertName("High CPU").tenantId(TENANT_ID).build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(alert));

            MetricAlert result = service.getAlert("a1");
            assertEquals(alert, result);
        }
    }

    @Test
    void getAlert_wrongTenant_throwsValidationException() {
        MetricAlert alert = MetricAlert.builder().id("a1").tenantId("other-tenant").build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(alert));

            assertThrows(ValidationException.class, () -> service.getAlert("a1"));
        }
    }

    @Test
    void getAlert_notFound_throwsNotFoundException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("missing")).thenReturn(Optional.empty());

            assertThrows(NotFoundException.class, () -> service.getAlert("missing"));
        }
    }

    @Test
    void getAlert_noTenantId_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getAlert("a1"));
        }
    }

    @Test
    void getAlerts_returnsEnabledAlerts() {
        MetricAlert alert = MetricAlert.builder().id("a1").tenantId(TENANT_ID).enabled(true).build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findByTenantIdAndEnabledTrue(TENANT_ID)).thenReturn(List.of(alert));

            List<MetricAlert> result = service.getAlerts();
            assertEquals(1, result.size());
        }
    }

    @Test
    void getAlerts_noTenantId_throwsValidationException() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(null);

            assertThrows(ValidationException.class, () -> service.getAlerts());
        }
    }

    @Test
    void getMetrics_withMetricNameAndSourceService_usesMetricNameBranch() {
        baseQuery.setMetricName("cpu.usage");
        baseQuery.setSourceService("order-service");

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndMetricNameAndTimestampBetween(
                    eq(TENANT_ID), eq("cpu.usage"), eq(startTime), eq(endTime)))
                    .thenReturn(List.of(createDataPoint("cpu.usage", new BigDecimal("85"))));

            Page<MetricDataPoint> result = service.getMetrics(baseQuery, PageRequest.of(0, 10));

            assertEquals(1, result.getTotalElements());
            verify(metricRepository).findByTenantIdAndMetricNameAndTimestampBetween(
                    eq(TENANT_ID), eq("cpu.usage"), eq(startTime), eq(endTime));
            verify(metricRepository, never()).findByTenantIdAndSourceServiceAndTimestampBetween(
                    any(), any(), any(), any());
        }
    }

    @Test
    void getMetrics_withEmptyResults_returnsEmptyPage() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndTimestampBetween(eq(TENANT_ID), eq(startTime), eq(endTime)))
                    .thenReturn(Collections.emptyList());

            Page<MetricDataPoint> result = service.getMetrics(baseQuery, PageRequest.of(0, 10));

            assertEquals(0, result.getTotalElements());
            assertTrue(result.getContent().isEmpty());
        }
    }

    @Test
    void getMetrics_withSecondPage_returnsCorrectSlice() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndTimestampBetween(eq(TENANT_ID), eq(startTime), eq(endTime)))
                    .thenReturn(List.of(
                            createDataPoint("m1", BigDecimal.ONE),
                            createDataPoint("m2", BigDecimal.TEN),
                            createDataPoint("m3", BigDecimal.ZERO)));

            Page<MetricDataPoint> page = service.getMetrics(baseQuery, PageRequest.of(1, 2));

            assertEquals(3, page.getTotalElements());
            assertEquals(1, page.getContent().size());
            assertEquals("m3", page.getContent().get(0).getMetricName());
        }
    }

    @Test
    void getAggregatedMetrics_withSumType_returnsResults() {
        MetricAggregation agg = MetricAggregation.builder()
                .metricName("cpu").aggregationType(MetricAggregation.AggregationType.SUM).build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(aggregationRepository.findAggregatesForTimeRange(eq(TENANT_ID), eq("cpu"),
                    eq(MetricAggregation.AggregationType.SUM), eq(startTime), eq(endTime)))
                    .thenReturn(List.of(agg));

            List<MetricAggregation> result = service.getAggregatedMetrics("cpu", "SUM", startTime, endTime);

            assertEquals(1, result.size());
            assertEquals(MetricAggregation.AggregationType.SUM, result.get(0).getAggregationType());
        }
    }

    @Test
    void getAggregatedMetrics_withCaseInsensitiveType() {
        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(aggregationRepository.findAggregatesForTimeRange(eq(TENANT_ID), eq("cpu"),
                    eq(MetricAggregation.AggregationType.MAX), eq(startTime), eq(endTime)))
                    .thenReturn(Collections.emptyList());

            service.getAggregatedMetrics("cpu", "max", startTime, endTime);

            verify(aggregationRepository).findAggregatesForTimeRange(TENANT_ID, "cpu",
                    MetricAggregation.AggregationType.MAX, startTime, endTime);
        }
    }

    @Test
    void getMetricStatistics_singleMetric_returnsCorrectStats() {
        List<MetricDataPoint> points = List.of(createDataPoint("cpu", new BigDecimal("42")));

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndMetricNameAndTimestampBetween(eq(TENANT_ID), eq("cpu"),
                    eq(startTime), eq(endTime))).thenReturn(points);

            var stats = service.getMetricStatistics("cpu", startTime, endTime);

            assertEquals(1L, stats.count());
            assertEquals(new BigDecimal("42.000000"), stats.average());
            assertEquals(new BigDecimal("42"), stats.minimum());
            assertEquals(new BigDecimal("42"), stats.maximum());
            assertEquals(new BigDecimal("42"), stats.sum());
        }
    }

    @Test
    void getAlerts_returnsMultipleAlerts() {
        MetricAlert alert1 = MetricAlert.builder().id("a1").tenantId(TENANT_ID).enabled(true).build();
        MetricAlert alert2 = MetricAlert.builder().id("a2").tenantId(TENANT_ID).enabled(true).build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findByTenantIdAndEnabledTrue(TENANT_ID)).thenReturn(List.of(alert1, alert2));

            List<MetricAlert> result = service.getAlerts();
            assertEquals(2, result.size());
        }
    }

    @Test
    void getAlert_correctTenant_returnsAlert() {
        MetricAlert alert = MetricAlert.builder()
                .id("a1").alertName("Test Alert").tenantId(TENANT_ID).metricName("cpu").build();

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(alertRepository.findById("a1")).thenReturn(Optional.of(alert));

            MetricAlert result = service.getAlert("a1");
            assertEquals("Test Alert", result.getAlertName());
        }
    }

    @Test
    void getMetrics_withMetricNameNullSourceService_usesMetricName() {
        baseQuery.setMetricName("mem.usage");
        baseQuery.setSourceService(null);

        try (MockedStatic<RequestContext> mocked = mockStatic(RequestContext.class)) {
            mocked.when(RequestContext::getTenantIdFromThreadLocal).thenReturn(TENANT_ID);
            when(metricRepository.findByTenantIdAndMetricNameAndTimestampBetween(
                    eq(TENANT_ID), eq("mem.usage"), eq(startTime), eq(endTime)))
                    .thenReturn(List.of(createDataPoint("mem.usage", new BigDecimal("75"))));

            Page<MetricDataPoint> result = service.getMetrics(baseQuery, PageRequest.of(0, 10));

            assertEquals(1, result.getTotalElements());
        }
    }
}
