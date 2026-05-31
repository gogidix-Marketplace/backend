package com.gogidix.shared.infrastructure.services.infrastructure.messaging.config;

import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.aggregate.MessageBrokerRegistry;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.event.MessageConsumedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.event.MessagePublishedEvent;
import com.gogidix.shared.infrastructure.services.infrastructure.messaging.domain.event.TopicCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for message broker domain layer.
 * Creates aggregate roots and wires up event handlers.
 */
@Configuration
public class MessageBrokerDomainConfig {

    private static final Logger log = LoggerFactory.getLogger(MessageBrokerDomainConfig.class);

    @Bean
    public MessageBrokerRegistry messageBrokerRegistry() {
        MessageBrokerRegistry registry = new MessageBrokerRegistry();

        // Register event handlers
        registry.onMessagePublished(this::handleMessagePublished);
        registry.onMessageConsumed(this::handleMessageConsumed);
        registry.onTopicCreated(this::handleTopicCreated);

        log.info("MessageBrokerRegistry aggregate initialized with event handlers");

        return registry;
    }

    private void handleMessagePublished(MessagePublishedEvent event) {
        log.debug("Message published: messageId={}, topic={}, key={}",
            event.getMessageId(), event.getTopic(), event.getKey());
    }

    private void handleMessageConsumed(MessageConsumedEvent event) {
        log.debug("Message consumed: messageId={}, topic={}, group={}",
            event.getMessageId(), event.getTopic(), event.getConsumerGroup());
    }

    private void handleTopicCreated(TopicCreatedEvent event) {
        log.info("Topic created: name={}, partitions={}, replication={}, type={}",
            event.getTopicName(), event.getPartitions(), event.getReplicationFactor(), event.getType());
    }
}
