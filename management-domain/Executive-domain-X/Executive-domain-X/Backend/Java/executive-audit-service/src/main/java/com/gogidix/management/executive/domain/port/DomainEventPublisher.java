package com.gogidix.management.executive.domain.port;

import com.gogidix.management.executive.domain.event.*;

public interface DomainEventPublisher {
    void publishAuditCreated(AuditCreatedEvent event);
    void publishAuditUpdated(AuditUpdatedEvent event);
    void publishAuditDeleted(AuditDeletedEvent event);
    void publishDataFeedCreated(DataFeedCreatedEvent event);
    void publishDataFeedUpdated(DataFeedUpdatedEvent event);
    void publishDataFeedDeleted(DataFeedDeletedEvent event);
    void publishExecutiveSummaryCreated(ExecutiveSummaryCreatedEvent event);
    void publishExecutiveSummaryUpdated(ExecutiveSummaryUpdatedEvent event);
    void publishExecutiveSummaryDeleted(ExecutiveSummaryDeletedEvent event);
    void publishPerformanceBenchmarkCreated(PerformanceBenchmarkCreatedEvent event);
    void publishPerformanceBenchmarkUpdated(PerformanceBenchmarkUpdatedEvent event);
    void publishPerformanceBenchmarkDeleted(PerformanceBenchmarkDeletedEvent event);
}
