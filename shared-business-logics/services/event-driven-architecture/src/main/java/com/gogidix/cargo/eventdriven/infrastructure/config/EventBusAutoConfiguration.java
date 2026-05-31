package com.gogidix.cargo.eventdriven.infrastructure.config;

import com.gogidix.cargo.eventdriven.application.service.EventBusService;
import com.gogidix.cargo.eventdriven.application.service.EventCatalogService;
import com.gogidix.cargo.eventdriven.domain.port.EventPublisherPort;
import com.gogidix.cargo.eventdriven.domain.port.EventStorePort;
import com.gogidix.cargo.eventdriven.domain.port.EventSubscriberPort;
import com.gogidix.cargo.eventdriven.infrastructure.messaging.KafkaEventPublisherAdapter;
import com.gogidix.cargo.eventdriven.infrastructure.messaging.KafkaEventSubscriberAdapter;
import com.gogidix.cargo.eventdriven.infrastructure.persistence.InMemoryEventStoreAdapter;
import com.gogidix.cargo.eventdriven.infrastructure.serialization.JsonEventSerializer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
public class EventBusAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EventPublisherPort eventPublisherPort(KafkaTemplate<String, String> kafkaTemplate) {
        return new KafkaEventPublisherAdapter(kafkaTemplate);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventSubscriberPort eventSubscriberPort() {
        return new KafkaEventSubscriberAdapter();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventStorePort eventStorePort() {
        return new InMemoryEventStoreAdapter();
    }

    @Bean
    @ConditionalOnMissingBean
    public JsonEventSerializer jsonEventSerializer() {
        return new JsonEventSerializer();
    }

    @Bean
    @ConditionalOnMissingBean
    public EventBusService eventBusService(EventPublisherPort publisher, EventStorePort eventStore) {
        return new EventBusService(publisher, eventStore);
    }

    @Bean
    @ConditionalOnMissingBean
    public EventCatalogService eventCatalogService() {
        return new EventCatalogService();
    }
}
