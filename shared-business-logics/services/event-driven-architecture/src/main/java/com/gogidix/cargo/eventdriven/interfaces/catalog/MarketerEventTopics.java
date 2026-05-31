package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class MarketerEventTopics {
    private MarketerEventTopics() {}
    public static final String DOMAIN = "marketer";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "order", "created"),
        EventTopic.of(DOMAIN, "order", "updated"),
        EventTopic.of(DOMAIN, "loading", "authorized"),
        EventTopic.of(DOMAIN, "payment", "advice.requested"),
        EventTopic.of(DOMAIN, "insurance", "requested")
    );
}
