package com.gogidix.shared.infrastructure.services.communication.messagequeue.infrastructure.gateway;

import java.util.List;
import java.util.Map;

/**
 * Unified interface for message queue providers.
 *
 * <p>This interface abstracts different message queue providers allowing
 * the message queue service to work with any backend transparently.</p>
 *
 * <p>Supported providers:
 * <ul>
 *   <li>Apache Kafka</li>
 *   <li>RabbitMQ</li>
 *   <li>Amazon SQS</li>
 *   <li>Azure Service Bus</li>
 * </ul>
 *
 * <p>Configuration:
 * <pre>
 * message-queue.provider=kafka|rabbitmq|sqs|azure
 * message-queue.kafka.bootstrap-servers=localhost:9092
 * message-queue.rabbitmq.host=localhost
 * message-queue.rabbitmq.port=5672
 * message-queue.rabbitmq.username=guest
 * message-queue.rabbitmq.password=guest
 * </pre>
 */
public interface MessageQueueGateway {

    /**
     * Send a message to a queue.
     *
     * @param queueName The target queue name
     * @param message The message payload
     * @param properties Additional message properties (headers, etc.)
     * @return The message ID or correlation ID
     */
    String sendMessage(String queueName, Object message, Map<String, Object> properties);

    /**
     * Send multiple messages to a queue in batch.
     *
     * @param queueName The target queue name
     * @param messages The list of message payloads
     * @param properties Additional message properties
     * @return The number of messages sent
     */
    int sendBatch(String queueName, List<Object> messages, Map<String, Object> properties);

    /**
     * Receive messages from a queue.
     *
     * @param queueName The source queue name
     * @param maxMessages Maximum number of messages to receive
     * @return List of received messages
     */
    List<QueueMessage> receiveMessages(String queueName, int maxMessages);

    /**
     * Peek at messages without removing them from queue.
     *
     * @param queueName The source queue name
     * @param maxMessages Maximum number of messages to peek
     * @return List of peeked messages
     */
    List<QueueMessage> peekMessages(String queueName, int maxMessages);

    /**
     * Acknowledge/complete processing of a message.
     *
     * @param queueName The queue name
     * @param messageId The message ID to acknowledge
     * @return true if acknowledged successfully
     */
    boolean acknowledgeMessage(String queueName, String messageId);

    /**
     * Reject/negatively acknowledge a message.
     *
     * @param queueName The queue name
     * @param messageId The message ID to reject
     * @param requeue Whether to requeue the message
     * @return true if rejected successfully
     */
    boolean rejectMessage(String queueName, String messageId, boolean requeue);

    /**
     * Create a new queue.
     *
     * @param queueName The queue name
     * @param properties Queue properties (durable, exclusive, etc.)
     * @return true if queue created successfully
     */
    boolean createQueue(String queueName, QueueProperties properties);

    /**
     * Delete a queue.
     *
     * @param queueName The queue name to delete
     * @return true if deleted successfully
     */
    boolean deleteQueue(String queueName);

    /**
     * Get queue information.
     *
     * @param queueName The queue name
     * @return Queue information or null if not exists
     */
    QueueInfo getQueueInfo(String queueName);

    /**
     * Check if gateway is properly configured.
     *
     * @return true if configured and ready to use
     */
    boolean isConfigured();

    /**
     * Check health of the message queue connection.
     *
     * @return true if connection is healthy
     */
    boolean isHealthy();

    /**
     * Get the provider identifier.
     *
     * @return The provider name (kafka, rabbitmq, sqs, azure)
     */
    String getProvider();

    /**
     * Purge all messages from a queue.
     *
     * @param queueName The queue name to purge
     * @return The number of messages purged
     */
    int purgeQueue(String queueName);

    /**
     * Queue message representation.
     */
    record QueueMessage(
        String messageId,
        String queueName,
        Object payload,
        Map<String, Object> headers,
        long timestamp,
        int retryCount
    ) {}

    /**
     * Queue properties for creation.
     */
    record QueueProperties(
        boolean durable,
        boolean exclusive,
        boolean autoDelete,
        long messageTtl,
        int maxPriority,
        int maxLength
    ) {
        public static QueueProperties defaults() {
            return new QueueProperties(true, false, false, 0, 0, 0);
        }
    }

    /**
     * Queue information.
     */
    record QueueInfo(
        String name,
        int messageCount,
        int consumerCount,
        boolean durable,
        String type
    ) {}
}
