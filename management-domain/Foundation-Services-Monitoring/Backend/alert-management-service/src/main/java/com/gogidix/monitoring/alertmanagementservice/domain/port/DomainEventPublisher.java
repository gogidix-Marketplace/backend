package com.gogidix.monitoring.alertmanagementservice.domain.port;

import com.gogidix.monitoring.alertmanagementservice.domain.event.*;

public interface DomainEventPublisher {
    void publishAlertCreated(AlertCreatedEvent event);
    void publishAlertUpdated(AlertUpdatedEvent event);
    void publishAlertDeleted(AlertDeletedEvent event);
    void publishAlertHistoryCreated(AlertHistoryCreatedEvent event);
    void publishAlertHistoryUpdated(AlertHistoryUpdatedEvent event);
    void publishAlertHistoryDeleted(AlertHistoryDeletedEvent event);
    void publishAlertRuleCreated(AlertRuleCreatedEvent event);
    void publishAlertRuleUpdated(AlertRuleUpdatedEvent event);
    void publishAlertRuleDeleted(AlertRuleDeletedEvent event);
}
