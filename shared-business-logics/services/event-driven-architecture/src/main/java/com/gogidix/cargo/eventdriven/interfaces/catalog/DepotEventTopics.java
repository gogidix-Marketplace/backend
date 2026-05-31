package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class DepotEventTopics {
    private DepotEventTopics() {}
    public static final String DOMAIN = "depot";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "tanker", "arrived"),
        EventTopic.of(DOMAIN, "sobriety", "test.passed"),
        EventTopic.of(DOMAIN, "sobriety", "test.failed"),
        EventTopic.of(DOMAIN, "weighbridge", "pre-load"),
        EventTopic.of(DOMAIN, "safety", "cleared"),
        EventTopic.of(DOMAIN, "bay", "assigned"),
        EventTopic.of(DOMAIN, "loading", "started"),
        EventTopic.of(DOMAIN, "loading", "completed"),
        EventTopic.of(DOMAIN, "sealing", "completed"),
        EventTopic.of(DOMAIN, "weighbridge", "post-load"),
        EventTopic.of(DOMAIN, "tanker", "dispatched"),
        EventTopic.of(DOMAIN, "discrepancy", "detected")
    );
}
