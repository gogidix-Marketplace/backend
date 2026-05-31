package com.gogidix.management.executive.approval.domain.port;

import com.gogidix.management.executive.approval.domain.event.*;

public interface DomainEventPublisher {
    void publishApprovalCreated(ApprovalCreatedEvent event);
    void publishApprovalUpdated(ApprovalUpdatedEvent event);
    void publishApprovalDeleted(ApprovalDeletedEvent event);
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
