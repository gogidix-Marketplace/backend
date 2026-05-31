package com.gogidix.shared.messaging.adapter.out.messaging;

import com.gogidix.shared.messaging.application.port.out.MessagePublisher;
import com.gogidix.shared.messaging.domain.model.Message;
import com.gogidix.shared.messaging.domain.valueobject.MessageType;
import com.gogidix.shared.messaging.domain.valueobject.MessagePriority;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Kafka-based implementation of MessagePublisher.
 * Handles message publishing to various external systems.
 */
@Component
public class KafkaMessagePublisher implements MessagePublisher {
    
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    
    // Statistics tracking
    private final AtomicLong totalMessagesPublished = new AtomicLong(0);
    private final AtomicLong successfulPublishes = new AtomicLong(0);
    private final AtomicLong failedPublishes = new AtomicLong(0);
    private final Map<MessageType, AtomicLong> messagesByType = new HashMap<>();
    private final Map<MessagePriority, AtomicLong> messagesByPriority = new HashMap<>();
    
    // Configuration
    private static final String DEFAULT_TOPIC = "messages.general";
    private static final String HIGH_PRIORITY_TOPIC = "messages.priority";
    private static final String NOTIFICATION_TOPIC = "messages.notifications";
    
    public KafkaMessagePublisher(KafkaTemplate<String, String> kafkaTemplate, 
                                RestTemplate restTemplate, 
                                ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        
        // Initialize counters for all enum values
        for (MessageType type : MessageType.values()) {
            messagesByType.put(type, new AtomicLong(0));
        }
        for (MessagePriority priority : MessagePriority.values()) {
            messagesByPriority.put(priority, new AtomicLong(0));
        }
    }
    
    @Override
    public PublishResult publish(Message message) {
        long startTime = System.currentTimeMillis();
        
        try {
            totalMessagesPublished.incrementAndGet();
            messagesByType.get(message.getType()).incrementAndGet();
            messagesByPriority.get(message.getPriority()).incrementAndGet();
            
            String topic = determineTopicForMessage(message);
            String messageJson = objectMapper.writeValueAsString(createKafkaMessage(message));
            
            CompletableFuture<SendResult<String, String>> future = 
                kafkaTemplate.send(topic, message.getMessageId(), messageJson);
            
            SendResult<String, String> result = future.get(); // Wait for completion
            
            long publishTime = System.currentTimeMillis() - startTime;
            successfulPublishes.incrementAndGet();
            
            return PublishResult.success(message.getMessageId(), 
                                       result.getRecordMetadata().toString(), 
                                       publishTime);
            
        } catch (Exception e) {
            failedPublishes.incrementAndGet();
            return PublishResult.failure(message.getMessageId(), 
                                       "Kafka publish failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public BatchPublishResult publishBatch(List<Message> messages) {
        long startTime = System.currentTimeMillis();
        List<PublishResult> results = new ArrayList<>();
        int successful = 0;
        int failed = 0;
        
        for (Message message : messages) {
            PublishResult result = publish(message);
            results.add(result);
            
            if (result.isSuccess()) {
                successful++;
            } else {
                failed++;
            }
        }
        
        long totalTime = System.currentTimeMillis() - startTime;
        return new BatchPublishResult(messages.size(), successful, failed, results, totalTime);
    }
    
    @Override
    public PublishResult publishToTopic(Message message, String topic) {
        long startTime = System.currentTimeMillis();
        
        try {
            String messageJson = objectMapper.writeValueAsString(createKafkaMessage(message));
            CompletableFuture<SendResult<String, String>> future = 
                kafkaTemplate.send(topic, message.getMessageId(), messageJson);
            
            SendResult<String, String> result = future.get();
            long publishTime = System.currentTimeMillis() - startTime;
            
            return PublishResult.success(message.getMessageId(),
                                       result.getRecordMetadata().toString(),
                                       publishTime);
            
        } catch (Exception e) {
            return PublishResult.failure(message.getMessageId(),
                                       "Topic publish failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public PublishResult publishToQueue(Message message, String queue) {
        // For Kafka, queues are implemented as topics with specific partitioning
        return publishToTopic(message, "queue." + queue);
    }
    
    @Override
    public BroadcastResult broadcast(Message message, List<String> recipients) {
        long startTime = System.currentTimeMillis();
        int successful = 0;
        List<String> failed = new ArrayList<>();
        
        for (String recipient : recipients) {
            try {
                Message individualMessage = message.withRecipient(recipient);
                PublishResult result = publish(individualMessage);
                
                if (result.isSuccess()) {
                    successful++;
                } else {
                    failed.add(recipient);
                }
            } catch (Exception e) {
                failed.add(recipient);
            }
        }
        
        long broadcastTime = System.currentTimeMillis() - startTime;
        return new BroadcastResult(recipients.size(), successful, failed.size(), 
                                 failed, broadcastTime);
    }
    
    @Override
    public ScheduleResult scheduleMessage(Message message, long delayMs) {
        try {
            // Create scheduled message payload
            ScheduledMessagePayload payload = new ScheduledMessagePayload(
                message.getMessageId(), 
                message, 
                System.currentTimeMillis() + delayMs
            );
            
            String payloadJson = objectMapper.writeValueAsString(payload);
            CompletableFuture<SendResult<String, String>> future = 
                kafkaTemplate.send("messages.scheduled", message.getMessageId(), payloadJson);
            
            SendResult<String, String> result = future.get();
            
            return new ScheduleResult(true, message.getMessageId(), 
                                    System.currentTimeMillis() + delayMs,
                                    "Message scheduled successfully", null);
            
        } catch (Exception e) {
            return new ScheduleResult(false, message.getMessageId(), 0L,
                                    "Scheduling failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public PublishResult publishEvent(String eventType, Object eventData) {
        try {
            EventPayload payload = new EventPayload(eventType, eventData, System.currentTimeMillis());
            String payloadJson = objectMapper.writeValueAsString(payload);
            
            CompletableFuture<SendResult<String, String>> future = 
                kafkaTemplate.send("events." + eventType.toLowerCase(), 
                                 UUID.randomUUID().toString(), payloadJson);
            
            SendResult<String, String> result = future.get();
            
            return PublishResult.success(UUID.randomUUID().toString(),
                                       result.getRecordMetadata().toString(), 0L);
            
        } catch (Exception e) {
            return PublishResult.failure(null, "Event publish failed: " + e.getMessage(), e);
        }
    }
    
    @Override
    public WebhookResult publishWebhook(String url, Message message, Map<String, String> headers) {
        long startTime = System.currentTimeMillis();
        
        try {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
            headers.forEach(httpHeaders::set);
            
            WebhookPayload payload = new WebhookPayload(message);
            String payloadJson = objectMapper.writeValueAsString(payload);
            
            HttpEntity<String> entity = new HttpEntity<>(payloadJson, httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(
                url, HttpMethod.POST, entity, String.class);
            
            long responseTime = System.currentTimeMillis() - startTime;
            
            return new WebhookResult(response.getStatusCode().is2xxSuccessful(),
                                   response.getStatusCode().value(),
                                   response.getBody(),
                                   responseTime,
                                   "Webhook delivered successfully");
            
        } catch (Exception e) {
            long responseTime = System.currentTimeMillis() - startTime;
            return new WebhookResult(false, 0, null, responseTime,
                                   "Webhook failed: " + e.getMessage());
        }
    }
    
    @Override
    public SmsResult publishSms(String phoneNumber, String content) {
        try {
            // Simulate SMS sending (in real implementation, integrate with SMS service)
            SmsPayload payload = new SmsPayload(phoneNumber, content, System.currentTimeMillis());
            String payloadJson = objectMapper.writeValueAsString(payload);
            
            CompletableFuture<SendResult<String, String>> future = 
                kafkaTemplate.send("messages.sms", phoneNumber, payloadJson);
            
            SendResult<String, String> result = future.get();
            
            return new SmsResult(true, UUID.randomUUID().toString(), 
                               "SMS queued for delivery", 0.05); // Mock cost
            
        } catch (Exception e) {
            return new SmsResult(false, null, "SMS send failed: " + e.getMessage(), 0.0);
        }
    }
    
    @Override
    public EmailResult publishEmail(String to, String subject, String content, String fromEmail) {
        long startTime = System.currentTimeMillis();
        
        try {
            EmailPayload payload = new EmailPayload(to, fromEmail, subject, content, 
                                                   System.currentTimeMillis());
            String payloadJson = objectMapper.writeValueAsString(payload);
            
            CompletableFuture<SendResult<String, String>> future = 
                kafkaTemplate.send("messages.email", to, payloadJson);
            
            SendResult<String, String> result = future.get();
            long queueTime = System.currentTimeMillis() - startTime;
            
            return new EmailResult(true, UUID.randomUUID().toString(),
                                 "Email queued for delivery", queueTime);
            
        } catch (Exception e) {
            return new EmailResult(false, null, "Email send failed: " + e.getMessage(), 0L);
        }
    }
    
    @Override
    public PushResult publishPushNotification(String deviceToken, String title, String body) {
        try {
            PushPayload payload = new PushPayload(deviceToken, title, body, 
                                                System.currentTimeMillis());
            String payloadJson = objectMapper.writeValueAsString(payload);
            
            CompletableFuture<SendResult<String, String>> future = 
                kafkaTemplate.send("messages.push", deviceToken, payloadJson);
            
            SendResult<String, String> result = future.get();
            
            return new PushResult(true, UUID.randomUUID().toString(),
                                "Push notification queued", 1); // Single recipient
            
        } catch (Exception e) {
            return new PushResult(false, null, "Push send failed: " + e.getMessage(), 0);
        }
    }
    
    @Override
    public RetryResult retryMessage(Message message) {
        if (!message.canRetry()) {
            return new RetryResult(false, message.getRetryCount(),
                                 "Message has exceeded maximum retry attempts", 0L);
        }
        
        try {
            Message retryMessage = message.incrementRetry();
            PublishResult result = publish(retryMessage);
            
            return new RetryResult(result.isSuccess(), retryMessage.getRetryCount(),
                                 result.getMessage(), 
                                 message.getPriority().getRetryIntervalMs());
            
        } catch (Exception e) {
            return new RetryResult(false, message.getRetryCount(),
                                 "Retry failed: " + e.getMessage(), 0L);
        }
    }
    
    @Override
    public CancelResult cancelScheduledMessage(String messageId) {
        try {
            // Send cancellation message to scheduled messages topic
            CancelPayload payload = new CancelPayload(messageId, System.currentTimeMillis());
            String payloadJson = objectMapper.writeValueAsString(payload);
            
            kafkaTemplate.send("messages.scheduled.cancel", messageId, payloadJson);
            
            return new CancelResult(true, "Message cancellation requested");
            
        } catch (Exception e) {
            return new CancelResult(false, "Cancellation failed: " + e.getMessage());
        }
    }
    
    @Override
    public PublishingStatistics getStatistics() {
        Map<MessageType, Long> typeStats = new HashMap<>();
        Map<MessagePriority, Long> priorityStats = new HashMap<>();
        
        messagesByType.forEach((type, count) -> typeStats.put(type, count.get()));
        messagesByPriority.forEach((priority, count) -> priorityStats.put(priority, count.get()));
        
        // Simple average calculation (in real implementation, track timing)
        double averagePublishTime = 100.0; // Mock value
        
        return new PublishingStatistics(
            totalMessagesPublished.get(),
            successfulPublishes.get(),
            failedPublishes.get(),
            averagePublishTime,
            typeStats,
            priorityStats
        );
    }
    
    @Override
    public DeliveryStatus getDeliveryStatus(String messageId) {
        // In real implementation, query delivery tracking system
        // For now, return mock status
        return DeliveryStatus.DELIVERED;
    }
    
    /**
     * Determines the appropriate Kafka topic based on message characteristics
     */
    private String determineTopicForMessage(Message message) {
        if (message.isHighPriority()) {
            return HIGH_PRIORITY_TOPIC;
        }
        
        if (message.getType().isNotification()) {
            return NOTIFICATION_TOPIC;
        }
        
        return DEFAULT_TOPIC;
    }
    
    /**
     * Creates Kafka message payload from domain message
     */
    private KafkaMessagePayload createKafkaMessage(Message message) {
        return new KafkaMessagePayload(
            message.getMessageId(),
            message.getType().name(),
            message.getPriority().name(),
            message.getSubject(),
            message.getContent(),
            message.getSender(),
            message.getRecipient(),
            message.getStatus().name(),
            System.currentTimeMillis(),
            message.getMetadata()
        );
    }
    
    // Payload classes for different message types
    public static class KafkaMessagePayload {
        public String messageId;
        public String type;
        public String priority;
        public String subject;
        public String content;
        public String sender;
        public String recipient;
        public String status;
        public long timestamp;
        public Map<String, Object> metadata;
        
        public KafkaMessagePayload(String messageId, String type, String priority, 
                                 String subject, String content, String sender, 
                                 String recipient, String status, long timestamp,
                                 Map<String, Object> metadata) {
            this.messageId = messageId;
            this.type = type;
            this.priority = priority;
            this.subject = subject;
            this.content = content;
            this.sender = sender;
            this.recipient = recipient;
            this.status = status;
            this.timestamp = timestamp;
            this.metadata = metadata;
        }
    }
    
    public static class ScheduledMessagePayload {
        public String scheduleId;
        public Message message;
        public long scheduledTime;
        
        public ScheduledMessagePayload(String scheduleId, Message message, long scheduledTime) {
            this.scheduleId = scheduleId;
            this.message = message;
            this.scheduledTime = scheduledTime;
        }
    }
    
    public static class EventPayload {
        public String eventType;
        public Object eventData;
        public long timestamp;
        
        public EventPayload(String eventType, Object eventData, long timestamp) {
            this.eventType = eventType;
            this.eventData = eventData;
            this.timestamp = timestamp;
        }
    }
    
    public static class WebhookPayload {
        public String messageId;
        public String type;
        public String subject;
        public String content;
        public String sender;
        public String recipient;
        public long timestamp;
        
        public WebhookPayload(Message message) {
            this.messageId = message.getMessageId();
            this.type = message.getType().name();
            this.subject = message.getSubject();
            this.content = message.getContent();
            this.sender = message.getSender();
            this.recipient = message.getRecipient();
            this.timestamp = System.currentTimeMillis();
        }
    }
    
    public static class SmsPayload {
        public String phoneNumber;
        public String content;
        public long timestamp;
        
        public SmsPayload(String phoneNumber, String content, long timestamp) {
            this.phoneNumber = phoneNumber;
            this.content = content;
            this.timestamp = timestamp;
        }
    }
    
    public static class EmailPayload {
        public String to;
        public String from;
        public String subject;
        public String content;
        public long timestamp;
        
        public EmailPayload(String to, String from, String subject, String content, long timestamp) {
            this.to = to;
            this.from = from;
            this.subject = subject;
            this.content = content;
            this.timestamp = timestamp;
        }
    }
    
    public static class PushPayload {
        public String deviceToken;
        public String title;
        public String body;
        public long timestamp;
        
        public PushPayload(String deviceToken, String title, String body, long timestamp) {
            this.deviceToken = deviceToken;
            this.title = title;
            this.body = body;
            this.timestamp = timestamp;
        }
    }
    
    public static class CancelPayload {
        public String messageId;
        public long timestamp;
        
        public CancelPayload(String messageId, long timestamp) {
            this.messageId = messageId;
            this.timestamp = timestamp;
        }
    }
}