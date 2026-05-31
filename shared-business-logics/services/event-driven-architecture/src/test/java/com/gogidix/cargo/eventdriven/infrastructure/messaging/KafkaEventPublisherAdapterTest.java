package com.gogidix.cargo.eventdriven.infrastructure.messaging;

import com.gogidix.cargo.eventdriven.domain.event.BaseDomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;
import static org.mockito.Mockito.*;
import org.springframework.kafka.support.SendResult;
import java.util.concurrent.CompletableFuture;
import static org.junit.jupiter.api.Assertions.*;

class KafkaEventPublisherAdapterTest {
    private KafkaEventPublisherAdapter adapter;
    private KafkaTemplate<String, String> kafkaTemplate;

    static class TestEvent extends BaseDomainEvent {
        TestEvent() { super("TEST", "t1", "c1"); }
        @Override public String getAggregateType() { return "TEST"; }
        @Override public String getAggregateId() { return "agg-1"; }
    }

    @SuppressWarnings("unchecked")
    @BeforeEach
    void setUp() {
        kafkaTemplate = mock(KafkaTemplate.class);
        when(kafkaTemplate.send(anyString(), anyString(), anyString()))
            .thenReturn(CompletableFuture.completedFuture(mock(SendResult.class)));
        adapter = new KafkaEventPublisherAdapter(kafkaTemplate);
    }

    @Test
    void shouldPublishEventWithKey() {
        TestEvent event = new TestEvent();
        adapter.publish("test.topic", "key-1", event);
        verify(kafkaTemplate, times(1)).send(eq("test.topic"), eq("key-1"), anyString());
    }

    @Test
    void shouldPublishEventWithoutKey() {
        TestEvent event = new TestEvent();
        adapter.publish("test.topic", event);
        verify(kafkaTemplate, times(1)).send(eq("test.topic"), eq("agg-1"), anyString());
    }
}
