package com.gogidix.transaction.monitoring.dto;

import com.gogidix.transaction.monitoring.domain.entity.Alert;
import com.gogidix.transaction.monitoring.domain.entity.TransactionMetrics;
import com.gogidix.transaction.monitoring.mapper.MonitoringMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DTO and Mapper Tests")
class DtoMapperTest {

    @Test
    @DisplayName("Should build AlertCreateRequest")
    void shouldBuildAlertCreateRequest() {
        AlertCreateRequest req = AlertCreateRequest.builder()
            .alertType(Alert.AlertType.SECURITY).severity(Alert.AlertSeverity.CRITICAL)
            .title("Breach").build();
        assertEquals("Breach", req.getTitle());
        assertEquals(Alert.AlertSeverity.CRITICAL, req.getSeverity());
    }

    @Test
    @DisplayName("Should build MetricCreateRequest")
    void shouldBuildMetricCreateRequest() {
        MetricCreateRequest req = MetricCreateRequest.builder()
            .transactionId(UUID.randomUUID()).metricType(TransactionMetrics.MetricType.THROUGHPUT)
            .metricName("tps").metricValue(BigDecimal.TEN).build();
        assertEquals("tps", req.getMetricName());
    }

    @Test
    @DisplayName("Should build MonitoringDashboard")
    void shouldBuildDashboard() {
        MonitoringDashboard d = MonitoringDashboard.builder()
            .openAlerts(5L).criticalAlerts(2L).warningAlerts(3L).build();
        assertEquals(5L, d.getOpenAlerts());
    }

    @Test
    @DisplayName("Should map MetricCreateRequest to entity")
    void shouldMapMetricToEntity() {
        UUID txId = UUID.randomUUID();
        MetricCreateRequest req = MetricCreateRequest.builder()
            .transactionId(txId).metricType(TransactionMetrics.MetricType.ERROR_RATE)
            .metricName("err").metricValue(BigDecimal.ONE).build();
        TransactionMetrics entity = MonitoringMapper.toMetricEntity(req);
        assertEquals(txId, entity.getTransactionId());
        assertEquals("err", entity.getMetricName());
    }

    @Test
    @DisplayName("Should map TransactionMetrics to response")
    void shouldMapMetricToResponse() {
        TransactionMetrics entity = TransactionMetrics.builder()
            .metricName("resp").metricValue(BigDecimal.valueOf(100))
            .severity(TransactionMetrics.MetricSeverity.NORMAL).build();
        TransactionMetricsResponse res = MonitoringMapper.toMetricResponse(entity);
        assertEquals("resp", res.getMetricName());
    }

    @Test
    @DisplayName("Should map AlertCreateRequest to entity")
    void shouldMapAlertToEntity() {
        AlertCreateRequest req = AlertCreateRequest.builder()
            .alertType(Alert.AlertType.COMPLIANCE).severity(Alert.AlertSeverity.ERROR)
            .title("Violation").build();
        Alert entity = MonitoringMapper.toAlertEntity(req);
        assertEquals("Violation", entity.getTitle());
        assertEquals(Alert.AlertStatus.OPEN, entity.getStatus());
    }

    @Test
    @DisplayName("Should map Alert to response")
    void shouldMapAlertToResponse() {
        Alert entity = Alert.builder().title("T").severity(Alert.AlertSeverity.INFO)
            .status(Alert.AlertStatus.OPEN).build();
        AlertResponse res = MonitoringMapper.toAlertResponse(entity);
        assertEquals("T", res.getTitle());
    }
}
