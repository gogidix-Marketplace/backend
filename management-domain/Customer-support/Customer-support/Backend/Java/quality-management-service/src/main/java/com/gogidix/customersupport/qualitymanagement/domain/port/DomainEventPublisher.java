package com.gogidix.customersupport.qualitymanagement.domain.port;

import com.gogidix.customersupport.qualitymanagement.domain.event.*;

public interface DomainEventPublisher {
    void publishAgentQualityProfileCreated(AgentQualityProfileCreatedEvent event);
    void publishAgentQualityProfileUpdated(AgentQualityProfileUpdatedEvent event);
    void publishAgentQualityProfileDeleted(AgentQualityProfileDeletedEvent event);
    void publishCalibrationSessionCreated(CalibrationSessionCreatedEvent event);
    void publishCalibrationSessionUpdated(CalibrationSessionUpdatedEvent event);
    void publishCalibrationSessionDeleted(CalibrationSessionDeletedEvent event);
    void publishQaReviewCreated(QaReviewCreatedEvent event);
    void publishQaReviewUpdated(QaReviewUpdatedEvent event);
    void publishQaReviewDeleted(QaReviewDeletedEvent event);
    void publishScorecardTemplateCreated(ScorecardTemplateCreatedEvent event);
    void publishScorecardTemplateUpdated(ScorecardTemplateUpdatedEvent event);
    void publishScorecardTemplateDeleted(ScorecardTemplateDeletedEvent event);
}
