package com.gogidix.globalbusinessmanagement.businessintelligence.domain.port;

import com.gogidix.globalbusinessmanagement.businessintelligence.domain.event.*;

public interface DomainEventPublisher {
    void publishBIReportCreated(BIReportCreatedEvent event);
    void publishBIReportUpdated(BIReportUpdatedEvent event);
    void publishBIReportDeleted(BIReportDeletedEvent event);
    void publishForecastCreated(ForecastCreatedEvent event);
    void publishForecastUpdated(ForecastUpdatedEvent event);
    void publishForecastDeleted(ForecastDeletedEvent event);
    void publishInsightCreated(InsightCreatedEvent event);
    void publishInsightUpdated(InsightUpdatedEvent event);
    void publishInsightDeleted(InsightDeletedEvent event);
    void publishTrendAnalysisCreated(TrendAnalysisCreatedEvent event);
    void publishTrendAnalysisUpdated(TrendAnalysisUpdatedEvent event);
    void publishTrendAnalysisDeleted(TrendAnalysisDeletedEvent event);
}
