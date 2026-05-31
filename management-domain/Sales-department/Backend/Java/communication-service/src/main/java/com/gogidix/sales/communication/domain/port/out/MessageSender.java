package com.gogidix.sales.communication.domain.port.out;

import com.gogidix.sales.communication.domain.model.Message;

import java.util.List;

/**
 * Message Sender Interface (Port)
 * Defines the contract for sending messages through external channels
 */
public interface MessageSender {

    /**
     * Sends a message through the configured channel
     */
    String send(Message message);

    /**
     * Sends multiple messages
     */
    List<String> sendBatch(List<Message> messages);

    /**
     * Checks if the sender is ready
     */
    boolean isReady();

    /**
     * Validates if a message can be sent
     */
    boolean canSend(Message message);
}
