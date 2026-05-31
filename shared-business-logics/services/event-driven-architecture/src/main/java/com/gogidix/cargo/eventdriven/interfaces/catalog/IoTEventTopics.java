package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class IoTEventTopics {
    private IoTEventTopics() {}
    public static final String DOMAIN = "iot";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "gps", "position"),
        EventTopic.of(DOMAIN, "fuel", "level"),
        EventTopic.of(DOMAIN, "flow", "meter"),
        EventTopic.of(DOMAIN, "seal", "status"),
        EventTopic.of(DOMAIN, "valve", "status"),
        EventTopic.of(DOMAIN, "temperature", "reading"),
        EventTopic.of(DOMAIN, "panic", "button"),
        EventTopic.of(DOMAIN, "alert", "triggered"),
        EventTopic.of(DOMAIN, "device", "offline"),
        EventTopic.of(DOMAIN, "device", "online")
    );
}
