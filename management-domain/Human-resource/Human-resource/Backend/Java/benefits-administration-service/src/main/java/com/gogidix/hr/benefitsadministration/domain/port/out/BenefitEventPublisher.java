package com.gogidix.hr.benefitsadministration.domain.port.out;

import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentCancelledEvent;
import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentCreatedEvent;
import com.gogidix.hr.benefitsadministration.domain.event.BenefitEnrollmentUpdatedEvent;

/**
 * Output port for publishing benefit events
 * Part of hexagonal architecture - secondary port
 */
public interface BenefitEventPublisher {

    /**
     * Publish benefit enrollment created event
     * @param event the enrollment created event
     */
    void publishEnrollmentCreated(BenefitEnrollmentCreatedEvent event);

    /**
     * Publish benefit enrollment updated event
     * @param event the enrollment updated event
     */
    void publishEnrollmentUpdated(BenefitEnrollmentUpdatedEvent event);

    /**
     * Publish benefit enrollment cancelled event
     * @param event the enrollment cancelled event
     */
    void publishEnrollmentCancelled(BenefitEnrollmentCancelledEvent event);

    /**
     * Publish event to a specific topic
     * @param topic the topic name
     * @param event the event object
     * @param key the event key
     */
    void publish(String topic, Object event, String key);
}
