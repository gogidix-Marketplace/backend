package com.gogidix.sysadmin.alertmanagement.domain.port;

import com.gogidix.sysadmin.alertmanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishAlertCreated(AlertCreatedEvent event);
    void publishAlertUpdated(AlertUpdatedEvent event);
    void publishAlertDeleted(AlertDeletedEvent event);
    void publishAlertRuleCreated(AlertRuleCreatedEvent event);
    void publishAlertRuleUpdated(AlertRuleUpdatedEvent event);
    void publishAlertRuleDeleted(AlertRuleDeletedEvent event);
}
