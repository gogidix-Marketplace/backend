package com.gogidix.customersupport.phonesupport.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "call_queues")
public class CallQueue extends BaseEntity {

    @Field("queue_name")
    @Indexed
    private String queueName;

    @Field("queue_id")
    @Indexed(unique = true)
    private String queueId;

    @Field("description")
    private String description;

    @Field("status")
    @Indexed
    private QueueStatus status;

    @Field("priority")
    private Integer priority;

    @Field("max_wait_time_seconds")
    private Integer maxWaitTimeSeconds;

    @Field("agent_ids")
    private List<String> agentIds;

    @Field("current_calls_in_queue")
    private Integer currentCallsInQueue;

    @Field("total_calls_today")
    private Integer totalCallsToday;

    @Field("abandoned_calls_today")
    private Integer abandonedCallsToday;

    @Field("average_wait_time_seconds")
    private Long averageWaitTimeSeconds;

    @Field("average_handle_time_seconds")
    private Long averageHandleTimeSeconds;

    @Field("service_level_percentage")
    private Double serviceLevelPercentage;

    @Field("operating_hours")
    private OperatingHours operatingHours;

    @Field("is_active")
    private Boolean isActive;

    public static CallQueue create(String tenantId, String queueName, String queueId) {
        CallQueue queue = new CallQueue();
        queue.setId(java.util.UUID.randomUUID().toString());
        queue.setTenantId(tenantId);
        queue.setQueueName(queueName);
        queue.setQueueId(queueId);
        queue.setStatus(QueueStatus.ACTIVE);
        queue.setCurrentCallsInQueue(0);
        queue.setTotalCallsToday(0);
        queue.setAbandonedCallsToday(0);
        queue.setIsActive(true);
        queue.setCreatedAt(Instant.now());
        queue.setUpdatedAt(Instant.now());
        return queue;
    }

    public enum QueueStatus {
        ACTIVE, INACTIVE, CLOSED
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OperatingHours {
        private Integer startHour;
        private Integer endHour;
        private List<Integer> workingDays;
        private String timezone;
    }
}
