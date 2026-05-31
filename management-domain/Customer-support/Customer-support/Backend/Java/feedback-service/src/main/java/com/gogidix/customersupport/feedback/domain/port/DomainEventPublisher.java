package com.gogidix.customersupport.feedback.domain.port;

import com.gogidix.customersupport.feedback.domain.event.*;

public interface DomainEventPublisher {
    void publishCSATSurveyCreated(CSATSurveyCreatedEvent event);
    void publishCSATSurveyUpdated(CSATSurveyUpdatedEvent event);
    void publishCSATSurveyDeleted(CSATSurveyDeletedEvent event);
    void publishFeedbackCreated(FeedbackCreatedEvent event);
    void publishFeedbackUpdated(FeedbackUpdatedEvent event);
    void publishFeedbackDeleted(FeedbackDeletedEvent event);
    void publishNPSMetricCreated(NPSMetricCreatedEvent event);
    void publishNPSMetricUpdated(NPSMetricUpdatedEvent event);
    void publishNPSMetricDeleted(NPSMetricDeletedEvent event);
}
