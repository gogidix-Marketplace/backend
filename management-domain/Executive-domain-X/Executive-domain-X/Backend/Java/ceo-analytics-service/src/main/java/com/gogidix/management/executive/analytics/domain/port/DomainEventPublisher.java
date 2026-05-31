package com.gogidix.management.executive.analytics.domain.port;

import com.gogidix.management.executive.analytics.domain.event.*;

public interface DomainEventPublisher {
    void publishAnalyticsCreated(AnalyticsCreatedEvent event);
    void publishAnalyticsUpdated(AnalyticsUpdatedEvent event);
    void publishAnalyticsDeleted(AnalyticsDeletedEvent event);
    void publishAnalyticsDataCreated(AnalyticsDataCreatedEvent event);
    void publishAnalyticsDataUpdated(AnalyticsDataUpdatedEvent event);
    void publishAnalyticsDataDeleted(AnalyticsDataDeletedEvent event);
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
