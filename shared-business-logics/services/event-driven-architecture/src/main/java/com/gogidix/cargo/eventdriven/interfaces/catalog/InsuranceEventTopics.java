package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class InsuranceEventTopics {
    private InsuranceEventTopics() {}
    public static final String DOMAIN = "insurance";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "quote", "requested"),
        EventTopic.of(DOMAIN, "quote", "received"),
        EventTopic.of(DOMAIN, "policy", "created"),
        EventTopic.of(DOMAIN, "policy", "activated"),
        EventTopic.of(DOMAIN, "coverage", "adjusted"),
        EventTopic.of(DOMAIN, "claim", "initiated"),
        EventTopic.of(DOMAIN, "risk", "score.updated")
    );
}
