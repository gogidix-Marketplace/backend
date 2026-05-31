package com.gogidix.globalbusinessmanagement.datavalidation.domain.port;

import com.gogidix.globalbusinessmanagement.datavalidation.domain.event.*;

public interface DomainEventPublisher {
    void publishDataQualityReportCreated(DataQualityReportCreatedEvent event);
    void publishDataQualityReportUpdated(DataQualityReportUpdatedEvent event);
    void publishDataQualityReportDeleted(DataQualityReportDeletedEvent event);
    void publishValidationResultCreated(ValidationResultCreatedEvent event);
    void publishValidationResultUpdated(ValidationResultUpdatedEvent event);
    void publishValidationResultDeleted(ValidationResultDeletedEvent event);
    void publishValidationRuleCreated(ValidationRuleCreatedEvent event);
    void publishValidationRuleUpdated(ValidationRuleUpdatedEvent event);
    void publishValidationRuleDeleted(ValidationRuleDeletedEvent event);
}
