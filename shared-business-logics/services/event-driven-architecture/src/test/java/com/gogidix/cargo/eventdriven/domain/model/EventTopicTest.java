package com.gogidix.cargo.eventdriven.domain.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EventTopicTest {

    @Test
    void shouldCreateEventTopicFromParts() {
        EventTopic topic = EventTopic.of("marketer", "order", "created");
        assertEquals("marketer", topic.domain());
        assertEquals("order", topic.entity());
        assertEquals("created", topic.action());
        assertEquals("cargo.marketer.order.created", topic.fullTopicName());
    }

    @Test
    void shouldCreateEventTopicWithDescription() {
        EventTopic topic = EventTopic.of("depot", "loading", "completed", "Depot loading finished");
        assertEquals("Depot loading finished", topic.description());
        assertEquals("cargo.depot.loading.completed", topic.fullTopicName());
    }

    @Test
    void shouldBuildCorrectTopicName() {
        EventTopic topic = EventTopic.of("insurance", "policy", "activated");
        assertEquals("cargo.insurance.policy.activated", topic.fullTopicName());
    }
}
