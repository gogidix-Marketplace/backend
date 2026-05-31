package com.gogidix.shared.messaging.application.port.out;

import com.gogidix.shared.messaging.domain.model.Message;
import com.gogidix.shared.messaging.domain.valueobject.MessageType;
import com.gogidix.shared.messaging.domain.valueobject.MessagePriority;

import java.util.List;
import java.util.Map;

/**
 * Output port for message publishing operations.
 * Defines the contract for external messaging system integrations.
 */
public interface MessagePublisher {
    
    /**
     * Publishes a message to external messaging system
     */
    PublishResult publish(Message message);
    
    /**
     * Publishes multiple messages in batch
     */
    BatchPublishResult publishBatch(List<Message> messages);
    
    /**
     * Publishes message to specific topic
     */
    PublishResult publishToTopic(Message message, String topic);
    
    /**
     * Publishes message to specific queue
     */
    PublishResult publishToQueue(Message message, String queue);
    
    /**
     * Broadcasts message to all subscribers
     */
    BroadcastResult broadcast(Message message, List<String> recipients);
    
    /**
     * Schedules message for delayed delivery
     */
    ScheduleResult scheduleMessage(Message message, long delayMs);
    
    /**
     * Publishes event notification
     */
    PublishResult publishEvent(String eventType, Object eventData);
    
    /**
     * Publishes webhook message
     */
    WebhookResult publishWebhook(String url, Message message, Map<String, String> headers);
    
    /**
     * Publishes SMS message
     */
    SmsResult publishSms(String phoneNumber, String content);
    
    /**
     * Publishes email message
     */
    EmailResult publishEmail(String to, String subject, String content, String fromEmail);
    
    /**
     * Publishes push notification
     */
    PushResult publishPushNotification(String deviceToken, String title, String body);
    
    /**
     * Retries failed message
     */
    RetryResult retryMessage(Message message);
    
    /**
     * Cancels scheduled message
     */
    CancelResult cancelScheduledMessage(String messageId);
    
    /**
     * Gets publishing statistics
     */
    PublishingStatistics getStatistics();
    
    /**
     * Checks message delivery status
     */
    DeliveryStatus getDeliveryStatus(String messageId);
    
    /**
     * Basic publish result
     */
    class PublishResult {
        private final boolean success;
        private final String messageId;
        private final String externalId;
        private final String message;
        private final Exception error;
        private final long publishTimeMs;
        
        public PublishResult(boolean success, String messageId, String externalId, 
                           String message, Exception error, long publishTimeMs) {
            this.success = success;
            this.messageId = messageId;
            this.externalId = externalId;
            this.message = message;
            this.error = error;
            this.publishTimeMs = publishTimeMs;
        }
        
        public static PublishResult success(String messageId, String externalId, long publishTimeMs) {
            return new PublishResult(true, messageId, externalId, "Message published successfully", null, publishTimeMs);
        }
        
        public static PublishResult failure(String messageId, String message, Exception error) {
            return new PublishResult(false, messageId, null, message, error, 0L);
        }
        
        public boolean isSuccess() { return success; }
        public String getMessageId() { return messageId; }
        public String getExternalId() { return externalId; }
        public String getMessage() { return message; }
        public Exception getError() { return error; }
        public long getPublishTimeMs() { return publishTimeMs; }
    }
    
    /**
     * Batch publish result
     */
    class BatchPublishResult {
        private final int totalMessages;
        private final int successfulPublishes;
        private final int failedPublishes;
        private final List<PublishResult> results;
        private final long totalTimeMs;
        
        public BatchPublishResult(int totalMessages, int successfulPublishes, int failedPublishes,
                                List<PublishResult> results, long totalTimeMs) {
            this.totalMessages = totalMessages;
            this.successfulPublishes = successfulPublishes;
            this.failedPublishes = failedPublishes;
            this.results = results;
            this.totalTimeMs = totalTimeMs;
        }
        
        public int getTotalMessages() { return totalMessages; }
        public int getSuccessfulPublishes() { return successfulPublishes; }
        public int getFailedPublishes() { return failedPublishes; }
        public List<PublishResult> getResults() { return results; }
        public long getTotalTimeMs() { return totalTimeMs; }
        
        public double getSuccessRate() {
            return totalMessages > 0 ? (double) successfulPublishes / totalMessages : 0.0;
        }
    }
    
    /**
     * Broadcast result
     */
    class BroadcastResult {
        private final int totalRecipients;
        private final int successfulDeliveries;
        private final int failedDeliveries;
        private final List<String> failedRecipients;
        private final long broadcastTimeMs;
        
        public BroadcastResult(int totalRecipients, int successfulDeliveries, int failedDeliveries,
                             List<String> failedRecipients, long broadcastTimeMs) {
            this.totalRecipients = totalRecipients;
            this.successfulDeliveries = successfulDeliveries;
            this.failedDeliveries = failedDeliveries;
            this.failedRecipients = failedRecipients;
            this.broadcastTimeMs = broadcastTimeMs;
        }
        
        public int getTotalRecipients() { return totalRecipients; }
        public int getSuccessfulDeliveries() { return successfulDeliveries; }
        public int getFailedDeliveries() { return failedDeliveries; }
        public List<String> getFailedRecipients() { return failedRecipients; }
        public long getBroadcastTimeMs() { return broadcastTimeMs; }
        
        public double getDeliveryRate() {
            return totalRecipients > 0 ? (double) successfulDeliveries / totalRecipients : 0.0;
        }
    }
    
    /**
     * Schedule result
     */
    class ScheduleResult {
        private final boolean success;
        private final String scheduleId;
        private final long scheduledTime;
        private final String message;
        private final Exception error;
        
        public ScheduleResult(boolean success, String scheduleId, long scheduledTime, 
                            String message, Exception error) {
            this.success = success;
            this.scheduleId = scheduleId;
            this.scheduledTime = scheduledTime;
            this.message = message;
            this.error = error;
        }
        
        public boolean isSuccess() { return success; }
        public String getScheduleId() { return scheduleId; }
        public long getScheduledTime() { return scheduledTime; }
        public String getMessage() { return message; }
        public Exception getError() { return error; }
    }
    
    /**
     * Webhook result
     */
    class WebhookResult {
        private final boolean success;
        private final int httpStatus;
        private final String response;
        private final long responseTimeMs;
        private final String message;
        
        public WebhookResult(boolean success, int httpStatus, String response, 
                           long responseTimeMs, String message) {
            this.success = success;
            this.httpStatus = httpStatus;
            this.response = response;
            this.responseTimeMs = responseTimeMs;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public int getHttpStatus() { return httpStatus; }
        public String getResponse() { return response; }
        public long getResponseTimeMs() { return responseTimeMs; }
        public String getMessage() { return message; }
    }
    
    /**
     * SMS result
     */
    class SmsResult {
        private final boolean success;
        private final String smsId;
        private final String message;
        private final double cost;
        
        public SmsResult(boolean success, String smsId, String message, double cost) {
            this.success = success;
            this.smsId = smsId;
            this.message = message;
            this.cost = cost;
        }
        
        public boolean isSuccess() { return success; }
        public String getSmsId() { return smsId; }
        public String getMessage() { return message; }
        public double getCost() { return cost; }
    }
    
    /**
     * Email result
     */
    class EmailResult {
        private final boolean success;
        private final String emailId;
        private final String message;
        private final long queueTimeMs;
        
        public EmailResult(boolean success, String emailId, String message, long queueTimeMs) {
            this.success = success;
            this.emailId = emailId;
            this.message = message;
            this.queueTimeMs = queueTimeMs;
        }
        
        public boolean isSuccess() { return success; }
        public String getEmailId() { return emailId; }
        public String getMessage() { return message; }
        public long getQueueTimeMs() { return queueTimeMs; }
    }
    
    /**
     * Push notification result
     */
    class PushResult {
        private final boolean success;
        private final String pushId;
        private final String message;
        private final int recipientCount;
        
        public PushResult(boolean success, String pushId, String message, int recipientCount) {
            this.success = success;
            this.pushId = pushId;
            this.message = message;
            this.recipientCount = recipientCount;
        }
        
        public boolean isSuccess() { return success; }
        public String getPushId() { return pushId; }
        public String getMessage() { return message; }
        public int getRecipientCount() { return recipientCount; }
    }
    
    /**
     * Retry result
     */
    class RetryResult {
        private final boolean success;
        private final int attemptNumber;
        private final String message;
        private final long retryDelayMs;
        
        public RetryResult(boolean success, int attemptNumber, String message, long retryDelayMs) {
            this.success = success;
            this.attemptNumber = attemptNumber;
            this.message = message;
            this.retryDelayMs = retryDelayMs;
        }
        
        public boolean isSuccess() { return success; }
        public int getAttemptNumber() { return attemptNumber; }
        public String getMessage() { return message; }
        public long getRetryDelayMs() { return retryDelayMs; }
    }
    
    /**
     * Cancel result
     */
    class CancelResult {
        private final boolean success;
        private final String message;
        
        public CancelResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
    }
    
    /**
     * Publishing statistics
     */
    class PublishingStatistics {
        private final long totalMessagesPublished;
        private final long successfulPublishes;
        private final long failedPublishes;
        private final double averagePublishTimeMs;
        private final Map<MessageType, Long> messagesByType;
        private final Map<MessagePriority, Long> messagesByPriority;
        
        public PublishingStatistics(long totalMessagesPublished, long successfulPublishes, 
                                  long failedPublishes, double averagePublishTimeMs,
                                  Map<MessageType, Long> messagesByType, 
                                  Map<MessagePriority, Long> messagesByPriority) {
            this.totalMessagesPublished = totalMessagesPublished;
            this.successfulPublishes = successfulPublishes;
            this.failedPublishes = failedPublishes;
            this.averagePublishTimeMs = averagePublishTimeMs;
            this.messagesByType = messagesByType;
            this.messagesByPriority = messagesByPriority;
        }
        
        public long getTotalMessagesPublished() { return totalMessagesPublished; }
        public long getSuccessfulPublishes() { return successfulPublishes; }
        public long getFailedPublishes() { return failedPublishes; }
        public double getAveragePublishTimeMs() { return averagePublishTimeMs; }
        public Map<MessageType, Long> getMessagesByType() { return messagesByType; }
        public Map<MessagePriority, Long> getMessagesByPriority() { return messagesByPriority; }
        
        public double getSuccessRate() {
            return totalMessagesPublished > 0 ? 
                   (double) successfulPublishes / totalMessagesPublished : 0.0;
        }
    }
    
    /**
     * Delivery status
     */
    enum DeliveryStatus {
        PENDING("Pending delivery"),
        QUEUED("Queued for delivery"),
        DELIVERING("Currently delivering"),
        DELIVERED("Successfully delivered"),
        FAILED("Delivery failed"),
        CANCELLED("Delivery cancelled"),
        EXPIRED("Message expired");
        
        private final String description;
        
        DeliveryStatus(String description) {
            this.description = description;
        }
        
        public String getDescription() {
            return description;
        }
    }
}