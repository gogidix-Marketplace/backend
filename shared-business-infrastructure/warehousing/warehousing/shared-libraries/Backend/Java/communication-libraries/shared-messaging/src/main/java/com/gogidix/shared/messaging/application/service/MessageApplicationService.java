package com.gogidix.shared.messaging.application.service;

import com.gogidix.shared.messaging.application.port.in.MessageHandlingUseCase;
import com.gogidix.shared.messaging.application.port.out.MessageRepository;
import com.gogidix.shared.messaging.application.port.out.MessagePublisher;
import com.gogidix.shared.messaging.domain.model.Message;
import com.gogidix.shared.messaging.domain.valueobject.MessageType;
import com.gogidix.shared.messaging.domain.valueobject.MessageStatus;
import com.gogidix.shared.messaging.domain.valueobject.MessagePriority;
import com.gogidix.shared.messaging.adapter.in.web.MessageController.MessageTemplate;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Application service implementing message handling use cases.
 * Orchestrates between domain services and infrastructure adapters.
 */
@Service
@Transactional
public class MessageApplicationService implements MessageHandlingUseCase {
    
    private final MessageRepository messageRepository;
    private final MessagePublisher messagePublisher;
    
    public MessageApplicationService(MessageRepository messageRepository, 
                                   MessagePublisher messagePublisher) {
        this.messageRepository = messageRepository;
        this.messagePublisher = messagePublisher;
    }
    
    @Override
    public MessageResult sendMessage(Message message) {
        try {
            // Validate message
            if (!message.isValid()) {
                return MessageResult.failure("Invalid message: missing required fields", 
                                           new IllegalArgumentException("Message validation failed"));
            }
            
            // Check if message is expired
            if (message.isExpired()) {
                return MessageResult.failure("Message has expired", 
                                           new IllegalStateException("Message expired"));
            }
            
            // Save message to repository
            Message savedMessage = messageRepository.save(message);
            
            // Publish message based on type
            MessagePublisher.PublishResult publishResult = publishMessageByType(savedMessage);
            
            if (publishResult.isSuccess()) {
                // Update message status to sent
                Message sentMessage = savedMessage.markAsSent();
                messageRepository.save(sentMessage);
                
                return MessageResult.success(sentMessage.getMessageId(), 
                                            "Message sent successfully");
            } else {
                // Mark as failed
                Message failedMessage = savedMessage.markAsFailed(publishResult.getMessage());
                messageRepository.save(failedMessage);
                
                return MessageResult.failure("Failed to send message: " + publishResult.getMessage(),
                                            publishResult.getError());
            }
            
        } catch (Exception e) {
            return MessageResult.failure("Unexpected error sending message: " + e.getMessage(), e);
        }
    }
    
    @Override
    public List<Message> getMessages(MessageFilter filter, int limit) {
        try {
            // Convert use case filter to repository filter
            MessageRepository.MessageFilter repoFilter = convertToRepositoryFilter(filter);
            return messageRepository.findWithFilter(repoFilter, Math.min(limit, 1000));
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
    
    @Override
    public Optional<Message> getMessage(String messageId) {
        try {
            return messageRepository.findById(messageId);
        } catch (Exception e) {
            return Optional.empty();
        }
    }
    
    @Override
    public MessageResult markAsRead(String messageId) {
        try {
            Optional<Message> messageOpt = messageRepository.findById(messageId);
            if (messageOpt.isEmpty()) {
                return MessageResult.failure("Message not found", 
                                           new IllegalArgumentException("Message ID not found: " + messageId));
            }
            
            Message message = messageOpt.get();
            Message readMessage = message.markAsRead();
            messageRepository.save(readMessage);
            
            return MessageResult.success(messageId, "Message marked as read");
            
        } catch (IllegalStateException e) {
            return MessageResult.failure("Cannot mark message as read: " + e.getMessage(), e);
        } catch (Exception e) {
            return MessageResult.failure("Error marking message as read: " + e.getMessage(), e);
        }
    }
    
    @Override
    public MessageResult deleteMessage(String messageId) {
        try {
            if (!messageRepository.existsById(messageId)) {
                return MessageResult.failure("Message not found", 
                                           new IllegalArgumentException("Message ID not found: " + messageId));
            }
            
            messageRepository.deleteById(messageId);
            return MessageResult.success(messageId, "Message deleted successfully");
            
        } catch (Exception e) {
            return MessageResult.failure("Error deleting message: " + e.getMessage(), e);
        }
    }
    
    @Override
    public MessageStatistics getMessageStatistics(String userId) {
        try {
            MessageRepository.MessageStatistics repoStats = messageRepository.getStatisticsForUser(userId);
            
            // Convert repository statistics to use case statistics
            Map<String, Long> messagesByType = new HashMap<>();
            Map<String, Long> messagesByPriority = new HashMap<>();
            Map<String, Long> messagesByStatus = new HashMap<>();
            
            // Get detailed breakdowns (simplified for this implementation)
            long totalMessages = repoStats.getTotalMessages();
            long unreadMessages = repoStats.getUnreadMessages();
            long sentMessages = repoStats.getSentMessages();
            double avgResponseTime = repoStats.getAverageResponseTimeMs();
            
            return new MessageStatistics(totalMessages, unreadMessages, sentMessages,
                                       messagesByType, messagesByPriority, messagesByStatus,
                                       avgResponseTime);
            
        } catch (Exception e) {
            // Return empty statistics on error
            return new MessageStatistics(0, 0, 0, new HashMap<>(), new HashMap<>(), 
                                       new HashMap<>(), 0.0);
        }
    }
    
    @Override
    public BroadcastResult broadcastMessage(String subject, String content, List<String> recipients,
                                          MessageType type, String sender) {
        try {
            if (recipients == null || recipients.isEmpty()) {
                return new BroadcastResult(false, 0, 0, 0, Collections.emptyList(), 
                                         "No recipients provided");
            }
            
            List<String> failedRecipients = new ArrayList<>();
            int successfulDeliveries = 0;
            
            // Create broadcast template
            Message template = Message.createBroadcastTemplate(subject, content, sender)
                                     .withType(type);
            
            // Send to each recipient
            for (String recipient : recipients) {
                try {
                    Message individualMessage = template.withRecipient(recipient);
                    MessageResult result = sendMessage(individualMessage);
                    
                    if (result.isSuccess()) {
                        successfulDeliveries++;
                    } else {
                        failedRecipients.add(recipient);
                    }
                    
                } catch (Exception e) {
                    failedRecipients.add(recipient);
                }
            }
            
            int totalRecipients = recipients.size();
            int failedDeliveries = totalRecipients - successfulDeliveries;
            boolean overallSuccess = successfulDeliveries > 0;
            
            return new BroadcastResult(overallSuccess, totalRecipients, successfulDeliveries,
                                     failedDeliveries, failedRecipients, 
                                     String.format("Delivered to %d/%d recipients", 
                                                 successfulDeliveries, totalRecipients));
            
        } catch (Exception e) {
            return new BroadcastResult(false, recipients.size(), 0, recipients.size(),
                                     new ArrayList<>(recipients), 
                                     "Broadcast failed: " + e.getMessage());
        }
    }
    
    @Override
    public List<MessageTemplate> getMessageTemplates(String category) {
        // Return predefined message templates
        List<MessageTemplate> templates = new ArrayList<>();
        
        switch (category.toLowerCase()) {
            case "notification":
                templates.add(new MessageTemplate("order_confirmation", "Order Confirmation",
                    "Your order #{orderNumber} has been confirmed", MessageType.ORDER_NOTIFICATION));
                templates.add(new MessageTemplate("payment_success", "Payment Successful",
                    "Payment of {amount} has been processed", MessageType.PAYMENT_NOTIFICATION));
                templates.add(new MessageTemplate("shipping_update", "Shipping Update",
                    "Your order has been shipped", MessageType.SHIPPING_NOTIFICATION));
                break;
                
            case "security":
                templates.add(new MessageTemplate("security_alert", "Security Alert",
                    "Suspicious activity detected on your account", MessageType.SECURITY_NOTIFICATION));
                templates.add(new MessageTemplate("password_reset", "Password Reset",
                    "Password reset requested for your account", MessageType.SECURITY_NOTIFICATION));
                break;
                
            case "system":
                templates.add(new MessageTemplate("maintenance_notice", "Maintenance Notice",
                    "System maintenance scheduled", MessageType.SYSTEM_NOTIFICATION));
                templates.add(new MessageTemplate("service_outage", "Service Outage",
                    "Service temporarily unavailable", MessageType.SYSTEM_ALERT));
                break;
                
            default:
                templates.add(new MessageTemplate("general", "General Message",
                    "General purpose message template", MessageType.SYSTEM_NOTIFICATION));
        }
        
        return templates;
    }
    
    @Override
    public ProcessingResult processMessageQueue() {
        long startTime = System.currentTimeMillis();
        int processedCount = 0;
        int failedCount = 0;
        
        try {
            // Find pending messages
            List<Message> pendingMessages = messageRepository.findByStatus(MessageStatus.PENDING, 100);
            
            for (Message message : pendingMessages) {
                try {
                    MessageResult result = sendMessage(message);
                    if (result.isSuccess()) {
                        processedCount++;
                    } else {
                        failedCount++;
                    }
                } catch (Exception e) {
                    failedCount++;
                }
            }
            
            long processingTime = System.currentTimeMillis() - startTime;
            String summary = String.format("Processed %d messages, %d failed in %dms",
                                          processedCount, failedCount, processingTime);
            
            return new ProcessingResult(processedCount, failedCount, processingTime, summary);
            
        } catch (Exception e) {
            long processingTime = System.currentTimeMillis() - startTime;
            return new ProcessingResult(0, 0, processingTime, 
                                      "Queue processing failed: " + e.getMessage());
        }
    }
    
    @Override
    public RetryResult retryFailedMessages() {
        try {
            List<Message> retriableMessages = messageRepository.findRetriableMessages(50);
            
            int retriedCount = 0;
            int successfulRetries = 0;
            int permanentFailures = 0;
            
            for (Message message : retriableMessages) {
                if (message.canRetry()) {
                    retriedCount++;
                    
                    try {
                        Message retryMessage = message.incrementRetry();
                        MessageResult result = sendMessage(retryMessage);
                        
                        if (result.isSuccess()) {
                            successfulRetries++;
                        } else {
                            // Check if we've exhausted retries
                            if (!retryMessage.canRetry()) {
                                permanentFailures++;
                            }
                        }
                    } catch (Exception e) {
                        permanentFailures++;
                    }
                } else {
                    permanentFailures++;
                }
            }
            
            String message = String.format("Retried %d messages: %d successful, %d permanent failures",
                                          retriedCount, successfulRetries, permanentFailures);
            
            return new RetryResult(retriedCount, successfulRetries, permanentFailures, message);
            
        } catch (Exception e) {
            return new RetryResult(0, 0, 0, "Retry operation failed: " + e.getMessage());
        }
    }
    
    /**
     * Publishes message using appropriate channel based on message type
     */
    private MessagePublisher.PublishResult publishMessageByType(Message message) {
        try {
            switch (message.getType()) {
                case EMAIL:
                    MessagePublisher.EmailResult emailResult = messagePublisher.publishEmail(
                        message.getRecipient(), message.getSubject(), message.getContent(),
                        message.getSender());
                    return new MessagePublisher.PublishResult(emailResult.isSuccess(), 
                        message.getMessageId(), emailResult.getEmailId(), 
                        emailResult.getMessage(), null, emailResult.getQueueTimeMs());
                
                case SMS:
                    MessagePublisher.SmsResult smsResult = messagePublisher.publishSms(
                        message.getRecipient(), message.getContent());
                    return new MessagePublisher.PublishResult(smsResult.isSuccess(),
                        message.getMessageId(), smsResult.getSmsId(),
                        smsResult.getMessage(), null, 0L);
                
                case PUSH_NOTIFICATION:
                    MessagePublisher.PushResult pushResult = messagePublisher.publishPushNotification(
                        message.getRecipient(), message.getSubject(), message.getContent());
                    return new MessagePublisher.PublishResult(pushResult.isSuccess(),
                        message.getMessageId(), pushResult.getPushId(),
                        pushResult.getMessage(), null, 0L);
                
                case WEBHOOK:
                    MessagePublisher.WebhookResult webhookResult = messagePublisher.publishWebhook(
                        message.getRecipient(), message, new HashMap<>());
                    return new MessagePublisher.PublishResult(webhookResult.isSuccess(),
                        message.getMessageId(), null, webhookResult.getMessage(),
                        null, webhookResult.getResponseTimeMs());
                
                default:
                    // For other types, use general publish
                    return messagePublisher.publish(message);
            }
        } catch (Exception e) {
            return MessagePublisher.PublishResult.failure(message.getMessageId(),
                "Publishing error: " + e.getMessage(), e);
        }
    }
    
    /**
     * Converts use case filter to repository filter
     */
    private MessageRepository.MessageFilter convertToRepositoryFilter(MessageFilter filter) {
        return new MessageRepository.MessageFilter(
            filter.getRecipient().orElse(null),
            filter.getSender().orElse(null),
            MessageType.valueOf(filter.getType().orElse(null)),
            MessageStatus.valueOf(filter.getStatus().orElse(null)),
            MessagePriority.valueOf(filter.getPriority().orElse(null)),
            LocalDateTime.ofEpochSecond(filter.getFromTimestamp().orElse(0L), 0, 
                java.time.ZoneOffset.UTC),
            LocalDateTime.ofEpochSecond(filter.getToTimestamp().orElse(0L), 0, 
                java.time.ZoneOffset.UTC),
            filter.getReadStatus().orElse(null),
            null, // threadId
            null  // contentSearch
        );
    }
}