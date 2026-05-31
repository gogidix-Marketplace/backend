package com.gogidix.shared.infrastructure.services.infrastructure.messaging.application.port.in;

import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.model.Message;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.model.Topic;

import java.util.List;
import java.util.Map;

/**
 * Input port for message broker operations.
 * Defines the contract for messaging use cases.
 */
public interface MessageBrokerPort {

    // Topic operations
    Topic createTopic(String topicName, int partitions, short replicationFactor,
                     Topic.TopicType type, String description);

    // Message operations
    Message publishMessage(String topicName, String key, Object payload, Map<String, Object> headers);

    Message consumeMessage(String topicName, String consumerGroup);

    List<Message> consumeBatch(String topicName, String consumerGroup, int batchSize);

    void acknowledgeMessage(String messageId, String topicName);

    // Query operations
    List<Topic> getAllTopics();

    long getTopicMessageCount(String topicName);
}
