package com.gogidix.customersupport.slamanagement.application.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gogidix.customersupport.slamanagement.domain.model.SLAPolicy;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SLAPolicyResponseDto {

    private String id;
    private String tenantId;
    private String policyName;
    private String policyCode;
    private String description;
    private Boolean isActive;
    private SLAPolicy.PolicyPriority priority;
    private Integer responseTimeTargetMinutes;
    private Integer resolutionTimeTargetMinutes;
    private Boolean businessHoursOnly;
    private BusinessHoursConfigDto businessHoursConfig;
    private List<String> applicableCategories;
    private List<String> applicablePriorities;
    private List<String> applicableChannels;
    private List<EscalationRuleDto> escalationRules;
    private String timezone;
    private Integer gracePeriodMinutes;
    private PenaltyConfigDto penaltyConfig;
    private NotificationConfigDto notificationConfig;
    private List<String> tags;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    private Instant updatedAt;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BusinessHoursConfigDto {
        private Integer startHour;
        private Integer endHour;
        private List<Integer> workingDays;
        private String timezone;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EscalationRuleDto {
        private Integer level;
        private String name;
        private Long triggerAfterMinutes;
        private String escalateTo;
        private String notifyUsers;
        private String notifyTeams;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PenaltyConfigDto {
        private Boolean enabled;
        private java.util.Map<String, Double> penaltyPerBreach;
        private Double maxPenaltyPerTicket;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NotificationConfigDto {
        private Boolean notifyOnBreach;
        private Boolean notifyBeforeBreach;
        private Integer notifyBeforeMinutes;
        private List<String> notificationRecipients;
    }
}
