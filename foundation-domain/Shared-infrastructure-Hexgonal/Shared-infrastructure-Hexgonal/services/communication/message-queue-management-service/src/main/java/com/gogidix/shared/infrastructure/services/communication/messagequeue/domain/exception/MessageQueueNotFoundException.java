package com.gogidix.shared.infrastructure.services.communication.messagequeue.domain.exception;
public class MessageQueueNotFoundException extends RuntimeException {
    public MessageQueueNotFoundException(String id) {
        super("Message Queue not found with id: " + id);
    }
}
