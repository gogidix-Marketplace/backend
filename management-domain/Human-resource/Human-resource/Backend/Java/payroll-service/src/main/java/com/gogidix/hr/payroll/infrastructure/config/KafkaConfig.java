package com.gogidix.hr.payroll.infrastructure.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;

/**
 * Kafka Configuration
 * Configures Kafka topics for payroll events
 */
@Configuration
public class KafkaConfig {

    @Value("${kafka.topic.payroll-events:payroll-events}")
    private String payrollEventsTopic;

    @Value("${kafka.topic.payslip-events:payslip-events}")
    private String payslipEventsTopic;

    @Bean
    public NewTopic payrollEventsTopic() {
        return TopicBuilder.name(payrollEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic payslipEventsTopic() {
        return TopicBuilder.name(payslipEventsTopic)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
