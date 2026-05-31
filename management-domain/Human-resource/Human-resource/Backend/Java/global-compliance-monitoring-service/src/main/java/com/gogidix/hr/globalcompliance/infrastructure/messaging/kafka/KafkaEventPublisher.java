package com.gogidix.hr.globalcompliance.infrastructure.messaging.kafka;

import com.gogidix.hr.globalcompliance.domain.event.*;
import com.gogidix.hr.globalcompliance.domain.port.out.EventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher Implementation
 * Publishes domain events to Kafka topics
 */
@Component
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    public KafkaEventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishRequirementEvent(ComplianceRequirementCreatedEvent event) {
        publish("compliance.requirement.created", event);
    }

    @Override
    public void publishCheckEvent(ComplianceCheckCompletedEvent event) {
        publish("compliance.check.completed", event);
    }

    @Override
    public void publishIssueCreatedEvent(NonComplianceIssueCreatedEvent event) {
        publish("compliance.issue.created", event);
    }

    @Override
    public void publishIssueResolvedEvent(NonComplianceIssueResolvedEvent event) {
        publish("compliance.issue.resolved", event);
    }

    @Override
    public void publishReportEvent(ComplianceReportGeneratedEvent event) {
        publish("compliance.report.generated", event);
    }

    @Override
    public void publishAll(List<Object> events) {
        for (Object event : events) {
            if (event instanceof ComplianceRequirementCreatedEvent) {
                publishRequirementEvent((ComplianceRequirementCreatedEvent) event);
            } else if (event instanceof ComplianceCheckCompletedEvent) {
                publishCheckEvent((ComplianceCheckCompletedEvent) event);
            } else if (event instanceof NonComplianceIssueCreatedEvent) {
                publishIssueCreatedEvent((NonComplianceIssueCreatedEvent) event);
            } else if (event instanceof NonComplianceIssueResolvedEvent) {
                publishIssueResolvedEvent((NonComplianceIssueResolvedEvent) event);
            } else if (event instanceof ComplianceReportGeneratedEvent) {
                publishReportEvent((ComplianceReportGeneratedEvent) event);
            }
        }
    }

    @Override
    public boolean isReady() {
        return kafkaTemplate != null;
    }

    private void publish(String topic, Object event) {
        try {
            CompletableFuture<SendResult<String, Object>> future =
                    kafkaTemplate.send(topic, event);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.debug("Published event to topic {}: {}", topic, event);
                } else {
                    log.error("Failed to publish event to topic {}: {}", topic, ex.getMessage());
                }
            });
        } catch (Exception e) {
            log.error("Error publishing event to topic {}: {}", topic, e.getMessage());
        }
    }
}
