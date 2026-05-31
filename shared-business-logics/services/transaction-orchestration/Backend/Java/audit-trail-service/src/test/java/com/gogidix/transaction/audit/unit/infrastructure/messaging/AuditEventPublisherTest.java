package com.gogidix.transaction.audit.unit.infrastructure.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.transaction.audit.domain.model.AuditLog;
import com.gogidix.transaction.audit.infrastructure.messaging.kafka.AuditEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AuditEventPublisher Unit Tests")
class AuditEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private AuditEventPublisher publisher;

    private AuditLog auditLog;
    private UUID auditLogId;
    private String serializedAuditLog;

    @BeforeEach
    void setUp() throws Exception {
        auditLogId = UUID.randomUUID();
        auditLog = AuditLog.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId("user-001")
                .actorType("USER")
                .severity("INFO")
                .status("SUCCESS")
                .timestamp(LocalDateTime.now())
                .description("Transaction created")
                .build();

        serializedAuditLog = "{\"id\":\"" + auditLogId + "\",\"entityType\":\"Transaction\"}";

        // Set the topic field using reflection or create a test configuration
        try {
            var field = AuditEventPublisher.class.getDeclaredField("auditEventsTopic");
            field.setAccessible(true);
            field.set(publisher, "audit-events");
        } catch (Exception e) {
            // Field will use default value
        }
    }

    @Test
    @DisplayName("Should publish audit event successfully")
    void shouldPublishAuditEventSuccessfully() throws Exception {
        // Given
        when(objectMapper.writeValueAsString(auditLog)).thenReturn(serializedAuditLog);

        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq("audit-events"), eq(auditLogId.toString()), eq(serializedAuditLog)))
                .thenReturn(future);

        // When
        publisher.publishAuditEvent(auditLog);

        // Then
        verify(objectMapper).writeValueAsString(auditLog);
        verify(kafkaTemplate).send(eq("audit-events"), eq(auditLogId.toString()), eq(serializedAuditLog));
    }

    @Test
    @DisplayName("Should log error when serialization fails")
    void shouldLogErrorWhenSerializationFails() throws Exception {
        // Given
        when(objectMapper.writeValueAsString(auditLog))
                .thenThrow(new com.fasterxml.jackson.core.JsonProcessingException("Serialization error") {});

        // When
        publisher.publishAuditEvent(auditLog);

        // Then
        verify(objectMapper).writeValueAsString(auditLog);
        verify(kafkaTemplate, never()).send(any(), any(), any());
    }

    @Test
    @DisplayName("Should use correct topic and key")
    void shouldUseCorrectTopicAndKey() throws Exception {
        // Given
        when(objectMapper.writeValueAsString(auditLog)).thenReturn(serializedAuditLog);

        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq("audit-events"), eq(auditLogId.toString()), eq(serializedAuditLog)))
                .thenReturn(future);

        // When
        publisher.publishAuditEvent(auditLog);

        // Then
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), valueCaptor.capture());

        assertEquals("audit-events", topicCaptor.getValue());
        assertEquals(auditLogId.toString(), keyCaptor.getValue());
        assertEquals(serializedAuditLog, valueCaptor.getValue());
    }

    @Test
    @DisplayName("Should handle null fields in audit log")
    void shouldHandleNullFieldsInAuditLog() throws Exception {
        // Given
        AuditLog auditLogWithNulls = AuditLog.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Transaction")
                .entityId("txn-12345")
                .action("CREATE")
                .actorId(null)
                .actorType(null)
                .severity(null)
                .status(null)
                .timestamp(null)
                .build();

        when(objectMapper.writeValueAsString(auditLogWithNulls)).thenReturn("{}");

        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq("audit-events"), eq(auditLogId.toString()), eq("{}")))
                .thenReturn(future);

        // When
        publisher.publishAuditEvent(auditLogWithNulls);

        // Then
        verify(objectMapper).writeValueAsString(auditLogWithNulls);
        verify(kafkaTemplate).send(eq("audit-events"), eq(auditLogId.toString()), eq("{}"));
    }

    @Test
    @DisplayName("Should handle critical audit events")
    void shouldHandleCriticalAuditEvents() throws Exception {
        // Given
        auditLog.setSeverity("CRITICAL");
        when(objectMapper.writeValueAsString(auditLog)).thenReturn(serializedAuditLog);

        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq("audit-events"), eq(auditLogId.toString()), eq(serializedAuditLog)))
                .thenReturn(future);

        // When
        publisher.publishAuditEvent(auditLog);

        // Then
        verify(kafkaTemplate).send(eq("audit-events"), eq(auditLogId.toString()), eq(serializedAuditLog));
    }

    @Test
    @DisplayName("Should handle failed audit events")
    void shouldHandleFailedAuditEvents() throws Exception {
        // Given
        auditLog.setStatus("FAILURE");
        auditLog.setErrorMessage("Payment processing failed");
        when(objectMapper.writeValueAsString(auditLog)).thenReturn(serializedAuditLog);

        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq("audit-events"), eq(auditLogId.toString()), eq(serializedAuditLog)))
                .thenReturn(future);

        // When
        publisher.publishAuditEvent(auditLog);

        // Then
        verify(kafkaTemplate).send(eq("audit-events"), eq(auditLogId.toString()), eq(serializedAuditLog));
    }

    @Test
    @DisplayName("Should handle system actor events")
    void shouldHandleSystemActorEvents() throws Exception {
        // Given
        auditLog.setActorType("SYSTEM");
        auditLog.setActorId("payment-service");
        when(objectMapper.writeValueAsString(auditLog)).thenReturn(serializedAuditLog);

        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq("audit-events"), eq(auditLogId.toString()), eq(serializedAuditLog)))
                .thenReturn(future);

        // When
        publisher.publishAuditEvent(auditLog);

        // Then
        verify(kafkaTemplate).send(eq("audit-events"), eq(auditLogId.toString()), eq(serializedAuditLog));
    }

    @Test
    @DisplayName("Should serialize audit log with all fields")
    void shouldSerializeAuditLogWithAllFields() throws Exception {
        // Given
        AuditLog fullAuditLog = AuditLog.builder()
                .id(auditLogId)
                .tenantId("tenant-001")
                .entityType("Payment")
                .entityId("pay-001")
                .action("PROCESS")
                .actorId("system")
                .actorType("SERVICE")
                .ipAddress("10.0.0.1")
                .userAgent("PaymentService/1.0")
                .correlationId("corr-001")
                .timestamp(LocalDateTime.now())
                .oldState("{\"status\":\"PENDING\"}")
                .newState("{\"status\":\"COMPLETED\"}")
                .changedFields("[\"status\"]")
                .businessContext("{\"source\":\"API\"}")
                .severity("INFO")
                .category("BUSINESS")
                .description("Payment processed")
                .status("SUCCESS")
                .sessionId("sess-001")
                .requestId("req-001")
                .build();

        String expectedJson = "{\"id\":\"" + auditLogId + "\",\"entityType\":\"Payment\"}";
        when(objectMapper.writeValueAsString(fullAuditLog)).thenReturn(expectedJson);

        @SuppressWarnings("unchecked")
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(eq("audit-events"), eq(auditLogId.toString()), eq(expectedJson)))
                .thenReturn(future);

        // When
        publisher.publishAuditEvent(fullAuditLog);

        // Then
        verify(objectMapper).writeValueAsString(fullAuditLog);
        verify(kafkaTemplate).send(eq("audit-events"), eq(auditLogId.toString()), eq(expectedJson));
    }
}
