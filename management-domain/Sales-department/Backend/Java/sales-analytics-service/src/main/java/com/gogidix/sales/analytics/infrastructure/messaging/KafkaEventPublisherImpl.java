package com.gogidix.sales.analytics.infrastructure.messaging;

import com.gogidix.sales.analytics.domain.event.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaEventPublisherImpl implements KafkaEventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void publishReportGenerated(ReportGeneratedEvent event) {
        log.info("Publishing ReportGenerated event: reportId={}, tenant={}", event.getReportId(), event.getTenantId());
        kafkaTemplate.send("sales-analytics-report-generated", event.getTenantId(), event);
    }

    @Override
    public void publishMetricUpdated(MetricUpdatedEvent event) {
        log.info("Publishing MetricUpdated event: metricId={}, tenant={}", event.getMetricId(), event.getTenantId());
        kafkaTemplate.send("sales-analytics-metric-updated", event.getTenantId(), event);
    }

    @Override
    public void publishSalesPerformanceUpdated(SalesPerformanceUpdatedEvent event) {
        log.info("Publishing SalesPerformanceUpdated event: entityId={}, tenant={}", event.getEntityId(), event.getTenantId());
        kafkaTemplate.send("sales-analytics-performance-updated", event.getTenantId(), event);
    }

    @Override
    public void publishPipelineUpdated(PipelineUpdatedEvent event) {
        log.info("Publishing PipelineUpdated event: entityId={}, tenant={}", event.getEntityId(), event.getTenantId());
        kafkaTemplate.send("sales-analytics-pipeline-updated", event.getTenantId(), event);
    }

    @Override
    public void publishWinLossAnalysis(WinLossAnalysisEvent event) {
        log.info("Publishing WinLossAnalysis event: entityId={}, tenant={}", event.getEntityId(), event.getTenantId());
        kafkaTemplate.send("sales-analytics-win-loss-analysis", event.getTenantId(), event);
    }
}
