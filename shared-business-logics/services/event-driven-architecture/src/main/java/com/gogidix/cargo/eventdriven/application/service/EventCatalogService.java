package com.gogidix.cargo.eventdriven.application.service;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import com.gogidix.cargo.eventdriven.interfaces.catalog.CargoEventTopics;

import java.util.*;
import java.util.stream.Collectors;

public class EventCatalogService {
    private final Map<String, EventTopic> topics;

    public EventCatalogService() {
        this.topics = new LinkedHashMap<>();
        for (EventTopic topic : CargoEventTopics.ALL_TOPICS) {
            topics.put(topic.fullTopicName(), topic);
        }
    }

    public List<EventTopic> getAllTopics() { return new ArrayList<>(topics.values()); }

    public List<EventTopic> getTopicsByDomain(String domain) {
        return topics.values().stream()
            .filter(t -> t.domain().equals(domain))
            .collect(Collectors.toList());
    }

    public Optional<EventTopic> getTopic(String fullTopicName) {
        return Optional.ofNullable(topics.get(fullTopicName));
    }

    public List<String> getAllDomainNames() {
        return topics.values().stream().map(EventTopic::domain).distinct().sorted().collect(Collectors.toList());
    }
}
