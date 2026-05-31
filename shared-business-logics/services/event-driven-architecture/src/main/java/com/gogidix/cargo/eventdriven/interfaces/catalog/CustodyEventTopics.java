package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class CustodyEventTopics {
    private CustodyEventTopics() {}
    public static final String DOMAIN = "custody";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "volume", "updated"),
        EventTopic.of(DOMAIN, "discrepancy", "detected"),
        EventTopic.of(DOMAIN, "theft", "alert"),
        EventTopic.of(DOMAIN, "stop", "completed")
    );
}
