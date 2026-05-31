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
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Document(collection = "phone_calls")
public class PhoneCall extends BaseEntity {

    @Field("call_id")
    @Indexed(unique = true)
    private String callId;

    @Field("caller_info")
    private CallerInfo callerInfo;

    @Field("agent_id")
    @Indexed
    private String agentId;

    @Field("agent_name")
    private String agentName;

    @Field("call_status")
    @Indexed
    private CallStatus callStatus;

    @Field("call_direction")
    private CallDirection callDirection;

    @Field("started_at")
    @Indexed
    private Instant startedAt;

    @Field("ended_at")
    private Instant endedAt;

    @Field("duration_seconds")
    private Long durationSeconds;

    @Field("recording_url")
    private String recordingUrl;

    @Field("transcript")
    private String transcript;

    @Field("sentiment_score")
    private Double sentimentScore;

    @Field("call_notes")
    private String callNotes;

    @Field("call_tags")
    private List<String> callTags;

    @Field("related_ticket_id")
    private String relatedTicketId;

    @Field("call_queue")
    private String callQueue;

    @Field("wait_time_seconds")
    private Long waitTimeSeconds;

    @Field("hold_time_seconds")
    private Long holdTimeSeconds;

    @Field("transfer_count")
    private Integer transferCount;

    @Field("disposition_code")
    private String dispositionCode;

    @Field("wrap_up_notes")
    private String wrapUpNotes;

    @Field("call_rating")
    private Integer callRating;

    @Field("customer_feedback")
    private String customerFeedback;

    @Field("metadata")
    private Map<String, Object> metadata;

    public static PhoneCall create(String tenantId, String callId, CallerInfo callerInfo, CallDirection direction) {
        PhoneCall call = new PhoneCall();
        call.setId(java.util.UUID.randomUUID().toString());
        call.setTenantId(tenantId);
        call.setCallId(callId);
        call.setCallerInfo(callerInfo);
        call.setCallDirection(direction);
        call.setCallStatus(CallStatus.INITIATED);
        call.setStartedAt(Instant.now());
        call.setTransferCount(0);
        call.setCreatedAt(Instant.now());
        call.setUpdatedAt(Instant.now());
        return call;
    }

    public void answer(String agentId, String agentName) {
        this.agentId = agentId;
        this.agentName = agentName;
        this.callStatus = CallStatus.IN_PROGRESS;
        this.updateTimestamp();
    }

    public void end() {
        this.callStatus = CallStatus.COMPLETED;
        this.endedAt = Instant.now();
        if (this.startedAt != null) {
            this.durationSeconds = java.time.Duration.between(this.startedAt, this.endedAt).getSeconds();
        }
        this.updateTimestamp();
    }

    public enum CallStatus {
        INITIATED, QUEUED, IN_PROGRESS, ON_HOLD, COMPLETED, ABANDONED, FAILED, TRANSFERRED
    }

    public enum CallDirection {
        INBOUND, OUTBOUND, CALLBACK
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CallerInfo {
        private String phoneNumber;
        private String name;
        private String email;
        private String customerId;
        private String accountNumber;
        private String location;
        private String callerId;
    }
}
