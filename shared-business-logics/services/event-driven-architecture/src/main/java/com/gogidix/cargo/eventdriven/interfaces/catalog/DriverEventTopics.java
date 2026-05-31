package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class DriverEventTopics {
    private DriverEventTopics() {}
    public static final String DOMAIN = "driver";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "trip", "assigned"),
        EventTopic.of(DOMAIN, "stop", "arrived"),
        EventTopic.of(DOMAIN, "discharge", "confirmed"),
        EventTopic.of(DOMAIN, "panic", "activated"),
        EventTopic.of(DOMAIN, "offline", "synced")
    );
}
