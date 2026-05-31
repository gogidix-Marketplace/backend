package com.gogidix.sales.onboarding.domain.port.out;

import com.gogidix.sales.onboarding.domain.event.DocumentUploadedEvent;
import com.gogidix.sales.onboarding.domain.event.OnboardingCompletedEvent;
import com.gogidix.sales.onboarding.domain.event.OnboardingStartedEvent;
import com.gogidix.sales.onboarding.domain.event.OnboardingStepCompletedEvent;

import java.util.List;

/**
 * Event Publisher Interface (Port)
 * Defines the contract for publishing domain events
 */
public interface EventPublisher {

    /**
     * Publishes an onboarding started event
     */
    void publish(OnboardingStartedEvent event);

    /**
     * Publishes an onboarding completed event
     */
    void publish(OnboardingCompletedEvent event);

    /**
     * Publishes an onboarding step completed event
     */
    void publish(OnboardingStepCompletedEvent event);

    /**
     * Publishes a document uploaded event
     */
    void publish(DocumentUploadedEvent event);

    /**
     * Publishes multiple events
     */
    void publishAll(List<Object> events);

    /**
     * Checks if the publisher is ready
     */
    boolean isReady();
}
