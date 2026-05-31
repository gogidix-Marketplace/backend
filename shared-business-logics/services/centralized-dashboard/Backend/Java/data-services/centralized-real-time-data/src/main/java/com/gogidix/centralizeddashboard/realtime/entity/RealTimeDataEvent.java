package com.gogidix.centralizeddashboard.realtime.entity;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Document(collection = "real_time_events")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RealTimeDataEvent {

    @Id
    @Field("id")
    @Indexed
    private String id;

    @Field("event_id")
    @Indexed(unique = true)
    private String eventId;

    @Field("source")
    @Indexed
    private String source;

    @Field("event_type")
    @Indexed
    private String eventType;

    @Field("data")
    private String data;

    @Field("user_id")
    @Indexed
    private String userId;

    @Field("session_id")
    @Indexed
    private String sessionId;

    @Field("value")
    private Double value;

    @Field("unit")
    private String unit;

    @CreatedDate
    @Field("created_at")
    @Indexed
    private LocalDateTime createdAt;

    @Field("processed")
    @Builder.Default
    private Boolean processed = false;
}
