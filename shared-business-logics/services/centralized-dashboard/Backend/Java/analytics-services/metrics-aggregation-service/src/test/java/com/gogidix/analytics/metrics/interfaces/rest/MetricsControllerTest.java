package com.gogidix.analytics.metrics.interfaces.rest;

import com.gogidix.analytics.metrics.application.service.MetricsCommandService;
import com.gogidix.analytics.metrics.application.service.MetricsQueryService;
import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import com.gogidix.analytics.metrics.domain.port.in.CreateMetricAlertCommand;
import com.gogidix.analytics.metrics.domain.port.in.IngestMetricCommand;
import com.gogidix.analytics.metrics.application.service.MetricsQueryService.MetricStatistics;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricsControllerTest {

    @Mock
    private MetricsCommandService commandService;

    @Mock
    private MetricsQueryService queryService;

    @InjectMocks
    private MetricsController controller;

    private IngestMetricCommand ingestCommand;
    private CreateMetricAlertCommand alertCommand;

    @BeforeEach
    void setUp() {
        ingestCommand = IngestMetricCommand.builder()
                .metricName("cpu")
                .metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85"))
                .sourceService("svc")
                .build();

        alertCommand = CreateMetricAlertCommand.builder()
                .alertName("High CPU")
                .metricName("cpu")
                .conditionType(MetricAlert.ConditionType.GREATER_THAN)
                .thresholdValue(new BigDecimal("90"))
                .build();
    }

    @Test
    void ingestMetric_returnsCreated() {
        MetricDataPoint dp = MetricDataPoint.builder().metricName("cpu").build();
        when(commandService.ingestMetric(ingestCommand)).thenReturn(dp);

        ResponseEntity<MetricDataPoint> response = controller.ingestMetric(ingestCommand);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(dp, response.getBody());
    }

    @Test
    void ingestMetricsBatch_returnsCreated() {
        List<MetricDataPoint> dps = List.of(MetricDataPoint.builder().metricName("cpu").build());
        when(commandService.ingestMetricsBatch(anyList())).thenReturn(dps);

        ResponseEntity<List<MetricDataPoint>> response = controller.ingestMetricsBatch(List.of(ingestCommand));

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void getMetrics_returnsOk() {
        Page<MetricDataPoint> page = new PageImpl<>(List.of());
        when(queryService.getMetrics(any(), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<MetricDataPoint>> response = controller.getMetrics(
                "cpu", null, LocalDateTime.now(), LocalDateTime.now(),
                null, null, Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getCurrentMetric_returnsOk() {
        MetricDataPoint dp = MetricDataPoint.builder().metricName("cpu").build();
        when(queryService.getCurrentMetricValue("cpu")).thenReturn(dp);

        ResponseEntity<MetricDataPoint> response = controller.getCurrentMetric("cpu");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dp, response.getBody());
    }

    @Test
    void getMetricStatistics_returnsOk() {
        MetricStatistics stats = new MetricStatistics("cpu", LocalDateTime.now(), LocalDateTime.now(),
                1L, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE);
        when(queryService.getMetricStatistics(eq("cpu"), any(), any())).thenReturn(stats);

        ResponseEntity<MetricStatistics> response = controller.getMetricStatistics(
                "cpu", LocalDateTime.now(), LocalDateTime.now());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(stats, response.getBody());
    }

    @Test
    void getAggregatedMetrics_returnsOk() {
        when(queryService.getAggregatedMetrics(eq("cpu"), any(), any(), any())).thenReturn(List.of());

        ResponseEntity<?> response = controller.getAggregatedMetrics(
                "cpu", "AVG", LocalDateTime.now(), LocalDateTime.now());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createAlert_returnsCreated() {
        MetricAlert alert = MetricAlert.builder().alertName("High CPU").build();
        when(commandService.createAlert(alertCommand)).thenReturn(alert);

        ResponseEntity<MetricAlert> response = controller.createAlert(alertCommand);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(alert, response.getBody());
    }

    @Test
    void getAlerts_returnsOk() {
        when(queryService.getAlerts()).thenReturn(List.of());

        ResponseEntity<List<MetricAlert>> response = controller.getAlerts();

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAlert_returnsOk() {
        MetricAlert alert = MetricAlert.builder().id("a1").build();
        when(queryService.getAlert("a1")).thenReturn(alert);

        ResponseEntity<MetricAlert> response = controller.getAlert("a1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(alert, response.getBody());
    }

    @Test
    void updateAlert_returnsOk() {
        MetricAlert alert = MetricAlert.builder().id("a1").build();
        when(commandService.updateAlert("a1", alertCommand)).thenReturn(alert);

        ResponseEntity<MetricAlert> response = controller.updateAlert("a1", alertCommand);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(alert, response.getBody());
    }

    @Test
    void deleteAlert_callsService() {
        controller.deleteAlert("a1");
        verify(commandService).deleteAlert("a1");
    }

    @Test
    void toggleAlert_returnsOk() {
        MetricAlert alert = MetricAlert.builder().id("a1").build();
        when(commandService.toggleAlert("a1", true)).thenReturn(alert);

        ResponseEntity<MetricAlert> response = controller.toggleAlert("a1", true);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(alert, response.getBody());
    }

    @Test
    void getMetrics_withSourceService_returnsOk() {
        Page<MetricDataPoint> page = new PageImpl<>(List.of());
        when(queryService.getMetrics(any(), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<MetricDataPoint>> response = controller.getMetrics(
                null, "order-service", LocalDateTime.now(), LocalDateTime.now(),
                null, null, Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMetrics_withAllParams_returnsOk() {
        Page<MetricDataPoint> page = new PageImpl<>(List.of());
        when(queryService.getMetrics(any(), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<MetricDataPoint>> response = controller.getMetrics(
                "cpu", "svc", LocalDateTime.now(), LocalDateTime.now(),
                "AVG", 300, Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getAggregatedMetrics_nullAggregationType_returnsOk() {
        when(queryService.getAggregatedMetrics(eq("cpu"), isNull(), any(), any())).thenReturn(List.of());

        ResponseEntity<?> response = controller.getAggregatedMetrics(
                "cpu", null, LocalDateTime.now(), LocalDateTime.now());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void toggleAlert_disable_returnsOk() {
        MetricAlert alert = MetricAlert.builder().id("a1").enabled(false).build();
        when(commandService.toggleAlert("a1", false)).thenReturn(alert);

        ResponseEntity<MetricAlert> response = controller.toggleAlert("a1", false);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(alert, response.getBody());
    }

    @Test
    void ingestMetric_withFullCommand_returnsCreated() {
        MetricDataPoint dp = MetricDataPoint.builder()
                .id(1L)
                .metricName("cpu")
                .metricValue(new BigDecimal("85"))
                .build();
        when(commandService.ingestMetric(ingestCommand)).thenReturn(dp);

        ResponseEntity<MetricDataPoint> response = controller.ingestMetric(ingestCommand);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void createAlert_withFullCommand_returnsCreated() {
        MetricAlert alert = MetricAlert.builder()
                .id("new-alert")
                .alertName("High CPU")
                .metricName("cpu")
                .build();
        when(commandService.createAlert(alertCommand)).thenReturn(alert);

        ResponseEntity<MetricAlert> response = controller.createAlert(alertCommand);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("new-alert", response.getBody().getId());
    }

    @Test
    void getMetrics_withNullMetricNameAndSourceService_returnsOk() {
        Page<MetricDataPoint> page = new PageImpl<>(List.of());
        when(queryService.getMetrics(any(), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<MetricDataPoint>> response = controller.getMetrics(
                null, null, LocalDateTime.now(), LocalDateTime.now(),
                null, null, Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMetrics_withAggregationParams_returnsOk() {
        Page<MetricDataPoint> page = new PageImpl<>(List.of());
        when(queryService.getMetrics(any(), any(Pageable.class))).thenReturn(page);

        ResponseEntity<Page<MetricDataPoint>> response = controller.getMetrics(
                "cpu", null, LocalDateTime.now(), LocalDateTime.now(),
                "SUM", 60, Pageable.unpaged());

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
