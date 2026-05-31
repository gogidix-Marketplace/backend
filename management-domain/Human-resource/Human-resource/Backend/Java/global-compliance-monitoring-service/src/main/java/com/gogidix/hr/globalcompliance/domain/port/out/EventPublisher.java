package com.gogidix.hr.globalcompliance.domain.port.out;

import com.gogidix.hr.globalcompliance.domain.event.*;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes a compliance requirement event
     */
    void publishRequirementEvent(ComplianceRequirementCreatedEvent event);

    /**
     * Publishes a compliance check event
     */
    void publishCheckEvent(ComplianceCheckCompletedEvent event);

    /**
     * Publishes a non-compliance issue created event
     */
    void publishIssueCreatedEvent(NonComplianceIssueCreatedEvent event);

    /**
     * Publishes a non-compliance issue resolved event
     */
    void publishIssueResolvedEvent(NonComplianceIssueResolvedEvent event);

    /**
     * Publishes a compliance report event
     */
    void publishReportEvent(ComplianceReportGeneratedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
