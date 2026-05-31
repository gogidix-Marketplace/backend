package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CargoEventTopicsTest {

    @Test
    void shouldContainAllDomainTopics() {
        assertFalse(CargoEventTopics.ALL_TOPICS.isEmpty());
        assertTrue(CargoEventTopics.ALL_TOPICS.size() > 50);
    }

    @Test
    void shouldContainMarketerTopics() {
        assertTrue(CargoEventTopics.ALL_TOPICS.stream()
            .anyMatch(t -> t.fullTopicName().startsWith("cargo.marketer.")));
    }

    @Test
    void shouldContainDepotTopics() {
        assertTrue(CargoEventTopics.ALL_TOPICS.stream()
            .anyMatch(t -> t.fullTopicName().startsWith("cargo.depot.")));
    }

    @Test
    void shouldContainOrchestratorTopics() {
        assertTrue(CargoEventTopics.ALL_TOPICS.stream()
            .anyMatch(t -> t.fullTopicName().startsWith("cargo.orchestrator.")));
    }

    @Test
    void shouldHaveCorrectTopicFormat() {
        for (EventTopic topic : CargoEventTopics.ALL_TOPICS) {
            assertTrue(topic.fullTopicName().startsWith("cargo."),
                "Topic should start with 'cargo.': " + topic.fullTopicName());
        }
    }
}
