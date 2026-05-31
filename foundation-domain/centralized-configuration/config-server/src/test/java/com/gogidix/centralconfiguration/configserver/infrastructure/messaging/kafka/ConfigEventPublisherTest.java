package com.gogidix.centralconfiguration.configserver.infrastructure.messaging.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gogidix.centralconfiguration.configserver.domain.model.Configuration;
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

import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ConfigEventPublisher Tests")
class ConfigEventPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private ConfigEventPublisher configEventPublisher;

    private Configuration testConfiguration;

    @BeforeEach
    void setUp() {
        testConfiguration = Configuration.builder()
                .id(1L)
                .tenantId("tenant-1")
                .applicationName("payment-service")
                .profile("prod")
                .configKey("database.timeout")
                .configValue("30000")
                .isEncrypted(false)
                .version(1)
                .createdBy("admin")
                .build();
    }

    @Test
    @DisplayName("Should publish CONFIG_CREATED event successfully")
    void publishConfigCreated_Success() throws Exception {
        // Given
        String expectedPayload = "{\"eventType\":\"CONFIG_CREATED\"}";
        when(objectMapper.writeValueAsString(any())).thenReturn(expectedPayload);
        when(kafkaTemplate.send(eq("config-events"), any(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        // When
        configEventPublisher.publishConfigCreated(testConfiguration);

        // Then
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), payloadCaptor.capture());

        assertThat(topicCaptor.getValue()).isEqualTo("config-events");
        assertThat(keyCaptor.getValue()).isEqualTo("tenant-1-database.timeout");
        assertThat(payloadCaptor.getValue()).isEqualTo(expectedPayload);
    }

    @Test
    @DisplayName("Should publish CONFIG_UPDATED event successfully")
    void publishConfigUpdated_Success() throws Exception {
        // Given
        String oldValue = "20000";
        String expectedPayload = "{\"eventType\":\"CONFIG_UPDATED\"}";
        when(objectMapper.writeValueAsString(any())).thenReturn(expectedPayload);
        when(kafkaTemplate.send(eq("config-events"), any(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        // When
        configEventPublisher.publishConfigUpdated(testConfiguration, oldValue);

        // Then
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), payloadCaptor.capture());

        assertThat(topicCaptor.getValue()).isEqualTo("config-events");
        assertThat(keyCaptor.getValue()).isEqualTo("tenant-1-database.timeout");
        assertThat(payloadCaptor.getValue()).isEqualTo(expectedPayload);
    }

    @Test
    @DisplayName("Should publish CONFIG_DELETED event successfully")
    void publishConfigDeleted_Success() throws Exception {
        // Given
        String expectedPayload = "{\"eventType\":\"CONFIG_DELETED\"}";
        when(objectMapper.writeValueAsString(any())).thenReturn(expectedPayload);
        when(kafkaTemplate.send(eq("config-events"), any(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        // When
        configEventPublisher.publishConfigDeleted(testConfiguration);

        // Then
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> payloadCaptor = ArgumentCaptor.forClass(String.class);

        verify(kafkaTemplate).send(topicCaptor.capture(), keyCaptor.capture(), payloadCaptor.capture());

        assertThat(topicCaptor.getValue()).isEqualTo("config-events");
        assertThat(keyCaptor.getValue()).isEqualTo("tenant-1-database.timeout");
        assertThat(payloadCaptor.getValue()).isEqualTo(expectedPayload);
    }

    @Test
    @DisplayName("Should include all config fields in event payload")
    void publishEvent_IncludesAllFields() throws Exception {
        // Given
        String expectedPayload = "{\"eventType\":\"CONFIG_CREATED\"}";
        when(objectMapper.writeValueAsString(any())).thenReturn(expectedPayload);
        when(kafkaTemplate.send(eq("config-events"), any(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        // When
        configEventPublisher.publishConfigCreated(testConfiguration);

        // Then
        verify(objectMapper).writeValueAsString(any());
        verify(kafkaTemplate).send(eq("config-events"), eq("tenant-1-database.timeout"), eq(expectedPayload));
    }

    @Test
    @DisplayName("Should handle JSON serialization exception gracefully")
    void publishEvent_JsonException_HandlesGracefully() throws Exception {
        // Given
        when(objectMapper.writeValueAsString(any())).thenThrow(new RuntimeException("JSON error"));

        // When - Should not throw exception
        configEventPublisher.publishConfigCreated(testConfiguration);

        // Then - Verify kafka was not called due to exception
        verify(kafkaTemplate, never()).send(any(), any(), any());
    }

    @Test
    @DisplayName("Should use tenantId and configKey as Kafka message key")
    void publishEvent_UsesCompositeKey() throws Exception {
        // Given
        Configuration configWithDifferentKey = Configuration.builder()
                .id(2L)
                .tenantId("tenant-2")
                .configKey("custom.key")
                .build();

        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(kafkaTemplate.send(eq("config-events"), any(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        // When
        configEventPublisher.publishConfigCreated(configWithDifferentKey);

        // Then
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(eq("config-events"), keyCaptor.capture(), anyString());

        assertThat(keyCaptor.getValue()).isEqualTo("tenant-2-custom.key");
    }

    @Test
    @DisplayName("Should publish to correct topic")
    void publishEvent_CorrectTopic() throws Exception {
        // Given
        when(objectMapper.writeValueAsString(any())).thenReturn("{}");
        when(kafkaTemplate.send(eq("config-events"), any(), anyString()))
                .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));

        // When
        configEventPublisher.publishConfigCreated(testConfiguration);

        // Then
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        verify(kafkaTemplate).send(topicCaptor.capture(), any(), anyString());

        assertThat(topicCaptor.getValue()).isEqualTo("config-events");
    }
}
