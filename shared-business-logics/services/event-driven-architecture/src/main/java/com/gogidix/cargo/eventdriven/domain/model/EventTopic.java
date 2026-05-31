package com.gogidix.cargo.eventdriven.domain.model;

public record EventTopic(
    String domain,
    String entity,
    String action,
    String fullTopicName,
    String description
) {
    public static EventTopic of(String domain, String entity, String action) {
        return new EventTopic(domain, entity, action,
            "cargo." + domain + "." + entity + "." + action,
            domain + " " + entity + " " + action);
    }
    public static EventTopic of(String domain, String entity, String action, String description) {
        return new EventTopic(domain, entity, action,
            "cargo." + domain + "." + entity + "." + action, description);
    }
}
