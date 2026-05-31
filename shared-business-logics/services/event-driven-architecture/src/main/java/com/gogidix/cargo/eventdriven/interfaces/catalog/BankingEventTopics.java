package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.List;

public final class BankingEventTopics {
    private BankingEventTopics() {}
    public static final String DOMAIN = "banking";
    public static final List<EventTopic> TOPICS = List.of(
        EventTopic.of(DOMAIN, "payment", "advice.created"),
        EventTopic.of(DOMAIN, "payment", "advice.sent"),
        EventTopic.of(DOMAIN, "payment", "confirmed"),
        EventTopic.of(DOMAIN, "payment", "rejected"),
        EventTopic.of(DOMAIN, "settlement", "completed"),
        EventTopic.of(DOMAIN, "escrow", "created"),
        EventTopic.of(DOMAIN, "escrow", "released")
    );
}
