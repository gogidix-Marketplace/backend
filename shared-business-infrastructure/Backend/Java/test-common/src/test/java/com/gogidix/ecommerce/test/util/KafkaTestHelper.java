package com.gogidix.ecommerce.test.util;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import java.util.Properties;

/**
 * Utility class providing helper methods for Kafka-related test operations.
 */
public class KafkaTestHelper {

    /**
     * Default timeout for consuming records (in milliseconds).
     */
    public static final long DEFAULT_CONSUMER_TIMEOUT = 5000;

    /**
     * Creates a Kafka consumer with the given bootstrap servers and group ID.
     *
     * @param bootstrapServers the Kafka bootstrap servers
     * @param groupId the consumer group ID
     * @return a configured KafkaConsumer
     */
    public static KafkaConsumer<String, String> createConsumer(String bootstrapServers, String groupId) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        return new KafkaConsumer<>(properties);
    }

    /**
     * Creates a Kafka consumer with additional configuration properties.
     *
     * @param bootstrapServers the Kafka bootstrap servers
     * @param groupId the consumer group ID
     * @param additionalConfig additional configuration properties
     * @return a configured KafkaConsumer
     */
    public static KafkaConsumer<String, String> createConsumer(String bootstrapServers, String groupId,
                                                              Map<String, Object> additionalConfig) {
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        additionalConfig.forEach(properties::put);
        return new KafkaConsumer<>(properties);
    }

    /**
     * Consumes records from a topic with the default timeout.
     *
     * @param consumer the Kafka consumer
     * @param topic the topic to consume from
     * @return the consumed records
     */
    public static ConsumerRecords<String, String> consumeRecords(Consumer<String, String> consumer, String topic) {
        return consumeRecords(consumer, topic, DEFAULT_CONSUMER_TIMEOUT);
    }

    /**
     * Consumes records from a topic with a specified timeout.
     *
     * @param consumer the Kafka consumer
     * @param topic the topic to consume from
     * @param timeoutMs the timeout in milliseconds
     * @return the consumed records
     */
    public static ConsumerRecords<String, String> consumeRecords(Consumer<String, String> consumer, String topic,
                                                                 long timeoutMs) {
        consumer.subscribe(Collections.singletonList(topic));
        return KafkaTestUtils.getRecords(consumer, Duration.ofMillis(timeoutMs));
    }

    /**
     * Sends a test message using the provided KafkaTemplate.
     *
     * @param kafkaTemplate the KafkaTemplate to use
     * @param topic the topic to send to
     * @param key the message key
     * @param payload the message payload
     */
    public static void sendMessage(KafkaTemplate<String, String> kafkaTemplate, String topic,
                                  String key, String payload) {
        kafkaTemplate.send(topic, key, payload);
    }

    /**
     * Sends a test message with no key using the provided KafkaTemplate.
     *
     * @param kafkaTemplate the KafkaTemplate to use
     * @param topic the topic to send to
     * @param payload the message payload
     */
    public static void sendMessage(KafkaTemplate<String, String> kafkaTemplate, String topic, String payload) {
        kafkaTemplate.send(topic, payload);
    }

    /**
     * Polls for a single record from the specified topic.
     *
     * @param consumer the Kafka consumer
     * @param topic the topic to consume from
     * @param timeoutMs the timeout in milliseconds
     * @return the first record found, or null if no records were found
     */
    public static org.apache.kafka.clients.consumer.ConsumerRecord<String, String> pollSingleRecord(
            Consumer<String, String> consumer, String topic, long timeoutMs) {
        ConsumerRecords<String, String> records = consumeRecords(consumer, topic, timeoutMs);
        return records.isEmpty() ? null : records.iterator().next();
    }

    private KafkaTestHelper() {
        // Utility class - prevent instantiation
    }
}
