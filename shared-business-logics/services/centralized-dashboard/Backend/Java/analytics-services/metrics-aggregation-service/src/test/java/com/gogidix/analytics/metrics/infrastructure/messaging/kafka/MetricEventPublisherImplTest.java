package com.gogidix.analytics.metrics.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.analytics.metrics.domain.model.MetricAlert;
import com.gogidix.analytics.metrics.domain.model.MetricDataPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MetricEventPublisherImplTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private MetricEventPublisherImpl publisher;

    private MetricDataPoint dataPoint;
    private MetricAlert alert;

    @BeforeEach
    void setUp() {
        dataPoint = MetricDataPoint.builder()
                .id(1L)
                .metricName("cpu.usage")
                .metricType(MetricDataPoint.MetricType.GAUGE)
                .metricValue(new BigDecimal("85.5"))
                .sourceService("order-service")
                .tenantId("tenant-1")
                .timestamp(LocalDateTime.now())
                .build();

        alert = MetricAlert.builder()
                .id("alert-1")
                .alertName("High CPU")
                .metricName("cpu.usage")
                .severity(MetricAlert.AlertSeverity.CRITICAL)
                .tenantId("tenant-1")
                .build();
    }

    @Test
    void publishMetricIngested_sendsToKafka() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"METRIC_INGESTED\"}");

        publisher.publishMetricIngested(dataPoint);

        verify(kafkaTemplate).send(eq("metrics.raw"), eq("cpu.usage"), anyString());
    }

    @Test
    void publishMetricIngested_handlesException() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("Serialization failed"));

        publisher.publishMetricIngested(dataPoint);

        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void publishMetricAggregated_sendsToKafka() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"METRIC_AGGREGATED\"}");

        publisher.publishMetricAggregated(dataPoint, "AVG");

        verify(kafkaTemplate).send(eq("metrics.aggregated"), eq("cpu.usage"), anyString());
    }

    @Test
    void publishMetricAggregated_handlesException() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("err"));

        publisher.publishMetricAggregated(dataPoint, "AVG");

        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void publishAlertTriggered_sendsToKafka() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"ALERT_TRIGGERED\"}");

        publisher.publishAlertTriggered(alert, "CPU too high");

        verify(kafkaTemplate).send(eq("metrics.alerts"), eq("alert-1"), anyString());
    }

    @Test
    void publishAlertTriggered_handlesException() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("err"));

        publisher.publishAlertTriggered(alert, "reason");

        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void publishAlertResolved_sendsToKafka() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"ALERT_RESOLVED\"}");

        publisher.publishAlertResolved(alert);

        verify(kafkaTemplate).send(eq("metrics.alerts"), eq("alert-1"), anyString());
    }

    @Test
    void publishAlertResolved_handlesException() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("err"));

        publisher.publishAlertResolved(alert);

        verify(kafkaTemplate, never()).send(anyString(), anyString(), anyString());
    }

    @Test
    void publishMetricIngested_sendsCorrectPayload() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"METRIC_INGESTED\"}");

        publisher.publishMetricIngested(dataPoint);

        verify(kafkaTemplate).send(eq("metrics.raw"), eq("cpu.usage"), eq("{\"eventType\":\"METRIC_INGESTED\"}"));
    }

    @Test
    void publishMetricAggregated_sendsCorrectPayload() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"METRIC_AGGREGATED\"}");

        publisher.publishMetricAggregated(dataPoint, "SUM");

        verify(kafkaTemplate).send(eq("metrics.aggregated"), eq("cpu.usage"), eq("{\"eventType\":\"METRIC_AGGREGATED\"}"));
    }

    @Test
    void publishAlertTriggered_sendsCorrectPayload() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"ALERT_TRIGGERED\"}");

        publisher.publishAlertTriggered(alert, "CPU exceeded threshold");

        verify(kafkaTemplate).send(eq("metrics.alerts"), eq("alert-1"), eq("{\"eventType\":\"ALERT_TRIGGERED\"}"));
    }

    @Test
    void publishAlertResolved_sendsCorrectPayload() throws Exception {
        when(objectMapper.writeValueAsString(any())).thenReturn("{\"eventType\":\"ALERT_RESOLVED\"}");

        publisher.publishAlertResolved(alert);

        verify(kafkaTemplate).send(eq("metrics.alerts"), eq("alert-1"), eq("{\"eventType\":\"ALERT_RESOLVED\"}"));
    }

    @Test
    void publishMetricIngested_withMinimalDataPoint() throws Exception {
        MetricDataPoint minimal = MetricDataPoint.builder()
                .id(2L)
                .metricName("mem")
                .metricType(MetricDataPoint.MetricType.COUNTER)
                .metricValue(BigDecimal.ONE)
                .sourceService("svc")
                .tenantId("t1")
                .timestamp(LocalDateTime.now())
                .build();

        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        publisher.publishMetricIngested(minimal);

        verify(kafkaTemplate).send(eq("metrics.raw"), eq("mem"), eq("{}"));
    }

    @Test
    void publishAlertTriggered_withMinimalAlert() throws Exception {
        MetricAlert minimal = MetricAlert.builder()
                .id("a2")
                .alertName("Test")
                .metricName("mem")
                .severity(MetricAlert.AlertSeverity.INFO)
                .tenantId("t1")
                .build();

        when(objectMapper.writeValueAsString(any())).thenReturn("{}");

        publisher.publishAlertTriggered(minimal, "test reason");

        verify(kafkaTemplate).send(eq("metrics.alerts"), eq("a2"), eq("{}"));
    }
}
