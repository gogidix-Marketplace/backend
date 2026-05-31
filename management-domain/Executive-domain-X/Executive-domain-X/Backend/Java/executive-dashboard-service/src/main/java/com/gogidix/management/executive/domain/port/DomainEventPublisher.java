package com.gogidix.management.executive.domain.port;

import com.gogidix.management.executive.domain.event.*;

public interface DomainEventPublisher {
    void publishAnalyticsDataCreated(AnalyticsDataCreatedEvent event);
    void publishAnalyticsDataUpdated(AnalyticsDataUpdatedEvent event);
    void publishAnalyticsDataDeleted(AnalyticsDataDeletedEvent event);
    void publishDashboardCreated(DashboardCreatedEvent event);
    void publishDashboardUpdated(DashboardUpdatedEvent event);
    void publishDashboardDeleted(DashboardDeletedEvent event);
    void publishDataFeedCreated(DataFeedCreatedEvent event);
    void publishDataFeedUpdated(DataFeedUpdatedEvent event);
    void publishDataFeedDeleted(DataFeedDeletedEvent event);
    void publishExecutiveSummaryCreated(ExecutiveSummaryCreatedEvent event);
    void publishExecutiveSummaryUpdated(ExecutiveSummaryUpdatedEvent event);
    void publishExecutiveSummaryDeleted(ExecutiveSummaryDeletedEvent event);
    void publishKpiWidgetCreated(KpiWidgetCreatedEvent event);
    void publishKpiWidgetUpdated(KpiWidgetUpdatedEvent event);
    void publishKpiWidgetDeleted(KpiWidgetDeletedEvent event);
    void publishPerformanceBenchmarkCreated(PerformanceBenchmarkCreatedEvent event);
    void publishPerformanceBenchmarkUpdated(PerformanceBenchmarkUpdatedEvent event);
    void publishPerformanceBenchmarkDeleted(PerformanceBenchmarkDeletedEvent event);
}
