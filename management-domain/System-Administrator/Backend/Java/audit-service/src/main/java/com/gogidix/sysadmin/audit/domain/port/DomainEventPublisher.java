package com.gogidix.sysadmin.audit.domain.port;

import com.gogidix.sysadmin.audit.domain.event.*;

public interface DomainEventPublisher {
    void publishAuditLogCreated(AuditLogCreatedEvent event);
    void publishAuditLogUpdated(AuditLogUpdatedEvent event);
    void publishAuditLogDeleted(AuditLogDeletedEvent event);
}
