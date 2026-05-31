package com.gogidix.finance.consolidation.domain.port;

import com.gogidix.finance.consolidation.domain.event.*;

public interface DomainEventPublisher {
    void publishConsolidatedBalanceCreated(ConsolidatedBalanceCreatedEvent event);
    void publishConsolidatedBalanceUpdated(ConsolidatedBalanceUpdatedEvent event);
    void publishConsolidatedBalanceDeleted(ConsolidatedBalanceDeletedEvent event);
    void publishConsolidationJobCreated(ConsolidationJobCreatedEvent event);
    void publishConsolidationJobUpdated(ConsolidationJobUpdatedEvent event);
    void publishConsolidationJobDeleted(ConsolidationJobDeletedEvent event);
    void publishConsolidationReportCreated(ConsolidationReportCreatedEvent event);
    void publishConsolidationReportUpdated(ConsolidationReportUpdatedEvent event);
    void publishConsolidationReportDeleted(ConsolidationReportDeletedEvent event);
    void publishConsolidationRuleCreated(ConsolidationRuleCreatedEvent event);
    void publishConsolidationRuleUpdated(ConsolidationRuleUpdatedEvent event);
    void publishConsolidationRuleDeleted(ConsolidationRuleDeletedEvent event);
}
