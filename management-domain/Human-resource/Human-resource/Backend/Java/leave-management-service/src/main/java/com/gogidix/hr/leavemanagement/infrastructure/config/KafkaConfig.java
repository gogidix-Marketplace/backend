package com.gogidix.hr.leavemanagement.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.leave-events:leave-events}")
    private String leaveEventsTopic;

    @Bean
    public NewTopic leaveEventsTopic() {
        return TopicBuilder.name(leaveEventsTopic).partitions(3).replicas(1).build();
    }
}
