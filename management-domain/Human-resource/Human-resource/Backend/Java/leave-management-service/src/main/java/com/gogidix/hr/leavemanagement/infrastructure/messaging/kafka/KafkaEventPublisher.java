package com.gogidix.hr.leavemanagement.infrastructure.messaging.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.hr.leavemanagement.domain.event.LeaveBalanceUpdatedEvent;
import com.gogidix.hr.leavemanagement.domain.event.LeaveRequestApprovedEvent;
import com.gogidix.hr.leavemanagement.domain.event.LeaveRequestCancelledEvent;
import com.gogidix.hr.leavemanagement.domain.event.LeaveRequestCreatedEvent;
import com.gogidix.hr.leavemanagement.domain.event.LeaveRequestRejectedEvent;
import com.gogidix.hr.leavemanagement.domain.port.out.EventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class KafkaEventPublisher implements EventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.leave-events:leave-events}")
    private String leaveEventsTopic;

    @Value("${kafka.enabled:true}")
    private boolean kafkaEnabled;

    @Override
    public void publish(Object event) {
        publish(leaveEventsTopic, event);
    }

    @Override
    public void publish(String topic, Object event) {
        if (!isReady()) {
            log.debug("Kafka is disabled, skipping event publication: {}", event.getClass().getSimpleName());
            return;
        }

        try {
            String payload = objectMapper.writeValueAsString(event);
            String key = extractEventKey(event);
            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topic, key, payload);
            future.whenComplete((result, ex) -> {
                if (ex != null) {
                    log.error("Failed to publish event: {}", ex.getMessage());
                } else {
                    log.debug("Successfully published event to topic: {}", topic);
                }
            });
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize event: {}", e.getMessage());
        }
    }

    public void publishLeaveRequestCreated(LeaveRequestCreatedEvent event) {
        publish("leave-created", event);
    }

    public void publishLeaveRequestApproved(LeaveRequestApprovedEvent event) {
        publish("leave-approved", event);
    }

    public void publishLeaveRequestRejected(LeaveRequestRejectedEvent event) {
        publish("leave-rejected", event);
    }

    public void publishLeaveRequestCancelled(LeaveRequestCancelledEvent event) {
        publish("leave-cancelled", event);
    }

    public void publishLeaveBalanceUpdated(LeaveBalanceUpdatedEvent event) {
        publish("balance-updated", event);
    }

    public boolean isReady() {
        return kafkaEnabled && kafkaTemplate != null;
    }

    private String extractEventKey(Object event) {
        if (event instanceof LeaveRequestCreatedEvent) return "leave-created";
        if (event instanceof LeaveRequestApprovedEvent) return "leave-approved";
        if (event instanceof LeaveRequestRejectedEvent) return "leave-rejected";
        if (event instanceof LeaveRequestCancelledEvent) return "leave-cancelled";
        if (event instanceof LeaveBalanceUpdatedEvent) return "balance-updated";
        return event.getClass().getSimpleName();
    }
}
