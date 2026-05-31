package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class OrchestratorEventTopics {
    private OrchestratorEventTopics() {}
    public static final String DOMAIN = "orchestrator";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "shipment", "started"),
        EventTopic.of(DOMAIN, "shipment", "completed"),
        EventTopic.of(DOMAIN, "shipment", "failed"),
        EventTopic.of(DOMAIN, "insurance", "coverage.started"),
        EventTopic.of(DOMAIN, "insurance", "coverage.adjusted"),
        EventTopic.of(DOMAIN, "payment", "started"),
        EventTopic.of(DOMAIN, "payment", "settled"),
        EventTopic.of(DOMAIN, "delivery", "stop.completed"),
        EventTopic.of(DOMAIN, "delivery", "completed"),
        EventTopic.of(DOMAIN, "loading", "started"),
        EventTopic.of(DOMAIN, "loading", "step.completed"),
        EventTopic.of(DOMAIN, "loading", "completed"),
        EventTopic.of(DOMAIN, "loading", "failed")
    );
}
