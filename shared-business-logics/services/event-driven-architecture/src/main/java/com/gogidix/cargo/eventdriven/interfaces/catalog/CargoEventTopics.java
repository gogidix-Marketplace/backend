package com.gogidix.cargo.eventdriven.interfaces.catalog;

import com.gogidix.cargo.eventdriven.domain.model.EventTopic;
import java.util.ArrayList;
import java.util.List;

public final class CargoEventTopics {
    private CargoEventTopics() {}
    public static final List<EventTopic> ALL_TOPICS = buildAllTopics();

    private static List<EventTopic> buildAllTopics() {
        List<EventTopic> topics = new ArrayList<>();
        topics.addAll(MarketerEventTopics.TOPICS);
        topics.addAll(DepotEventTopics.TOPICS);
        topics.addAll(FleetEventTopics.TOPICS);
        topics.addAll(InsuranceEventTopics.TOPICS);
        topics.addAll(BankingEventTopics.TOPICS);
        topics.addAll(CustodyEventTopics.TOPICS);
        topics.addAll(IoTEventTopics.TOPICS);
        topics.addAll(DriverEventTopics.TOPICS);
        topics.addAll(CustomerEventTopics.TOPICS);
        topics.addAll(OrchestratorEventTopics.TOPICS);
        return topics;
    }
}
