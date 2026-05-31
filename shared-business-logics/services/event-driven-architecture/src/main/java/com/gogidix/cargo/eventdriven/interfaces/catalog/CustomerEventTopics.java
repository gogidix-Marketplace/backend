package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class CustomerEventTopics {
    private CustomerEventTopics() {}
    public static final String DOMAIN = "customer";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "delivery", "tracking.updated"),
        EventTopic.of(DOMAIN, "delivery", "arrived"),
        EventTopic.of(DOMAIN, "verification", "completed"),
        EventTopic.of(DOMAIN, "dispute", "raised"),
        EventTopic.of(DOMAIN, "pod", "collected")
    );
}
