package com.gogidix.sales.analytics.infrastructure.messaging;

import com.gogidix.sales.analytics.domain.event.*;
import com.gogidix.sales.analytics.domain.port.DomainEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingEventPublisher implements DomainEventPublisher {

    @Override
    public void publishAnalyticsReportCreated(AnalyticsReportCreatedEvent event) {
        log.info("AnalyticsReport created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsReportUpdated(AnalyticsReportUpdatedEvent event) {
        log.info("AnalyticsReport updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishAnalyticsReportDeleted(AnalyticsReportDeletedEvent event) {
        log.info("AnalyticsReport deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDashboardWidgetCreated(DashboardWidgetCreatedEvent event) {
        log.info("DashboardWidget created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDashboardWidgetUpdated(DashboardWidgetUpdatedEvent event) {
        log.info("DashboardWidget updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishDashboardWidgetDeleted(DashboardWidgetDeletedEvent event) {
        log.info("DashboardWidget deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricCreated(MetricCreatedEvent event) {
        log.info("Metric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishMetricDeleted(MetricDeletedEvent event) {
        log.info("Metric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPerformanceMetricCreated(PerformanceMetricCreatedEvent event) {
        log.info("PerformanceMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPerformanceMetricUpdated(PerformanceMetricUpdatedEvent event) {
        log.info("PerformanceMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPerformanceMetricDeleted(PerformanceMetricDeletedEvent event) {
        log.info("PerformanceMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPipelineMetricCreated(PipelineMetricCreatedEvent event) {
        log.info("PipelineMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPipelineMetricUpdated(PipelineMetricUpdatedEvent event) {
        log.info("PipelineMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishPipelineMetricDeleted(PipelineMetricDeletedEvent event) {
        log.info("PipelineMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSalesCycleMetricCreated(SalesCycleMetricCreatedEvent event) {
        log.info("SalesCycleMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSalesCycleMetricUpdated(SalesCycleMetricUpdatedEvent event) {
        log.info("SalesCycleMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishSalesCycleMetricDeleted(SalesCycleMetricDeletedEvent event) {
        log.info("SalesCycleMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWinLossMetricCreated(WinLossMetricCreatedEvent event) {
        log.info("WinLossMetric created: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWinLossMetricUpdated(WinLossMetricUpdatedEvent event) {
        log.info("WinLossMetric updated: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishWinLossMetricDeleted(WinLossMetricDeletedEvent event) {
        log.info("WinLossMetric deleted: tenant={}, eventId={}", event.getTenantId(), event.getEventId());
    }
    @Override
    public void publishReportGenerated(ReportGeneratedEvent event) {
        log.info("ReportGenerated: tenant={}, reportId={}, eventType={}", event.getTenantId(), event.getReportId(), event.getEventType());
    }
    @Override
    public void publishSalesPerformanceUpdated(SalesPerformanceUpdatedEvent event) {
        log.info("SalesPerformanceUpdated: tenant={}, entityId={}, eventType={}", event.getTenantId(), event.getEntityId(), event.getEventType());
    }
    @Override
    public void publishPipelineUpdated(PipelineUpdatedEvent event) {
        log.info("PipelineUpdated: tenant={}, entityId={}, eventType={}", event.getTenantId(), event.getEntityId(), event.getEventType());
    }
    @Override
    public void publishWinLossAnalysis(WinLossAnalysisEvent event) {
        log.info("WinLossAnalysis: tenant={}, entityId={}, eventType={}", event.getTenantId(), event.getEntityId(), event.getEventType());
    }
}
