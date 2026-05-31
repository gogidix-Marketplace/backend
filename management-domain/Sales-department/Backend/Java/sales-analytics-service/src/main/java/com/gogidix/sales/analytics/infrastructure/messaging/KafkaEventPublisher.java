package com.gogidix.sales.analytics.infrastructure.messaging;

import com.gogidix.sales.analytics.domain.event.*;

public interface KafkaEventPublisher {

    void publishReportGenerated(ReportGeneratedEvent event);

    void publishMetricUpdated(MetricUpdatedEvent event);

    void publishSalesPerformanceUpdated(SalesPerformanceUpdatedEvent event);

    void publishPipelineUpdated(PipelineUpdatedEvent event);

    void publishWinLossAnalysis(WinLossAnalysisEvent event);
}
