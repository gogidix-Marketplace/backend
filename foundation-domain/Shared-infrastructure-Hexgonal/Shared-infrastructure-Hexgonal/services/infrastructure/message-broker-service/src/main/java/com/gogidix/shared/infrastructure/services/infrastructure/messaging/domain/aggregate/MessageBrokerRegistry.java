package com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.aggregate;

import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.event.MessagePublishedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.event.MessageConsumedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.event.TopicCreatedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.model.Message;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.model.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * Message Broker Registry Aggregate Root.
 * Manages messaging operations and publishes domain events.
 */
public class MessageBrokerRegistry {

    private static final Logger log = LoggerFactory.getLogger(MessageBrokerRegistry.class);

    // In-memory topic registry
    private final Map<String, Topic> topics = new ConcurrentHashMap<>();
    private final Map<String, List<Message>> messageBuffer = new ConcurrentHashMap<>();

    // Event handlers
    private final Map<String, Consumer<MessagePublishedEvent>> messagePublishedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<MessageConsumedEvent>> messageConsumedHandlers = new ConcurrentHashMap<>();
    private final Map<String, Consumer<TopicCreatedEvent>> topicCreatedHandlers = new ConcurrentHashMap<>();

    public MessageBrokerRegistry() {
        log.info("MessageBrokerRegistry aggregate initialized");
    }

    // Event handler registration
    public void onMessagePublished(Consumer<MessagePublishedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        messagePublishedHandlers.put(handlerId, handler);
    }

    public void onMessageConsumed(Consumer<MessageConsumedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        messageConsumedHandlers.put(handlerId, handler);
    }

    public void onTopicCreated(Consumer<TopicCreatedEvent> handler) {
        String handlerId = UUID.randomUUID().toString();
        topicCreatedHandlers.put(handlerId, handler);
    }

    // Topic operations
    public Topic createTopic(String topicName, int partitions, short replicationFactor,
                           Topic.TopicType type, String description) {
        log.info("Creating topic: name={}, partitions={}, replicationFactor={}",
            topicName, partitions, replicationFactor);

        if (topics.containsKey(topicName)) {
            log.warn("Topic already exists: {}", topicName);
            return topics.get(topicName);
        }

        Topic topic = Topic.builder()
            .topicName(topicName)
            .partitions(partitions)
            .replicationFactor(replicationFactor)
            .type(type)
            .description(description)
            .createdAt(Instant.now())
            .build();

        topics.put(topicName, topic);
        messageBuffer.put(topicName, new ArrayList<>());

        TopicCreatedEvent event = new TopicCreatedEvent(
            topicName,
            partitions,
            replicationFactor,
            type.name(),
            Instant.now()
        );
        publishEvent(event);

        log.info("Topic created successfully: {}", topicName);
        return topic;
    }

    public Optional<Topic> getTopic(String topicName) {
        return Optional.ofNullable(topics.get(topicName));
    }

    public List<Topic> getAllTopics() {
        return new ArrayList<>(topics.values());
    }

    public boolean deleteTopic(String topicName) {
        log.info("Deleting topic: {}", topicName);
        return topics.remove(topicName) != null;
    }

    // Message operations
    public Message publishMessage(String topicName, String key, Object payload,
                                 Map<String, Object> headers) {
        log.info("Publishing message: topic={}, key={}", topicName, key);

        Topic topic = topics.get(topicName);
        if (topic == null) {
            log.warn("Topic not found: {}", topicName);
            throw new IllegalArgumentException("Topic not found: " + topicName);
        }

        String messageId = generateMessageId();
        Instant now = Instant.now();

        Message message = Message.builder()
            .messageId(messageId)
            .topic(topicName)
            .key(key)
            .payload(payload)
            .headers(headers)
            .timestamp(now)
            .status(Message.MessageStatus.SENT)
            .build();

        // Buffer message for tracking
        messageBuffer.get(topicName).add(message);

        MessagePublishedEvent event = new MessagePublishedEvent(
            messageId,
            topicName,
            key,
            now
        );
        publishEvent(event);

        log.info("Message published: messageId={}, topic={}", messageId, topicName);
        return message;
    }

    public Message consumeMessage(String topicName, String consumerGroup) {
        log.debug("Consuming message: topic={}, group={}", topicName, consumerGroup);

        List<Message> messages = messageBuffer.get(topicName);
        if (messages == null || messages.isEmpty()) {
            return null;
        }

        Message message = messages.remove(0);

        MessageConsumedEvent event = new MessageConsumedEvent(
            message.getMessageId(),
            topicName,
            consumerGroup,
            Instant.now()
        );
        publishEvent(event);

        return message;
    }

    public List<Message> consumeBatch(String topicName, String consumerGroup, int batchSize) {
        log.debug("Consuming batch: topic={}, group={}, size={}", topicName, consumerGroup, batchSize);

        List<Message> messages = messageBuffer.get(topicName);
        if (messages == null || messages.isEmpty()) {
            return Collections.emptyList();
        }

        List<Message> batch = new ArrayList<>();
        for (int i = 0; i < batchSize && !messages.isEmpty(); i++) {
            Message message = messages.remove(0);
            batch.add(message);

            MessageConsumedEvent event = new MessageConsumedEvent(
                message.getMessageId(),
                topicName,
                consumerGroup,
                Instant.now()
            );
            publishEvent(event);
        }

        return batch;
    }

    public void acknowledgeMessage(String messageId, String topicName) {
        log.debug("Message acknowledged: messageId={}, topic={}", messageId, topicName);
        // In a real implementation, this would commit the offset
    }

    public long getTopicMessageCount(String topicName) {
        List<Message> messages = messageBuffer.get(topicName);
        return messages != null ? messages.size() : 0;
    }

    public Map<String, Long> getAllTopicMessageCounts() {
        Map<String, Long> counts = new HashMap<>();
        messageBuffer.forEach((topic, messages) -> counts.put(topic, (long) messages.size()));
        return counts;
    }

    // Utility methods
    private String generateMessageId() {
        return "msg-" + UUID.randomUUID().toString();
    }

    private void publishEvent(MessagePublishedEvent event) {
        try {
            messagePublishedHandlers.values().forEach(handler -> {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    log.error("Error in message published handler", e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to publish MessagePublishedEvent", e);
        }
    }

    private void publishEvent(MessageConsumedEvent event) {
        try {
            messageConsumedHandlers.values().forEach(handler -> {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    log.error("Error in message consumed handler", e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to publish MessageConsumedEvent", e);
        }
    }

    private void publishEvent(TopicCreatedEvent event) {
        try {
            topicCreatedHandlers.values().forEach(handler -> {
                try {
                    handler.accept(event);
                } catch (Exception e) {
                    log.error("Error in topic created handler", e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to publish TopicCreatedEvent", e);
        }
    }
}
