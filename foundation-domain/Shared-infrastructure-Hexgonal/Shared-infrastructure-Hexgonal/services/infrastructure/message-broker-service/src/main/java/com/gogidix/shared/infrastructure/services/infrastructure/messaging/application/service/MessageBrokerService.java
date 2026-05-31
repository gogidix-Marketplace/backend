package com.gogidix.shared.infrastructure.services.infrastructure.messaging.application.service;

import com.gogidix.shared.infrastructure.services.infrastructure.messaging.application.port.in.MessageBrokerPort;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.aggregate.MessageBrokerRegistry;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.model.Message;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.model.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Application service for message broker operations.
 * Implements the hexagonal architecture pattern.
 */
@Service
public class MessageBrokerService implements MessageBrokerPort {

    private static final Logger log = LoggerFactory.getLogger(MessageBrokerService.class);

    private final MessageBrokerRegistry messageBrokerRegistry;

    public MessageBrokerService(MessageBrokerRegistry messageBrokerRegistry) {
        this.messageBrokerRegistry = messageBrokerRegistry;
    }

    @Override
    public Topic createTopic(String topicName, int partitions, short replicationFactor,
                            Topic.TopicType type, String description) {
        log.info("Creating topic: name={}, partitions={}, type={}", topicName, partitions, type);
        return messageBrokerRegistry.createTopic(topicName, partitions, replicationFactor, type, description);
    }

    @Override
    public Message publishMessage(String topicName, String key, Object payload, Map<String, Object> headers) {
        log.debug("Publishing message: topic={}, key={}", topicName, key);
        return messageBrokerRegistry.publishMessage(topicName, key, payload, headers);
    }

    @Override
    public Message consumeMessage(String topicName, String consumerGroup) {
        return messageBrokerRegistry.consumeMessage(topicName, consumerGroup);
    }

    @Override
    public List<Message> consumeBatch(String topicName, String consumerGroup, int batchSize) {
        log.debug("Consuming batch: topic={}, group={}, size={}", topicName, consumerGroup, batchSize);
        return messageBrokerRegistry.consumeBatch(topicName, consumerGroup, batchSize);
    }

    @Override
    public void acknowledgeMessage(String messageId, String topicName) {
        log.debug("Acknowledging message: messageId={}, topic={}", messageId, topicName);
        messageBrokerRegistry.acknowledgeMessage(messageId, topicName);
    }

    @Override
    public List<Topic> getAllTopics() {
        return messageBrokerRegistry.getAllTopics();
    }

    @Override
    public long getTopicMessageCount(String topicName) {
        return messageBrokerRegistry.getTopicMessageCount(topicName);
    }
}
