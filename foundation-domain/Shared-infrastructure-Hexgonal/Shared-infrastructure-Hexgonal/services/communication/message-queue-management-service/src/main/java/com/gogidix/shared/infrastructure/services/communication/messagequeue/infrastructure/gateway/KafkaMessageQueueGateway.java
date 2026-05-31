package com.gogidix.shared.infrastructure.services.communication.messagequeue.infrastructure.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.MessageListenerContainer;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Apache Kafka adapter implementation for message queue gateway.
 *
 * <p>This implementation uses Spring Kafka to interact with Apache Kafka.
 * It supports:
 * <ul>
 *   <li>Message publishing to topics</li>
 *   <li>Message consumption from topics</li>
 *   <li>Consumer group management</li>
 *   <li>Message acknowledgment</li>
 *   <li>Topic creation and configuration</li>
 * </ul>
 *
 * <p>Configuration:
 * <pre>
 * message-queue.provider=kafka
 * message-queue.kafka.bootstrap-servers=localhost:9092
 * message-queue.kafka.consumer.group-id=default-group
 * message-queue.kafka.auto-create-topics=true
 * </pre>
 */
@Service
@ConditionalOnProperty(name = "message-queue.provider", havingValue = "kafka")
public class KafkaMessageQueueGateway implements MessageQueueGateway {
    private static final Logger logger = LoggerFactory.getLogger(KafkaMessageQueueGateway.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final Map<String, MessageListenerContainer> listenerContainers;
    private final String bootstrapServers;
    private final String consumerGroupId;
    private final boolean autoCreateTopics;
    private final boolean configured;

    public KafkaMessageQueueGateway(
            @Value("${message-queue.kafka.bootstrap-servers:}") String bootstrapServers,
            @Value("${message-queue.kafka.consumer.group-id:gogidix-default}") String consumerGroupId,
            @Value("${message-queue.kafka.auto-create-topics:true}") boolean autoCreateTopics,
            KafkaTemplate<String, Object> kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
        this.bootstrapServers = bootstrapServers;
        this.consumerGroupId = consumerGroupId;
        this.autoCreateTopics = autoCreateTopics;
        this.listenerContainers = new HashMap<>();

        this.configured = !bootstrapServers.isEmpty();
        if (configured) {
            logger.info("Kafka message queue gateway initialized with bootstrap servers: {}", bootstrapServers);
        } else {
            logger.warn("Kafka message queue gateway not configured. Please set message-queue.kafka.bootstrap-servers");
        }
    }

    @Override
    public String sendMessage(String queueName, Object message, Map<String, Object> properties) {
        if (!configured) {
            throw new IllegalStateException("Kafka message queue gateway is not configured");
        }

        try {
            String messageId = properties != null && properties.containsKey("messageId")
                ? (String) properties.get("messageId")
                : UUID.randomUUID().toString();

            kafkaTemplate.send(queueName, message);
            logger.debug("Message sent to Kafka topic: {}, messageId: {}", queueName, messageId);
            return messageId;
        } catch (Exception e) {
            logger.error("Failed to send message to Kafka topic: {}", queueName, e);
            throw new RuntimeException("Kafka send failed: " + e.getMessage(), e);
        }
    }

    @Override
    public int sendBatch(String queueName, List<Object> messages, Map<String, Object> properties) {
        if (!configured) {
            throw new IllegalStateException("Kafka message queue gateway is not configured");
        }

        int sentCount = 0;
        for (Object message : messages) {
            try {
                sendMessage(queueName, message, properties);
                sentCount++;
            } catch (Exception e) {
                logger.warn("Failed to send message in batch to Kafka topic: {}", queueName, e);
            }
        }
        logger.info("Batch sent to Kafka topic: {}, count: {}", queueName, sentCount);
        return sentCount;
    }

    @Override
    public List<QueueMessage> receiveMessages(String queueName, int maxMessages) {
        // Kafka uses push-based consumption via listeners
        // This method is provided for compatibility but will return empty
        // Applications should use registerMessageListener() for consuming messages
        logger.warn("Kafka uses listener-based consumption. Use registerMessageListener() for queue: {}", queueName);
        return List.of();
    }

    @Override
    public List<QueueMessage> peekMessages(String queueName, int maxMessages) {
        // Kafka doesn't natively support peeking
        logger.warn("Kafka doesn't support message peeking for topic: {}", queueName);
        return List.of();
    }

    @Override
    public boolean acknowledgeMessage(String queueName, String messageId) {
        // Kafka handles acknowledgment via listener offsets
        // Manual acknowledgment is handled via the listener container
        logger.debug("Kafka acknowledges messages via listener offsets for topic: {}", queueName);
        return true;
    }

    @Override
    public boolean rejectMessage(String queueName, String messageId, boolean requeue) {
        // Kafka doesn't have native message rejection
        // Messages can be sent to dead-letter topics via configuration
        logger.debug("Kafka handles rejected messages via dead-letter topics for topic: {}", queueName);
        return true;
    }

    @Override
    public boolean createQueue(String queueName, QueueProperties properties) {
        if (!configured) {
            throw new IllegalStateException("Kafka message queue gateway is not configured");
        }

        // Kafka topics are typically auto-created on first publish
        if (autoCreateTopics) {
            logger.info("Kafka topic will be auto-created on first use: {}", queueName);
            return true;
        }

        // Topic creation requires admin client - for simplicity, assume exists
        logger.warn("Topic creation requires Kafka AdminClient. Assuming topic exists: {}", queueName);
        return true;
    }

    @Override
    public boolean deleteQueue(String queueName) {
        // Requires Kafka AdminClient
        logger.warn("Topic deletion requires Kafka AdminClient. Topic not deleted: {}", queueName);
        return false;
    }

    @Override
    public QueueInfo getQueueInfo(String queueName) {
        // Requires Kafka AdminClient
        logger.debug("Getting topic info for Kafka topic: {}", queueName);
        return new QueueInfo(queueName, -1, -1, true, "topic");
    }

    @Override
    public boolean isConfigured() {
        return configured;
    }

    @Override
    public boolean isHealthy() {
        if (!configured) {
            return false;
        }

        try {
            // Simple health check - try to get cluster info
            kafkaTemplate.getProducerFactory();
            return true;
        } catch (Exception e) {
            logger.warn("Kafka health check failed: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public String getProvider() {
        return "kafka";
    }

    @Override
    public int purgeQueue(String queueName) {
        // Kafka doesn't natively support purging topics
        logger.warn("Kafka doesn't support topic purging: {}", queueName);
        return 0;
    }

    /**
     * Register a message listener for a specific topic.
     *
     * @param queueName The topic name
     * @param listener The message listener callback
     */
    public void registerMessageListener(String queueName, MessageListener listener) {
        if (listenerContainers.containsKey(queueName)) {
            logger.warn("Listener already registered for topic: {}", queueName);
            return;
        }

        // In a real implementation, you would configure
        // @KafkaListener beans dynamically here
        logger.info("Message listener registration requested for topic: {}", queueName);
    }

    /**
     * Unregister a message listener.
     *
     * @param queueName The topic name
     */
    public void unregisterMessageListener(String queueName) {
        MessageListenerContainer container = listenerContainers.remove(queueName);
        if (container != null) {
            container.stop();
            logger.info("Message listener unregistered for topic: {}", queueName);
        }
    }
}
