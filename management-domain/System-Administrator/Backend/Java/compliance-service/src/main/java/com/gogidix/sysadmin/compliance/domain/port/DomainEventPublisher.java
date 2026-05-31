package com.gogidix.sysadmin.compliance.domain.port;

import com.gogidix.sysadmin.compliance.domain.event.*;

public interface DomainEventPublisher {
    void publishComplianceReportCreated(ComplianceReportCreatedEvent event);
    void publishComplianceReportUpdated(ComplianceReportUpdatedEvent event);
    void publishComplianceReportDeleted(ComplianceReportDeletedEvent event);
}
