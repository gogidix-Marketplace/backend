package com.gogidix.shared.messaging.adapter.in.web;

import com.gogidix.shared.messaging.application.port.in.MessageHandlingUseCase;
import com.gogidix.shared.messaging.domain.model.Message;
import com.gogidix.shared.messaging.domain.valueobject.MessageType;
import com.gogidix.shared.messaging.domain.valueobject.MessagePriority;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * REST controller for message handling operations.
 * Provides HTTP endpoints for messaging functionality.
 */
@RestController
@RequestMapping("/api/v1/messaging")
@CrossOrigin(origins = "*", maxAge = 3600)
public class MessageController {
    
    private final MessageHandlingUseCase messageHandlingUseCase;
    
    public MessageController(MessageHandlingUseCase messageHandlingUseCase) {
        this.messageHandlingUseCase = messageHandlingUseCase;
    }
    
    /**
     * Sends a new message
     */
    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(
            @Valid @RequestBody SendMessageRequest request) {
        
        Message message = Message.builder()
            .type(MessageType.valueOf(request.getType().toUpperCase()))
            .priority(MessagePriority.valueOf(request.getPriority().toUpperCase()))
            .subject(request.getSubject())
            .content(request.getContent())
            .sender(request.getSender())
            .recipient(request.getRecipient())
            .metadata(request.getMetadata())
            .build();
        
        MessageHandlingUseCase.MessageResult result = messageHandlingUseCase.sendMessage(message);
        
        if (result.isSuccess()) {
            return ResponseEntity.ok(MessageResponse.success(
                result.getMessageId(), 
                result.getMessage()
            ));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(MessageResponse.failure(result.getMessage()));
        }
    }
    
    /**
     * Retrieves messages for a recipient
     */
    @GetMapping("/inbox/{recipient}")
    public ResponseEntity<List<MessageSummary>> getInbox(
            @PathVariable String recipient,
            @RequestParam(required = false, defaultValue = "50") int limit,
            @RequestParam(required = false) String status) {
        
        MessageHandlingUseCase.MessageFilter filter = 
            MessageHandlingUseCase.MessageFilter.forRecipient(recipient, status);
        
        List<Message> messages = messageHandlingUseCase.getMessages(filter, limit);
        
        List<MessageSummary> summaries = messages.stream()
            .map(MessageSummary::from)
            .toList();
        
        return ResponseEntity.ok(summaries);
    }
    
    /**
     * Retrieves a specific message
     */
    @GetMapping("/messages/{messageId}")
    public ResponseEntity<MessageDetail> getMessage(@PathVariable String messageId) {
        
        return messageHandlingUseCase.getMessage(messageId)
            .map(message -> ResponseEntity.ok(MessageDetail.from(message)))
            .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Marks a message as read
     */
    @PostMapping("/messages/{messageId}/read")
    public ResponseEntity<MessageResponse> markAsRead(@PathVariable String messageId) {
        
        MessageHandlingUseCase.MessageResult result = 
            messageHandlingUseCase.markAsRead(messageId);
        
        if (result.isSuccess()) {
            return ResponseEntity.ok(MessageResponse.success(messageId, result.getMessage()));
        } else {
            return ResponseEntity.badRequest()
                .body(MessageResponse.failure(result.getMessage()));
        }
    }
    
    /**
     * Deletes a message
     */
    @DeleteMapping("/messages/{messageId}")
    public ResponseEntity<MessageResponse> deleteMessage(@PathVariable String messageId) {
        
        MessageHandlingUseCase.MessageResult result = 
            messageHandlingUseCase.deleteMessage(messageId);
        
        if (result.isSuccess()) {
            return ResponseEntity.ok(MessageResponse.success(messageId, result.getMessage()));
        } else {
            return ResponseEntity.badRequest()
                .body(MessageResponse.failure(result.getMessage()));
        }
    }
    
    /**
     * Gets message statistics
     */
    @GetMapping("/statistics/{userId}")
    public ResponseEntity<MessageStatisticsResponse> getStatistics(
            @PathVariable String userId) {
        
        MessageHandlingUseCase.MessageStatistics stats = 
            messageHandlingUseCase.getMessageStatistics(userId);
        
        return ResponseEntity.ok(MessageStatisticsResponse.from(stats));
    }
    
    /**
     * Publishes a broadcast message
     */
    @PostMapping("/broadcast")
    public ResponseEntity<BroadcastResponse> broadcastMessage(
            @Valid @RequestBody BroadcastRequest request) {
        
        MessageHandlingUseCase.BroadcastResult result = messageHandlingUseCase.broadcastMessage(
            request.getSubject(),
            request.getContent(), 
            request.getRecipients(),
            MessageType.valueOf(request.getType().toUpperCase()),
            request.getSender()
        );
        
        return ResponseEntity.ok(BroadcastResponse.from(result));
    }
    
    /**
     * Gets message templates
     */
    @GetMapping("/templates")
    public ResponseEntity<List<MessageTemplate>> getMessageTemplates(
            @RequestParam(required = false) String category) {
        
        List<MessageTemplate> templates = messageHandlingUseCase.getMessageTemplates(category);
        
        return ResponseEntity.ok(templates);
    }
    
    // DTOs
    
    public static class SendMessageRequest {
        private String type;
        private String priority;
        private String subject;
        private String content;
        private String sender;
        private String recipient;
        private Map<String, Object> metadata;
        
        // Getters and setters
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getPriority() { return priority; }
        public void setPriority(String priority) { this.priority = priority; }
        
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
        
        public String getRecipient() { return recipient; }
        public void setRecipient(String recipient) { this.recipient = recipient; }
        
        public Map<String, Object> getMetadata() { return metadata; }
        public void setMetadata(Map<String, Object> metadata) { this.metadata = metadata; }
    }
    
    public static class MessageResponse {
        private boolean success;
        private String messageId;
        private String message;
        
        public MessageResponse(boolean success, String messageId, String message) {
            this.success = success;
            this.messageId = messageId;
            this.message = message;
        }
        
        public static MessageResponse success(String messageId, String message) {
            return new MessageResponse(true, messageId, message);
        }
        
        public static MessageResponse failure(String message) {
            return new MessageResponse(false, null, message);
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public String getMessageId() { return messageId; }
        public String getMessage() { return message; }
    }
    
    public static class MessageSummary {
        private String messageId;
        private String type;
        private String priority;
        private String subject;
        private String sender;
        private String timestamp;
        private boolean read;
        
        public static MessageSummary from(Message message) {
            MessageSummary summary = new MessageSummary();
            summary.messageId = message.getMessageId();
            summary.type = message.getType().name();
            summary.priority = message.getPriority().name();
            summary.subject = message.getSubject();
            summary.sender = message.getSender();
            summary.timestamp = message.getCreatedAt().toString();
            summary.read = message.isRead();
            return summary;
        }
        
        // Getters
        public String getMessageId() { return messageId; }
        public String getType() { return type; }
        public String getPriority() { return priority; }
        public String getSubject() { return subject; }
        public String getSender() { return sender; }
        public String getTimestamp() { return timestamp; }
        public boolean isRead() { return read; }
    }
    
    public static class MessageDetail {
        private String messageId;
        private String type;
        private String priority;
        private String subject;
        private String content;
        private String sender;
        private String recipient;
        private String timestamp;
        private boolean read;
        private Map<String, Object> metadata;
        
        public static MessageDetail from(Message message) {
            MessageDetail detail = new MessageDetail();
            detail.messageId = message.getMessageId();
            detail.type = message.getType().name();
            detail.priority = message.getPriority().name();
            detail.subject = message.getSubject();
            detail.content = message.getContent();
            detail.sender = message.getSender();
            detail.recipient = message.getRecipient();
            detail.timestamp = message.getCreatedAt().toString();
            detail.read = message.isRead();
            detail.metadata = message.getMetadata();
            return detail;
        }
        
        // Getters
        public String getMessageId() { return messageId; }
        public String getType() { return type; }
        public String getPriority() { return priority; }
        public String getSubject() { return subject; }
        public String getContent() { return content; }
        public String getSender() { return sender; }
        public String getRecipient() { return recipient; }
        public String getTimestamp() { return timestamp; }
        public boolean isRead() { return read; }
        public Map<String, Object> getMetadata() { return metadata; }
    }
    
    public static class MessageStatisticsResponse {
        private long totalMessages;
        private long unreadMessages;
        private long sentMessages;
        private Map<String, Long> messagesByType;
        private Map<String, Long> messagesByPriority;
        
        public static MessageStatisticsResponse from(MessageHandlingUseCase.MessageStatistics stats) {
            MessageStatisticsResponse response = new MessageStatisticsResponse();
            response.totalMessages = stats.getTotalMessages();
            response.unreadMessages = stats.getUnreadMessages();
            response.sentMessages = stats.getSentMessages();
            response.messagesByType = stats.getMessagesByType();
            response.messagesByPriority = stats.getMessagesByPriority();
            return response;
        }
        
        // Getters
        public long getTotalMessages() { return totalMessages; }
        public long getUnreadMessages() { return unreadMessages; }
        public long getSentMessages() { return sentMessages; }
        public Map<String, Long> getMessagesByType() { return messagesByType; }
        public Map<String, Long> getMessagesByPriority() { return messagesByPriority; }
    }
    
    public static class BroadcastRequest {
        private String subject;
        private String content;
        private List<String> recipients;
        private String type;
        private String sender;
        
        // Getters and setters
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public List<String> getRecipients() { return recipients; }
        public void setRecipients(List<String> recipients) { this.recipients = recipients; }
        
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
        
        public String getSender() { return sender; }
        public void setSender(String sender) { this.sender = sender; }
    }
    
    public static class BroadcastResponse {
        private boolean success;
        private int totalRecipients;
        private int successfulDeliveries;
        private int failedDeliveries;
        private List<String> failedRecipients;
        private String message;
        
        public static BroadcastResponse from(MessageHandlingUseCase.BroadcastResult result) {
            BroadcastResponse response = new BroadcastResponse();
            response.success = result.isSuccess();
            response.totalRecipients = result.getTotalRecipients();
            response.successfulDeliveries = result.getSuccessfulDeliveries();
            response.failedDeliveries = result.getFailedDeliveries();
            response.failedRecipients = result.getFailedRecipients();
            response.message = result.getMessage();
            return response;
        }
        
        // Getters
        public boolean isSuccess() { return success; }
        public int getTotalRecipients() { return totalRecipients; }
        public int getSuccessfulDeliveries() { return successfulDeliveries; }
        public int getFailedDeliveries() { return failedDeliveries; }
        public List<String> getFailedRecipients() { return failedRecipients; }
        public String getMessage() { return message; }
    }
    
    public static class MessageTemplate {
        private String templateId;
        private String name;
        private String category;
        private String subject;
        private String content;
        private Map<String, String> variables;
        
        // Constructors
        public MessageTemplate() {}
        
        public MessageTemplate(String templateId, String name, String category, com.gogidix.shared.messaging.domain.valueobject.MessageType messageType) {
            this.templateId = templateId;
            this.name = name;
            this.category = category;
            // messageType parameter can be used for future extensions
        }
        
        // Getters and setters
        public String getTemplateId() { return templateId; }
        public void setTemplateId(String templateId) { this.templateId = templateId; }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        
        public String getSubject() { return subject; }
        public void setSubject(String subject) { this.subject = subject; }
        
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        
        public Map<String, String> getVariables() { return variables; }
        public void setVariables(Map<String, String> variables) { this.variables = variables; }
    }
}