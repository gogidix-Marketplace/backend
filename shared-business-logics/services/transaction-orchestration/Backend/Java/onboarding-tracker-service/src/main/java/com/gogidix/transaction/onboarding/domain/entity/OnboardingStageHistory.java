package com.gogidix.transaction.onboarding.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.UUID;

@Document(collection = "onboarding_stage_history")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OnboardingStageHistory {

    @Id
    @Field("id")
    @Indexed
    @Builder.Default
    private UUID id = UUID.randomUUID();

    @Field("tracker_id")
    @Indexed
    private UUID trackerId;

    @Field("from_stage")
    private OnboardingTracker.OnboardingStage fromStage;

    @Field("to_stage")
    @Indexed
    private OnboardingTracker.OnboardingStage toStage;

    @Field("status")
    private String status;

    @Field("stage_description")
    private String stageDescription;

    @Field("error_message")
    private String errorMessage;

    @Field("duration_milliseconds")
    private Long durationMilliseconds;

    @Field("metadata")
    private String metadata;

    @Field("changed_by")
    private String changedBy;

    @Field("timestamp")
    @CreatedDate
    @Indexed
    private LocalDateTime timestamp;
}
