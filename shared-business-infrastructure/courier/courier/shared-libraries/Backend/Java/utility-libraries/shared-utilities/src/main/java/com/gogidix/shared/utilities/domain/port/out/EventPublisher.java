package com.gogidix.shared.utilities.domain.port.out;

import com.gogidix.shared.utilities.domain.model.UtilityOperation;
import com.gogidix.shared.utilities.domain.model.OperationStatus;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * Event publisher port for utility operations
 * Defines the contract for publishing utility operation events
 */
public interface EventPublisher {
    
    /**
     * Publishes an operation started event
     */
    void publishOperationStarted(UtilityOperation operation);
    
    /**
     * Publishes an operation completed event
     */
    void publishOperationCompleted(UtilityOperation operation);
    
    /**
     * Publishes an operation failed event
     */
    void publishOperationFailed(UtilityOperation operation, String errorMessage, String errorCode);
    
    /**
     * Publishes an operation cancelled event
     */
    void publishOperationCancelled(UtilityOperation operation, String reason);
    
    /**
     * Publishes an operation progress updated event
     */
    void publishProgressUpdated(UtilityOperation operation, int progressPercentage, String phase);
    
    /**
     * Publishes an operation status changed event
     */
    void publishStatusChanged(UtilityOperation operation, OperationStatus oldStatus, OperationStatus newStatus);
    
    /**
     * Publishes a generic utility event
     */
    void publishUtilityEvent(UtilityEvent event);
    
    /**
     * Base utility event
     */
    abstract class UtilityEvent {
        private final String eventId;
        private final LocalDateTime timestamp;
        private final String operationId;
        private final String userId;
        private final String eventType;
        private final Map<String, Object> eventData;
        
        protected UtilityEvent(String eventId, LocalDateTime timestamp, String operationId,
                             String userId, String eventType, Map<String, Object> eventData) {
            this.eventId = eventId;
            this.timestamp = timestamp;
            this.operationId = operationId;
            this.userId = userId;
            this.eventType = eventType;
            this.eventData = eventData;
        }
        
        public String getEventId() { return eventId; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public String getOperationId() { return operationId; }
        public String getUserId() { return userId; }
        public String getEventType() { return eventType; }
        public Map<String, Object> getEventData() { return eventData; }
    }
    
    /**
     * Operation started event
     */
    class OperationStartedEvent extends UtilityEvent {
        public OperationStartedEvent(String eventId, LocalDateTime timestamp, String operationId,
                                   String userId, Map<String, Object> eventData) {
            super(eventId, timestamp, operationId, userId, "OPERATION_STARTED", eventData);
        }
    }
    
    /**
     * Operation completed event
     */
    class OperationCompletedEvent extends UtilityEvent {
        public OperationCompletedEvent(String eventId, LocalDateTime timestamp, String operationId,
                                     String userId, Map<String, Object> eventData) {
            super(eventId, timestamp, operationId, userId, "OPERATION_COMPLETED", eventData);
        }
    }
    
    /**
     * Operation failed event
     */
    class OperationFailedEvent extends UtilityEvent {
        public OperationFailedEvent(String eventId, LocalDateTime timestamp, String operationId,
                                  String userId, Map<String, Object> eventData) {
            super(eventId, timestamp, operationId, userId, "OPERATION_FAILED", eventData);
        }
    }
    
    /**
     * Operation cancelled event
     */
    class OperationCancelledEvent extends UtilityEvent {
        public OperationCancelledEvent(String eventId, LocalDateTime timestamp, String operationId,
                                     String userId, Map<String, Object> eventData) {
            super(eventId, timestamp, operationId, userId, "OPERATION_CANCELLED", eventData);
        }
    }
    
    /**
     * Operation progress updated event
     */
    class ProgressUpdatedEvent extends UtilityEvent {
        public ProgressUpdatedEvent(String eventId, LocalDateTime timestamp, String operationId,
                                  String userId, Map<String, Object> eventData) {
            super(eventId, timestamp, operationId, userId, "PROGRESS_UPDATED", eventData);
        }
    }
    
    /**
     * Operation status changed event
     */
    class StatusChangedEvent extends UtilityEvent {
        public StatusChangedEvent(String eventId, LocalDateTime timestamp, String operationId,
                                String userId, Map<String, Object> eventData) {
            super(eventId, timestamp, operationId, userId, "STATUS_CHANGED", eventData);
        }
    }
}