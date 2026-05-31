package com.gogidix.analytics.metrics.infrastructure.messaging.kafka;

import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class KafkaConfigTest {

    @Test
    void kafkaConsumerConfig_createsConsumerFactory() {
        KafkaConsumerConfig config = new KafkaConsumerConfig();
        setField(config, "bootstrapServers", "localhost:9092");

        ConsumerFactory<String, String> factory = config.consumerFactory();
        assertNotNull(factory);
    }

    @Test
    void kafkaConsumerConfig_createsListenerContainerFactory() {
        KafkaConsumerConfig config = new KafkaConsumerConfig();
        setField(config, "bootstrapServers", "localhost:9092");

        ConcurrentKafkaListenerContainerFactory<String, String> factory = config.kafkaListenerContainerFactory();
        assertNotNull(factory);
    }

    @Test
    void kafkaProducerConfig_createsProducerFactory() {
        KafkaProducerConfig config = new KafkaProducerConfig();
        setField(config, "bootstrapServers", "localhost:9092");

        ProducerFactory<String, String> factory = config.producerFactory();
        assertNotNull(factory);
    }

    @Test
    void kafkaProducerConfig_createsKafkaTemplate() {
        KafkaProducerConfig config = new KafkaProducerConfig();
        setField(config, "bootstrapServers", "localhost:9092");

        KafkaTemplate<String, String> template = config.kafkaTemplate();
        assertNotNull(template);
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
