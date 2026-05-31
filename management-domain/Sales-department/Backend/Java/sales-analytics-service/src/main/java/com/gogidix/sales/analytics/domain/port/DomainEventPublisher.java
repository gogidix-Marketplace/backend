package com.gogidix.sales.analytics.domain.port;

import com.gogidix.sales.analytics.domain.event.*;

public interface DomainEventPublisher {
    void publishAnalyticsReportCreated(AnalyticsReportCreatedEvent event);
    void publishAnalyticsReportUpdated(AnalyticsReportUpdatedEvent event);
    void publishAnalyticsReportDeleted(AnalyticsReportDeletedEvent event);
    void publishDashboardWidgetCreated(DashboardWidgetCreatedEvent event);
    void publishDashboardWidgetUpdated(DashboardWidgetUpdatedEvent event);
    void publishDashboardWidgetDeleted(DashboardWidgetDeletedEvent event);
    void publishMetricCreated(MetricCreatedEvent event);
    void publishMetricDeleted(MetricDeletedEvent event);
    void publishPerformanceMetricCreated(PerformanceMetricCreatedEvent event);
    void publishPerformanceMetricUpdated(PerformanceMetricUpdatedEvent event);
    void publishPerformanceMetricDeleted(PerformanceMetricDeletedEvent event);
    void publishPipelineMetricCreated(PipelineMetricCreatedEvent event);
    void publishPipelineMetricUpdated(PipelineMetricUpdatedEvent event);
    void publishPipelineMetricDeleted(PipelineMetricDeletedEvent event);
    void publishSalesCycleMetricCreated(SalesCycleMetricCreatedEvent event);
    void publishSalesCycleMetricUpdated(SalesCycleMetricUpdatedEvent event);
    void publishSalesCycleMetricDeleted(SalesCycleMetricDeletedEvent event);
    void publishWinLossMetricCreated(WinLossMetricCreatedEvent event);
    void publishWinLossMetricUpdated(WinLossMetricUpdatedEvent event);
    void publishWinLossMetricDeleted(WinLossMetricDeletedEvent event);
    void publishReportGenerated(ReportGeneratedEvent event);
    void publishSalesPerformanceUpdated(SalesPerformanceUpdatedEvent event);
    void publishPipelineUpdated(PipelineUpdatedEvent event);
    void publishWinLossAnalysis(WinLossAnalysisEvent event);
}
