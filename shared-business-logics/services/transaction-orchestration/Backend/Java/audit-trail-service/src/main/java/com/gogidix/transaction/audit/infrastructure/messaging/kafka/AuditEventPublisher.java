package com.gogidix.transaction.audit.infrastructure.messaging.kafka;

import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Kafka event publisher for audit events.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuditEventPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${kafka.topic.audit-events:audit-events}")
    private String auditEventsTopic;

    public void publishAuditEvent(AuditLog auditLog) {
        try {
            String message = objectMapper.writeValueAsString(auditLog);
            kafkaTemplate.send(auditEventsTopic, auditLog.getId().toString(), message);
            log.debug("Published audit event: id={}, entity={}, action={}",
                auditLog.getId(), auditLog.getEntityType(), auditLog.getAction());
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize audit event: {}", auditLog.getId(), e);
        }
    }
}
