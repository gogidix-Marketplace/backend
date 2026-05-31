package com.gogidix.shared.messaging.adapter.out.messaging;

import com.gogidix.shared.messaging.application.port.out.MessagePublisher;
import com.gogidix.shared.messaging.domain.model.Message;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Kafka adapter for external messaging infrastructure
 * Implements the MessagePublisher port for Kafka integration
 * 
 * This is a proper hexagonal architecture adapter that:
 * - Implements domain port interface
 * - Contains no business logic
 * - Handles only technical concerns
 * - Translates between domain and infrastructure
 */
@Component
public class KafkaAdapter implements MessagePublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaAdapter.class);
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public KafkaAdapter(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    
    @Override
    public void publish(Message message) {
        try {
            String topic = message.getTopic() != null ? message.getTopic() : "default-topic";
            String routingKey = message.getRoutingKey();
            
            // Convert domain message to Kafka message
            KafkaMessage kafkaMessage = convertToKafkaMessage(message);
            
            kafkaTemplate.send(topic, routingKey, kafkaMessage);
            logger.info("Published message {} to Kafka topic {}", message.getMessageId(), topic);
            
        } catch (Exception e) {
            logger.error("Failed to publish message {} to Kafka", message.getMessageId(), e);
            throw new MessagePublishingException("Failed to publish message to Kafka", e);
        }
    }
    
    @Override
    public void publishBatch(java.util.List<Message> messages) {
        for (Message message : messages) {
            publish(message);
        }
    }
    
    private KafkaMessage convertToKafkaMessage(Message domainMessage) {
        return KafkaMessage.builder()
            .messageId(domainMessage.getMessageId())
            .correlationId(domainMessage.getCorrelationId())
            .content(domainMessage.getContent())
            .contentType(domainMessage.getContentType())
            .headers(domainMessage.getHeaders())
            .timestamp(domainMessage.getCreatedAt())
            .build();
    }
    
    // Internal Kafka message representation
    private static class KafkaMessage {
        private String messageId;
        private String correlationId;
        private String content;
        private String contentType;
        private java.util.Map<String, Object> headers;
        private java.time.LocalDateTime timestamp;
        
        // Builder pattern for clean construction
        public static Builder builder() {
            return new Builder();
        }
        
        public static class Builder {
            private KafkaMessage message = new KafkaMessage();
            
            public Builder messageId(String messageId) { message.messageId = messageId; return this; }
            public Builder correlationId(String correlationId) { message.correlationId = correlationId; return this; }
            public Builder content(String content) { message.content = content; return this; }
            public Builder contentType(String contentType) { message.contentType = contentType; return this; }
            public Builder headers(java.util.Map<String, Object> headers) { message.headers = headers; return this; }
            public Builder timestamp(java.time.LocalDateTime timestamp) { message.timestamp = timestamp; return this; }
            
            public KafkaMessage build() { return message; }
        }
        
        // Getters
        public String getMessageId() { return messageId; }
        public String getCorrelationId() { return correlationId; }
        public String getContent() { return content; }
        public String getContentType() { return contentType; }
        public java.util.Map<String, Object> getHeaders() { return headers; }
        public java.time.LocalDateTime getTimestamp() { return timestamp; }
    }
}

/**
 * Exception thrown when message publishing fails
 */
class MessagePublishingException extends RuntimeException {
    public MessagePublishingException(String message, Throwable cause) {
        super(message, cause);
    }
}
