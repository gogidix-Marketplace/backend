package com.gogidix.hr.payroll.infrastructure.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.hr.payroll.domain.event.PayrollApprovedEvent;
import com.gogidix.hr.payroll.domain.event.PayrollCreatedEvent;
import com.gogidix.hr.payroll.domain.event.PayrollPaidEvent;
import com.gogidix.hr.payroll.domain.event.PayrollProcessedEvent;
import com.gogidix.hr.payroll.domain.event.PayslipGeneratedEvent;
import com.gogidix.hr.payroll.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka Event Publisher
 * Publishes domain events to Kafka topics
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.payroll-events:payroll-events}")
    private String payrollEventsTopic;

    @Value("${kafka.topic.payslip-events:payslip-events}")
    private String payslipEventsTopic;

    @Value("${kafka.enabled:true}")
    private boolean kafkaEnabled;

    @Override
    public void publishPayrollCreated(PayrollCreatedEvent event) {
        publish(payrollEventsTopic, "payroll-created", event);
    }

    @Override
    public void publishPayrollApproved(PayrollApprovedEvent event) {
        publish(payrollEventsTopic, "payroll-approved", event);
    }

    @Override
    public void publishPayrollProcessed(PayrollProcessedEvent event) {
        publish(payrollEventsTopic, "payroll-processed", event);
    }

    @Override
    public void publishPayrollPaid(PayrollPaidEvent event) {
        publish(payrollEventsTopic, "payroll-paid", event);
    }

    @Override
    public void publishPayslipGenerated(PayslipGeneratedEvent event) {
        publish(payslipEventsTopic, "payslip-generated", event);
    }

    @Override
    public boolean isReady() {
        return kafkaEnabled && kafkaTemplate != null;
    }

    private void publish(String topic, String key, Object event) {
        if (!isReady()) {
            log.debug("Kafka is disabled, skipping event publication: {}", event.getClass().getSimpleName());
            return;
        }

        try {
            String payload = objectMapper.writeValueAsString(event);

            CompletableFuture<SendResult<String, String>> future =
                    kafkaTemplate.send(topic, key, payload);

            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish event to topic {}: {}", topic, ex.getMessage());
                } else {
                    log.debug("Successfully published event to topic: {}, partition: {}",
                            topic, result.getRecordMetadata().partition());
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", e.getMessage());
        }
    }
}
