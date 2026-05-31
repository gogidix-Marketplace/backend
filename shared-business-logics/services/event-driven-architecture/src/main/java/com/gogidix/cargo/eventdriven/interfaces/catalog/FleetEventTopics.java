package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class FleetEventTopics {
    private FleetEventTopics() {}
    public static final String DOMAIN = "fleet";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "transport", "assigned"),
        EventTopic.of(DOMAIN, "vehicle", "registered"),
        EventTopic.of(DOMAIN, "driver", "verified"),
        EventTopic.of(DOMAIN, "compliance", "checked")
    );
}
