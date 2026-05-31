package com.gogidix.sales.onboarding.infrastructure.messaging.kafka;

import com.gogidix.sales.onboarding.domain.event.DocumentUploadedEvent;
import com.gogidix.sales.onboarding.domain.event.OnboardingCompletedEvent;
import com.gogidix.sales.onboarding.domain.event.OnboardingStartedEvent;
import com.gogidix.sales.onboarding.domain.event.OnboardingStepCompletedEvent;
import com.gogidix.sales.onboarding.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher
 * Publishes onboarding events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.topics.onboarding-events:customer-onboarding-service.events}")
    private String onboardingEventsTopic;

    @Value("${spring.kafka.topics.domain-events:sales.domain-events}")
    private String domainEventsTopic;

    @Override
    public void publish(OnboardingStartedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getOnboardingId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(onboardingEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published onboarding started event: {} to topic: {}",
                            event.getEventId(), onboardingEventsTopic);
                } else {
                    log.error("Failed to publish onboarding started event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing onboarding started event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(OnboardingCompletedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getOnboardingId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(onboardingEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published onboarding completed event: {} to topic: {}",
                            event.getEventId(), onboardingEventsTopic);
                } else {
                    log.error("Failed to publish onboarding completed event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing onboarding completed event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(OnboardingStepCompletedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getOnboardingId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(onboardingEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published step completed event: {} to topic: {}",
                            event.getEventId(), onboardingEventsTopic);
                } else {
                    log.error("Failed to publish step completed event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing step completed event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publish(DocumentUploadedEvent event) {
        try {
            String key = event.getTenantId() + "-" + event.getOnboardingId();

            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(onboardingEventsTopic, key, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published document uploaded event: {} to topic: {}",
                            event.getEventId(), onboardingEventsTopic);
                } else {
                    log.error("Failed to publish document uploaded event: {}", event.getEventId(), ex);
                }
            });

        } catch (Exception e) {
            log.error("Error publishing document uploaded event: {}", event.getEventId(), e);
        }
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof OnboardingStartedEvent startedEvent) {
                publish(startedEvent);
            } else if (event instanceof OnboardingCompletedEvent completedEvent) {
                publish(completedEvent);
            } else if (event instanceof OnboardingStepCompletedEvent stepEvent) {
                publish(stepEvent);
            } else if (event instanceof DocumentUploadedEvent docEvent) {
                publish(docEvent);
            } else {
                log.warn("Unknown event type: {}", event.getClass().getSimpleName());
            }
        }
    }

    @Override
    public boolean isReady() {
        try {
            return kafkaTemplate != null;
        } catch (Exception e) {
            return false;
        }
    }
}
